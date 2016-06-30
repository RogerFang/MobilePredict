package edu.whu.irlab.service.impl;

import edu.whu.irlab.model.PredictRecord;
import edu.whu.irlab.process.model.PreProcessProps;
import edu.whu.irlab.process.service.ExSortService;
import edu.whu.irlab.process.service.PreProcessService;
import edu.whu.irlab.process.service.PredictService;
import edu.whu.irlab.service.DisplayService;
import edu.whu.irlab.service.PredictRecordService;
import edu.whu.irlab.util.CalendarUtil;
import edu.whu.irlab.util.StrUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

/**
 * Created by Roger on 2016/5/19.
 */
@Service
public class DisplayServiceImpl implements DisplayService {
    private static Logger logger = LoggerFactory.getLogger(DisplayServiceImpl.class);

    @Autowired
    private PreProcessProps preProcessProps;

    @Autowired
    private PredictService predictService;

    @Autowired
    private PredictRecordService predictRecordService;

    @Autowired
    private PreProcessService preProcessService;

    @Autowired
    private ExSortService exSortService;

    /**
     * 计算上个月的预测准确率
     * @param predictRecord 上个月进行预测的记录
     */
    @Override
    public void calculatePredictPrecision(PredictRecord predictRecord) throws IOException {
        // 正在训练设为 -1
        predictRecord.setPredictPrecision("-1");
        predictRecordService.updateByModelAndPredictMonth(predictRecord);

        // 这个月, 是上个月的predictMonth
        String curMonth = CalendarUtil.getCurMonth();
        File curMonthFile = new File(preProcessProps.getProp("DATA_TO_PREDICT_DIR") + File.separator + curMonth + ".txt");
        // 判断这个月新上传的数据是否存在
        if (!curMonthFile.exists()){
            logger.info("Calculate predict precision: 最新数据文件不存在");
            return;
        }

        List<File> cmpList = new ArrayList<>();
        File curSortedFile = exSortService.mSort(curMonthFile);
        File resultFile = new File(predictRecord.getResultPath());
        cmpList.add(curSortedFile);
        cmpList.add(resultFile);

        logger.info("Calculate predict precision: 开始计算predict precision; curMonth:{}, curMonthFile:{}, resultFile:{}", curMonth, curMonthFile.getAbsolutePath(), resultFile.getAbsolutePath());

        List<File> cmpFile = genCompareFile(cmpList);
        System.out.println("to compare:  "+cmpFile);
        Long correctNum = 0l;
        Long allNum =0l;
        for (File file: cmpFile){
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line=br.readLine())!=null){
                allNum++;
                String[] array = line.split(",");
                if (array[1].equals(array[2])){
                    correctNum++;
                }
            }
            br.close();
            file.delete();
        }
        curSortedFile.delete();

        System.out.println(correctNum + "\t" +allNum);
        String precision = String.format("%.2f", (double)correctNum/allNum);
        predictRecord.setPredictPrecision(precision);

        predictRecordService.updateByPrimaryKeySelective(predictRecord);
        logger.info("Calculate predict precision: curMonth:{},计算完成predict precision", curMonth);
    }

    private List<String> getSplitPath(String dataPath){
        List<String> curSplitPath = new ArrayList<>();
        try {
            File indexFile = new File(dataPath+File.separator+"index.txt");
            BufferedReader brIndex = new BufferedReader(new FileReader(indexFile));
            brIndex.readLine();
            brIndex.readLine();
            String line = null;
            while ((line=brIndex.readLine())!=null){
                curSplitPath.add(dataPath + File.separator + line.split(":")[1]);
            }
            brIndex.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return curSplitPath;
    }

    private List<File> genCompareFile(List<File> files) throws IOException {
        List<File> featureDataFiles = new ArrayList<File>();

        int flag = 0;
        int stop = 0;
        for (int i=0; i<files.size(); i++){
            stop += (int)Math.pow(2, i);
        }

        Set<String> interTels = new TreeSet<String>();

        // 文件对应的读取流
        List<BufferedReader> brList = new ArrayList<BufferedReader>();
        // 文件对应的Tel集合
        List<Set<String>> cacheList = new ArrayList<Set<String>>();
        // 文件对应的记录, key=Tel,val=propertiesList
        List<Map<String, List<String>>> cacheRcoList = new ArrayList<Map<String, List<String>>>();
        for (File file: files){
            BufferedReader br = new BufferedReader(new FileReader(file));
            brList.add(br);
            Set<String> perCacheSet = new TreeSet<String>();
            cacheList.add(perCacheSet);
            Map<String, List<String>> perCacheRcoMap = new HashMap<String, List<String>>();
            cacheRcoList.add(perCacheRcoMap);
        }

        while (flag != stop){
            for (int i=0; i<cacheList.size(); i++){
                if(pushCache(cacheList.get(i), cacheRcoList.get(i), brList.get(i)) < 0 ){
                    flag = flag|(int)Math.pow(2, i);
                }
            }
            interTels.addAll(intersection(cacheList));

            for (int i=0; i<cacheList.size(); i++){
                cacheList.get(i).removeAll(interTels);
            }

            if (interTels.size() >= Integer.parseInt(preProcessProps.getProp("INTER_COUNT_IN_CACHE"))){
                featureDataFiles.addAll(saveData(interTels, cacheRcoList, false));
            }
        }
        featureDataFiles.addAll(saveData(interTels, cacheRcoList, true));

        for (BufferedReader br: brList){
            if (br!=null){
                br.close();
            }
        }
        return featureDataFiles;
    }

    private int pushCache(Set<String> set, Map<String, List<String>> map,BufferedReader br) throws IOException {
        while (set.size() < Integer.parseInt(preProcessProps.getProp("PER_FILE_COUNT_IN_CACHE"))){
            String line = br.readLine();
            if (line!=null){
                if (!line.isEmpty()){
                    String[] props = line.trim().split(",");
                    String tel = props[0];
                    set.add(tel);
                    map.put(tel, Arrays.asList(props));
                }
            }else {
                return -1;
            }
        }
        return 0;
    }

    /**
     * this method is used to intersect each month's telephone set
     * @param cacheList the list of set of telephones in each month
     * @return the intersection set of each month's telephone
     */
    private Set<String> intersection(List<Set<String>> cacheList){
        // 所有月份的电话号码(交集)
        Set<String> interTels = null;
        if (cacheList.size()==1){
            interTels = cacheList.get(0);
        }else if (cacheList.size() > 1){
            interTels = new HashSet<String>(CollectionUtils.intersection(cacheList.get(0), cacheList.get(1)));
            if (cacheList.size()>2){
                for (Set<String> tmp: cacheList.subList(2, cacheList.size())){
                    interTels = new HashSet<String>(CollectionUtils.intersection(interTels, tmp));
                }
            }
        }
        return interTels;
    }

    /**
     * when the interTels count is up to INTER_COUNT_IN_CACHE, then save the data
     * @param interTels the intersection set of each month's telephone
     * @param cacheRcoList the months' record in cache
     * @param isFull true represents save all record; false represents still caching records whose count doesn't reach INTER_COUNT_IN_CACHE
     * @return the tmp split file of feature data
     * @throws IOException
     */
    private List<File> saveData(Set<String> interTels, List<Map<String, List<String>>> cacheRcoList, boolean isFull) throws IOException {
        List<File> featureDataFiles = new ArrayList<File>();

        System.out.println("------save feature data------");
        Iterator<String> iter = interTels.iterator();
        long saveNum = interTels.size()/Integer.parseInt(preProcessProps.getProp("INTER_COUNT_IN_CACHE"));
        for (int i=0; i<saveNum; i++){
            featureDataFiles.add(doSaveData(iter, cacheRcoList));
        }

        // 将剩下的数据全部保存
        if (isFull){
            featureDataFiles.add(doSaveData(iter, cacheRcoList));
        }
        return featureDataFiles;
    }

    /**
     * when the interTels count is up to INTER_COUNT_IN_CACHE, the real method to save data
     * @param iter the Iterator of interTels in cache
     * @param cacheRcoList the months' record in cache
     * @return the split feature file
     * @throws IOException
     */
    private File doSaveData(Iterator<String> iter, List<Map<String, List<String>>> cacheRcoList) throws IOException {
        String savepath = preProcessProps.getProp("TMP_DIR")+File.separator+"data_"+UUID.randomUUID() + ".txt";
        File file = new File(savepath);
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));

        int recordCount = 0;
        int n = 0;
        while (iter.hasNext()){
            String tel = iter.next();
            List<List<String>> tmpList = new ArrayList<List<String>>();
            for (int j=0; j<cacheRcoList.size(); j++){
                Map<String, List<String>> tmpMap = cacheRcoList.get(j);
                tmpList.add(tmpMap.get(tel));
                tmpMap.remove(tel);
            }

            // 存储特征数据
            String content = doGenFeatureData(tel, tmpList);
            if (content!=null){
                bw.write(content);
                bw.newLine();
                recordCount++;
            }
            n++;
            iter.remove();

            if (n == Integer.parseInt(preProcessProps.getProp("INTER_COUNT_IN_CACHE"))){
                break;
            }
        }
        bw.close();
        System.out.println("save :" + "\t" + savepath + "\t" + recordCount);
        return file;
    }

    /**
     * this method is to make the each property numeric in feature data
     * @param record the feature record of each telephone in month's file
     * @return the feature String of each telephone in month's file
     */
    private String doGenFeatureData(String tel, List<List<String>> record){
        List<String> featureRecord = new ArrayList<String>();
        featureRecord.add(tel);
        String curTag = null;
        if (record.get(0).get(2).equals("在网-开通")){
            curTag = "1";
        }else {
            curTag = "0";
        }
        featureRecord.add(curTag);

        String resultTag = record.get(1).get(1);
        featureRecord.add(resultTag);
        return StrUtil.strJoin(featureRecord, ",");
    }
}
