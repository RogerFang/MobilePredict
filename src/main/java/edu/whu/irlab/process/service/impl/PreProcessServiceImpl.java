package edu.whu.irlab.process.service.impl;

import edu.whu.irlab.process.model.PreProcessProps;
import edu.whu.irlab.process.service.ExSortService;
import edu.whu.irlab.process.service.PreProcessService;
import edu.whu.irlab.util.StrUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

/**
 * Created by Roger on 2016/5/17.
 */
@Service
public class PreProcessServiceImpl implements PreProcessService{

    @Autowired
    private PreProcessProps preProcessProps;

    @Autowired
    private ExSortService exSortService;

    // train&test or predict
    private boolean isTrain;

    // classify or regress
    private boolean isClassification;

    // // line count of per file in cache
    // public final long PER_FILE_COUNT_IN_CACHE = Long.parseLong(preProcessProps.getProp("PER_FILE_COUNT_IN_CACHE"));
    //
    // // inner file line count to save(intersection count)
    // public final long INTER_COUNT_IN_CACHE = Long.parseLong(preProcessProps.getProp("INTER_COUNT_IN_CACHE"));
    //
    // // save final feature data(train,test,predict) per file count
    // public final long PER_FILE_COUNT = Long.parseLong(preProcessProps.getProp("PER_FILE_COUNT"));
    //
    // // train feature data save dir
    // private final String TRAIN_DIR = preProcessProps.getProp("TRAIN_DIR")+File.separator;
    //
    // // test feature data save dir
    // private final String TEST_DIR = preProcessProps.getProp("TEST_DIR")+File.separator;
    //
    // // tmp file save dir
    // private final String TMP_DIR = preProcessProps.getProp("TMP_DIR")+File.separator;
    //
    // // predict feature data save dir
    // private final String PREDICT_DIR = preProcessProps.getProp("PREDICT_DIR")+File.separator;

    // per train sub path
    private String trainPath = null;
    // per test sub path
    private String testPath = null;
    // per predict sub path
    private String predictPath = null;

    // public PreProcess(){
    //     mkdir(TMP_DIR);
    //     mkdir(TRAIN_DIR);
    //     mkdir(PREDICT_DIR);
    //
    //     clearPath(new File(TMP_DIR));
    // }

    private void setIsTrain(boolean isTrain){
        this.isTrain = isTrain;
    }

    private void setIsClassification(boolean isClassification){
        this.isClassification = isClassification;
    }

    private void setTrainTestPath(String path){
        File fileTrain = new File(preProcessProps.getProp("TRAIN_DIR") + File.separator + path + File.separator);
        if (!fileTrain.exists()){
            fileTrain.mkdirs();
        }else {
            clearPath(fileTrain);
        }
        this.trainPath = fileTrain.getAbsolutePath() + File.separator;

        File fileTest = new File(preProcessProps.getProp("TEST_DIR") + File.separator + path + File.separator);
        if (!fileTest.exists()){
            fileTest.mkdirs();
        }else {
            clearPath(fileTest);
        }
        this.testPath = fileTest.getAbsolutePath() + File.separator;
    }

    private String getTrainPath(){
        return this.trainPath;
    }

    private String getTestPath(){
        return this.testPath;
    }

    private void setPredictPath(String path){
        File filePredict = new File(preProcessProps.getProp("PREDICT_DIR")+File.separator + path + File.separator);
        if (!filePredict.exists()){
            filePredict.mkdirs();
        }else {
            clearPath(filePredict);
        }
        this.predictPath = filePredict.getAbsolutePath() + File.separator;
    }

    private String getPredictPath(){
        return this.predictPath;
    }


    private void clearPath(File file){
        for (File f: file.listFiles()){
            if (f.isFile()){
                f.delete();
            }
        }
    }

    private void mkdir(String dirpath){
        File dir = new File(dirpath);
        if (!dir.exists()){
            dir.mkdirs();
        }
    }

    private void init(){
        mkdir(preProcessProps.getProp("TMP_DIR"));
        mkdir(preProcessProps.getProp("TRAIN_DIR"));
        mkdir(preProcessProps.getProp("PREDICT_DIR"));

        clearPath(new File(preProcessProps.getProp("TMP_DIR")));
    }

    /**
     * the enter to generate feature data for train,test
     * @param files the origin files of months' data, used to generate feature data
     * @param isClassification determine to generate feature data for classify or regress
     * @param path the path to save sets of splits' feature files
     * @throws IOException
     */
    public String[] genTrainAndTestData(List<File> files, boolean isClassification,String path){
        init();
        setIsTrain(true);
        setIsClassification(isClassification);
        setTrainTestPath(path);
        try {
            splitTrainAndTestData(genData(files));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String[]{getTrainPath(), getTestPath()};
    }

    /**
     * the enter to generate feature data for predict
     * @param files the origin files of months' data, used to generate feature data
     * @param path the path to save sets of splits' feature files
     * @throws IOException
     */
    public String genPredictData(List<File> files, String path) {
        init();
        setIsTrain(false);
        setPredictPath(path);
        try {
            splitPredictData(genData(files));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getPredictPath();
    }

    /**
     * the enter to generate feature data for train,test or predict
     * @param files the origin files of months' data, used to generate feature data
     * @return feature data files,but per file size may be unequal
     * @throws IOException
     */
    private List<File> genData(List<File> files) throws IOException {
        List<File> featureDataFiles = doGenData(exSort(files));
        return featureDataFiles;
    }

    /**
     * this method is used to external sort origin file of months
     * @param files the origin files of months
     * @return the sorted files according to the origin months' files
     * @throws IOException
     */
    private List<File> exSort(List<File> files) throws IOException {
        List<File> sortedFiles = new ArrayList<File>();
        for (File file: files){
            sortedFiles.add(exSortService.mSort(file));
        }
        return sortedFiles;
    }

    /**
     *
     * @param files the sorted files according to the origin months' files
     * @return the tmp files contains of intersection in months' files, each count of tmp file is INTER_COUNT_IN_CACHE
     * @throws IOException
     */
    private List<File> doGenData(List<File> files) throws IOException {
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

        // delete tmp merge file
        for (File file: files){
            file.delete();
        }
        return featureDataFiles;
    }

    /**
     * read the tmp intersection file into cache to make cache contains PER_FILE_COUNT_IN_CACHE record
     * @param set set of intersection telephones in months' files
     * @param map record of each intersection telephone in months' files
     * @param br the tmp BufferedReader is user to read the tmp intersection file
     * @return -1 represents this file has finished read; 0 represents this file can be read more
     * @throws IOException
     */
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
        int monthNum = 0;
        for (List<String> perMonthData: record){
            List<String> perMonthFeatureList = new ArrayList<String>();
            // 手机号码，去掉
            // 首先只判断这个月为开通状态的手机号码
//            if (!perMonthData.get(2).equals("在网-开通")){
//                return null;
//            }
            // 第一个特征,第一次欠费距离本月的月数
            String[] now = perMonthData.get(1).split("-");
            String[] last = perMonthData.get(5).split("-");
            if (now.length<2 || last.length<2){
                return null;
            }
            perMonthFeatureList.add(String.valueOf((Integer.parseInt(now[0]) - Integer.parseInt(last[0])) * 12 + (Integer.parseInt(now[1]) - Integer.parseInt(last[1]))));
            // 使用子品牌编号
            perMonthFeatureList.add(perMonthData.get(4).split("-")[0]);
            // 次欠费的时间转化为历史上是否欠费
            if (perMonthData.get(6).equals("0")){
                perMonthFeatureList.add(perMonthData.get(6));
            }else {
                perMonthFeatureList.add("1");
            }

            // feature
            for (int i=0; i<perMonthData.size(); i++){
                if (i<7){
                    continue;
                }else if (i==62 || i==61){
                    continue;
                }else  if (i==perMonthData.size()-1){
                    continue;
                }

                String tmp = perMonthData.get(i);
                if (tmp.equals("是")){
                    tmp = "1";
                }else if (tmp.equals("否")){
                    tmp = "0";
                }else if (tmp.equals("")){
                    tmp = "0";
                }else if (tmp.equals("不详")){
                    tmp = "0";
                }

                // 销售渠道
                if (tmp.equals("代销商")){
                    tmp = "0";
                }else if (tmp.equals("零售店")){
                    tmp = "1";
                }else if (tmp.equals("营业厅")){
                    tmp = "2";
                }

                // 付款方式
                if (tmp.equals("现金")){
                    tmp = "1";
                }else if (tmp.equals("借记卡")){
                    tmp = "2";
                }else if (tmp.equals("银行托收")){
                    tmp = "3";
                }else if (tmp.equals("信用卡")){
                    tmp = "4";
                }else if (tmp.equals("支票")){
                    tmp = "5";
                }else if (tmp.equals("未知")){
                    tmp = "6";
                }else if (tmp.equals("级别不详")){
                    tmp = "0";
                }

                perMonthFeatureList.add(tmp);
            }

            if (monthNum==record.size()-1){
                if (isTrain){
                    // train&test
                    if (isClassification){
                        // classify
                        if (record.get(monthNum).get(2).equals("在网-开通")){
                            featureRecord.add("1");
                        }else{
//                            System.out.println(perMonthFeatureList.get(2) + "\t" + record.get(monthNum));
                            featureRecord.add("0");
                        }
                    }else {
                        // regress
                        featureRecord.add(record.get(monthNum).get(22));
                    }
                }else {
                    // predict
                    featureRecord.add(StrUtil.strJoin(perMonthFeatureList, ","));
                }
            }else {
                featureRecord.add(StrUtil.strJoin(perMonthFeatureList, ","));
            }

            monthNum++;
        }

//        if (featureRecord.size()!=record.size()){
//            System.out.println("出问题了"+"\t"+featureRecord.size()+"\t"+record.size()+"\t"+record.get(record.size()-1));
//            return null;
//        }

        return StrUtil.strJoin(featureRecord, ",");
    }

    /**
     * the method is to read the tmp train&test feature files, then split them to train and test data
     * @param files the tmp feature files
     * @throws IOException
     */
    private void splitTrainAndTestData(List<File> files) throws IOException {
        System.out.println("#split train test data#");
        long allCount = 0;

        long allTrainCount = 0;
        long perCount = 0;
        int fileCount = 0;

        long allTestCount = 0;
        long perTestCount = 0;
        int fileTestCount = 0;

        List<String> splitRecords = new ArrayList<String>();
        List<String> splitTestRecords = new ArrayList<String>();

        File saveFile = getTrainAndTestSaveFile(fileCount, true);
        BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile));

        File testFile = getTrainAndTestSaveFile(0, false);
        BufferedWriter bwTest = new BufferedWriter(new FileWriter(testFile));

        for (File file: files){
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line=br.readLine())!=null){
                if (!line.isEmpty()){
                    if (allCount%10 == 0){
                        bwTest.write(line.trim());
                        bwTest.newLine();
                        perTestCount++;
                        allTestCount++;
                        if (perTestCount == Integer.parseInt(preProcessProps.getProp("PER_FILE_COUNT"))){
                            splitTestRecords.add(testFile.getName());

                            bwTest.close();
                            fileTestCount++;
                            testFile = getTrainAndTestSaveFile(fileTestCount, true);
                            bwTest = new BufferedWriter(new FileWriter(testFile));
                            perTestCount = 0;
                        }
                    }else{
                        bw.write(line.trim());
                        bw.newLine();
                        perCount++;
                        allTrainCount++;
                        if (perCount == Integer.parseInt(preProcessProps.getProp("PER_FILE_COUNT"))){
                            splitRecords.add(saveFile.getName());

                            bw.close();
                            fileCount++;
                            saveFile = getTrainAndTestSaveFile(fileCount, true);
                            bw = new BufferedWriter(new FileWriter(saveFile));
                            perCount = 0;
                        }
                    }
                    allCount++;
                }
            }
            br.close();
        }
        if (perCount != 0){
            splitRecords.add(saveFile.getName());
            splitTestRecords.add(testFile.getName());
        }

        String indexPath = getTrainPath() + "index.txt";
        BufferedWriter bwIndex = new BufferedWriter(new FileWriter(indexPath));
        bwIndex.write("allCount:"+allTrainCount);bwIndex.newLine();
        bwIndex.write("perCount:"+preProcessProps.getProp("PER_FILE_COUNT"));bwIndex.newLine();
        for (int i=0; i<splitRecords.size(); i++){
            bwIndex.write(i+":"+splitRecords.get(i));
            bwIndex.newLine();
        }

        String indexTestPath = getTestPath() + "index.txt";
        BufferedWriter bwTestIndex = new BufferedWriter(new FileWriter(indexTestPath));
        bwTestIndex.write("allCount:"+allTestCount);bwTestIndex.newLine();
        bwTestIndex.write("perCount:"+preProcessProps.getProp("PER_FILE_COUNT"));bwTestIndex.newLine();
        for (int i=0; i<splitTestRecords.size(); i++){
            bwTestIndex.write(i+":"+splitTestRecords.get(i));
            bwTestIndex.newLine();
        }

        if (bw!=null){
            bw.close();
        }
        if (bwTest!=null){
            bwTest.close();
        }
        if (bwIndex!=null){
            bwIndex.close();
        }
        if (bwTestIndex!=null){
            bwTestIndex.close();
        }

        // delete tmp
        for (File file: files){
            file.delete();
        }
    }

    /**
     * the method is to read the tmp feature files, then split them to predict data
     * @param files the tmp predict feature files
     * @throws IOException
     */
    private void splitPredictData(List<File> files) throws IOException {
        System.out.println("#split predict data#");
        long allCount = 0;
        long perCount = 0;
        int fileCount = 0;
        List<String> splitRecords = new ArrayList<String>();

        File saveFile = getPredictSaveFile(fileCount);
        BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile));

        for (File file: files){
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line=br.readLine())!=null){
                if (!line.isEmpty()){
                    bw.write(line.trim());
                    bw.newLine();
                    perCount++;
                    if (perCount == Long.parseLong(preProcessProps.getProp("PER_FILE_COUNT"))){
                        splitRecords.add(saveFile.getName());

                        bw.close();
                        fileCount++;
                        saveFile = getPredictSaveFile(fileCount);
                        bw = new BufferedWriter(new FileWriter(saveFile));
                        perCount = 0;
                    }
                    allCount++;
                }
            }
            br.close();
        }
        if (perCount != 0){
            splitRecords.add(saveFile.getName());
        }

        String indexPath = getPredictPath() + "index.txt";
        BufferedWriter bwIndex = new BufferedWriter(new FileWriter(indexPath));
        bwIndex.write("allCount:"+allCount);bwIndex.newLine();
        bwIndex.write("perCount:"+preProcessProps.getProp("PER_FILE_COUNT"));bwIndex.newLine();
        for (int i=0; i<splitRecords.size(); i++){
            bwIndex.write(i+":"+splitRecords.get(i));
            bwIndex.newLine();
        }

        if (bw!=null){
            bw.close();
        }
        if (bwIndex!=null){
            bwIndex.close();
        }

        // delete tmp
        for (File file: files){
            file.delete();
        }
    }

    /**
     * to get the save file of train or test final feature data
     * @param fileCount the file index
     * @param isTrain true represents Train data; false represents Test data
     * @return the save file of train or test final feature data
     */
    private File getTrainAndTestSaveFile(int fileCount, boolean isTrain){
        if (isTrain){
            return new File(getTrainPath() + fileCount + ".txt");
        }else {
            return new File(getTestPath() + fileCount + ".txt");
        }
    }

    /**
     * to get the save file of final predict feature data
     * @param fileCount the file index
     * @return the save file of final predict feature data
     */
    private File getPredictSaveFile(int fileCount){
        return new File(getPredictPath() + fileCount + ".txt");
    }

}
