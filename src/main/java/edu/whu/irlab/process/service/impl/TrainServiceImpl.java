package edu.whu.irlab.process.service.impl;

import edu.whu.irlab.model.TrainRecord;
import edu.whu.irlab.process.model.Command;
import edu.whu.irlab.process.model.InputParam;
import edu.whu.irlab.process.model.PreProcessProps;
import edu.whu.irlab.process.service.PreProcessService;
import edu.whu.irlab.process.service.TrainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roger on 2016/5/17.
 */
@Service
public class TrainServiceImpl implements TrainService {
    private static Logger logger = LoggerFactory.getLogger(TrainServiceImpl.class);

    @Autowired
    private PreProcessProps preProcessProps;

    @Autowired
    private PreProcessService preProcessService;

    @Override
    public TrainRecord doTrain(InputParam inputParam) {
        Command command = getCommand(inputParam);
        String[] dataPath = getTrainAndTestDataPath(inputParam.getModel(), inputParam.getMonths());
        command.setTrainDataPath(dataPath[0]);
        command.setTestDataPath(dataPath[1]);

        int exitValue = -1;
        String cmd = command.getCommand();
        logger.info(cmd);

        boolean isCompleted = false;
        String precision = null;
        try {
            BufferedWriter bw = null;
            if (!inputParam.isStdOut()){
                File dataFile = new File(dataPath[0]);
                String name = dataFile.getName();
                System.out.println("name:"+name);
                // String[] splitsDir = dataPath[0].split(File.separator);
                // String logPath = preProcessProps.getProp("LOG_ROOT")+File.separator+"train."+ splitsDir[splitsDir.length-1] +".log";
                String logPath = preProcessProps.getProp("LOG_ROOT")+File.separator+"train."+ name +".log";
                bw = new BufferedWriter(new FileWriter(logPath));
            }

            Process pr = Runtime.getRuntime().exec(cmd);

            BufferedReader brInput = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String lineInput = null;
            while ((lineInput=brInput.readLine())!=null){
                if(lineInput.startsWith("test accuracy:")){
                    precision = lineInput.split(":")[1];
                    logger.info("if precision: " + precision);
                }

                if (lineInput.trim().equals("training complete!")){
                    isCompleted = true;
                }
                outputInfo(lineInput.trim(), bw);
            }
            brInput.close();

            logger.info("precision: " + precision);

            BufferedReader brError = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
            String lineError = null;
            while ((lineError = brError.readLine()) != null) {
                outputInfo(lineError.trim(), bw);
            }
            brError.close();

            if (bw!=null){
                bw.close();
            }

            exitValue = pr.waitFor();
            pr.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TrainRecord trainRecord = new TrainRecord(inputParam);
        trainRecord.setTrainPrecision(precision);
        if (isCompleted){
            trainRecord.setState(0);
        }else {
            trainRecord.setState(2);
        }

        return trainRecord;
    }

    private Command getCommand(InputParam inputParam){
        Command command = new Command(preProcessProps);
        command.setMode(preProcessProps.getProp("MODE_TRAIN"));
        if (inputParam.getModel() != null){
            command.setModel(inputParam.getModel());
        }
        // if (inputParam.getModelPath() == null){
        //     command.setModelPath(preProcessProps.getProp("MODEL_SAVE_DIR")+File.separator+inputParam.getModel()+".model");
        // }
        command.setModelPath(inputParam.getModelPath());
        return command;
    }

    private String[] getTrainAndTestDataPath(String model, List<String> months){
        String[] dataPath = null;
        if (model.equals(preProcessProps.getProp("MODEL_RNN"))){
            if (months.size() < 3){
                throw new RuntimeException("the input months List size must be at least 3!");
            }
        }else {
            if (months.size() != 2){
                throw new RuntimeException("the input months List size must be 2!");
            }
        }

        if (model.equals(preProcessProps.getProp("MODEL_CMLP")) || model.equals(preProcessProps.getProp("MODEL_CNN"))){
            dataPath = genClassify(months.get(0), months.get(1));
        }else if (model.equals(preProcessProps.getProp("MODEL_RNN"))){
            dataPath = genRNN(months);
        }else if (model.equals(preProcessProps.getProp("MODEL_LINEAR")) || model.equals(preProcessProps.getProp("MODEL_RMLP"))){
            dataPath = genRegress(months.get(0), months.get(1));
        }

        System.out.println("trainDataPath:" + dataPath[0]);
        System.out.println("testDataPath:"+dataPath[1]);
        return dataPath;
    }

    private void outputInfo(String line, BufferedWriter bw) throws IOException {
        if (bw == null){
            System.out.println(line);
        }else {
            bw.write(line);
            bw.newLine();
        }
    }

    /**
     * preprocess two months' data to generate feature data for classification
     * @param thisMonth this Month file path
     * @param nextMonth next Month file path
     * @return index 0 represents trainDataPath; index 1 represents testDataPath
     */
    private String[] genClassify(String thisMonth, String nextMonth){
        return genTwoMonth(thisMonth, nextMonth, true);
    }

    /**
     * preprocess multiple months' data to generate feature data for RNN
     * @param months the list of the path of each month's file
     * @return index 0 represents trainDataPath; index 1 represents testDataPath
     */
    private String[] genRNN(List<String> months){
        return genMultiMonth(months, true);
    }

    /**
     * preprocess two months' data to generate feature data for regression
     * @param thisMonth this Month file path
     * @param nextMonth next Month file path
     * @return index 0 represents trainDataPath; index 1 represents testDataPath
     */
    private String[] genRegress(String thisMonth, String nextMonth){
        return genTwoMonth(thisMonth, nextMonth, false);
    }

    /**
     * @return index 0 represents trainDataPath; index 1 represents testDataPath
     */
    private String[] genTwoMonth(String thisMonth, String nextMonth, boolean isClassification){
        List<String> months = new ArrayList<String>();
        months.add(thisMonth);
        months.add(nextMonth);

        return genMultiMonth(months, isClassification);
    }

    /**
     * @return index 0 represents trainDataPath; index 1 represents testDataPath
     */
    private String[] genMultiMonth(List<String> months, boolean isClassification){
        String[] dataPath = null;

        List<File> files = new ArrayList<File>();
        for (String month: months){
            files.add(new File(preProcessProps.getProp("DATA_TO_TRAIN_DIR")+File.separator+month));
        }

        int N = months.size();
        String path;
        if (isClassification){
            path = "classify_"+files.get(N-1).getName().split("\\.")[0]+"_"+(N-1);
        }else {
            path = "regress_"+files.get(N-1).getName().split("\\.")[0]+"_"+(N-1);
        }

        dataPath = preProcessService.genTrainAndTestData(files, isClassification, path);

        return dataPath;
    }
}
