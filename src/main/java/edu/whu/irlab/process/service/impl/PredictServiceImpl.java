package edu.whu.irlab.process.service.impl;

import edu.whu.irlab.model.PredictRecord;
import edu.whu.irlab.process.model.PreProcessProps;
import edu.whu.irlab.process.model.Command;
import edu.whu.irlab.process.model.InputParam;
import edu.whu.irlab.process.service.PreProcessService;
import edu.whu.irlab.process.service.PredictService;
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
public class PredictServiceImpl implements PredictService {
    private static Logger logger = LoggerFactory.getLogger(PredictServiceImpl.class);

    @Autowired
    private PreProcessProps preProcessProps;

    @Autowired
    private PreProcessService preProcessService;

    @Override
    public PredictRecord doPredict(InputParam inputParam) {
        Command command = getCommand(inputParam);
        String dataPath = getPredictDataPath(inputParam.getMonths());
        command.setPredictDataPath(dataPath);

        int exitValue = -1;
        String cmd = command.getCommand();
        System.out.println(cmd);
        logger.info(cmd);
        String predictResultPath = null;
        try {
            // 读取预测索引文件
            BufferedReader brIndex = new BufferedReader(new FileReader(dataPath+"index.txt"));
            List<String> splitDataPath = new ArrayList<>();
            System.out.println(brIndex.readLine());
            brIndex.readLine();
            String splitTmpLine = null;
            while ((splitTmpLine=brIndex.readLine())!=null){
                splitDataPath.add(splitTmpLine.trim().split(":")[1]);
            }
            brIndex.close();


            // 执行python 脚本,并获取输入流

            String predictTmpResultPath = dataPath+"tmp.result";
            Process pr = Runtime.getRuntime().exec(cmd);
            BufferedWriter bw = new BufferedWriter(new FileWriter(predictTmpResultPath));

            // 标准输出流 存到tmp.result
            BufferedReader br = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String lineResult = null;
            while ((lineResult=br.readLine())!=null){
                bw.write(lineResult);
                bw.newLine();
            }
            br.close();
            bw.close();

            // 错误输出流
            BufferedWriter bwError = null;
            if (!inputParam.isStdOut()){
                String[] splitsDir = dataPath.split(File.separator);
                String logPath = preProcessProps.getProp("LOG_ROOT")+File.separator+"predict."+ splitsDir[splitsDir.length-1] +".log";
                bwError = new BufferedWriter(new FileWriter(logPath));
            }
            BufferedReader brError = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
            String lineError = null;
            while ((lineError=brError.readLine())!=null){
                outputInfo(lineError.trim(), bwError);
            }
            brError.close();
            if (bwError!=null){
                bwError.close();
            }

            exitValue = pr.waitFor();
            pr.destroy();

            // 合并tel to result
            BufferedReader brTmp = new BufferedReader(new FileReader(predictTmpResultPath));
            String tmpResult = null;
            predictResultPath = dataPath+"predict."+command.getModel()+".result";
            BufferedWriter bwFinal = new BufferedWriter(new FileWriter(predictResultPath));
            int readC = 0;
            int writeC = 0;
            for (String path: splitDataPath){
                BufferedReader brSplit = new BufferedReader(new FileReader(dataPath+path));
                String line = null;
                while ((line=brSplit.readLine())!=null){
                    String tel = line.trim().split(",")[0];
                    if ((tmpResult=brTmp.readLine())!=null){
                        bwFinal.write(tel+","+tmpResult.trim());
                        bwFinal.newLine();
                        writeC++;
                    }
                    readC++;
                }
            }
            brTmp.close();
            bwFinal.close();
            System.out.println("readC:"+readC+"\t"+"writeC:"+writeC);
            System.out.println("预测结果保存的位置:"+predictResultPath);
            // 删除predictTmpResultPath
            new File(predictTmpResultPath).delete();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        PredictRecord predictRecord = new PredictRecord(inputParam);
        predictRecord.setIndexPath(dataPath+"index.txt");
        predictRecord.setResultPath(predictResultPath);
        predictRecord.setState(0);
        return predictRecord;
    }

    private Command getCommand(InputParam inputParam){
        Command command = new Command(preProcessProps);
        command.setMode(preProcessProps.getProp("MODE_PREDICT"));
        if (inputParam.getModel() != null){
            command.setModel(inputParam.getModel());
        }
        // if (inputParam.getModelPath() == null){
        //     command.setModelPath(preProcessProps.getProp("MODEL_SAVE_DIR")+File.separator+inputParam.getModel()+".model");
        // }
        command.setModelPath(inputParam.getModelPath());
        return command;
    }

    public String getPredictDataPath(List<String> months){
        String dataPath = null;
        dataPath = genMultiMonth(months);
        System.out.println("predictDataPath:"+dataPath);
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
     * preprocess two months' data to generate feature data for prediction
     * @param thisMonth this Month file path
     * @param nextMonth next Month file path
     */
    private String genTwoMonth(String thisMonth, String nextMonth){
        List<String> months = new ArrayList<String>();
        months.add(thisMonth);
        months.add(nextMonth);

        return genMultiMonth(months);
    }

    /**
     * preprocess two months' data to generate feature data for prediction
     * @param months the list of the path of each month's file
     */
    private String genMultiMonth(List<String> months){
        String dataPath = null;
        List<File> files = new ArrayList<File>();
        for (String month: months){
            files.add(new File(preProcessProps.getProp("DATA_TO_PREDICT_DIR")+File.separator+month));
        }

        int N = months.size();
        String predictPath = files.get(N-1).getName().split("\\.")[0]+"_"+N;

        dataPath = preProcessService.genPredictData(files, predictPath);
        return dataPath;
    }
}
