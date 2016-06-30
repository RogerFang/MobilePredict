package edu.whu.irlab.process.service.impl;

import edu.whu.irlab.process.model.PreProcessProps;
import edu.whu.irlab.process.service.ExSortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

/**
 * Created by Roger on 2016/5/17.
 */
@Service
public class ExSortServiceImpl implements ExSortService{

    @Autowired
    private PreProcessProps preProcessProps;

    // split line count
    // private int LINE_COUNT = Integer.parseInt(preProcessProps.getProp("LINE_COUNT"));

    // tmp file dir
    // private String TMP_DIR = preProcessProps.getProp("TMP_DIR")+File.separator;


    // public ExSortService(){
    //     File tmpFile = new File(preProcessProps.getProp("LINE_COUNT"));
    //     if (!tmpFile.exists()){
    //         tmpFile.mkdirs();
    //     }
    // }

    /**
     * 多路归并
     * @param file
     * @throws IOException
     */
    @Override
    public File mSort(File file) {
        File tmpFile = new File(preProcessProps.getProp("TMP_DIR"));
        if (!tmpFile.exists()){
            tmpFile.mkdirs();
        }

        System.out.println(file.getName());
        ArrayList<File> files = null;
        try {
            files = split(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File rtnFile = null;
        try {
            rtnFile = multipleMerge(files);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rtnFile;
    }

    /**
     * Splits the original file into a number of sub files.
     */
    private ArrayList<File> split(File file) throws IOException {
        ArrayList<File> files = new ArrayList<File>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        Long lineCount = 0l;
        String line = br.readLine();
        // key=tel,value=properties
        Map<String, String> subData = new TreeMap<String, String>();
        while (line != null){
            String tel = line.trim().split(",")[0];
            if (tel.equals("手机号码")){
                line = br.readLine();
                continue;
            }
            subData.put(tel, line.trim());
            lineCount++;
            line = br.readLine();
            if (lineCount == Long.parseLong(preProcessProps.getProp("LINE_COUNT")) || line == null){
                String subpath = preProcessProps.getProp("TMP_DIR") + File.separator + "sub_"+ UUID.randomUUID() +".tmp";
                System.out.println("save tmpfile:"+lineCount+"\t"+subpath);
                File subFile = new File(subpath);
                BufferedWriter bw = new BufferedWriter(new FileWriter(subFile));
                for (String properties: subData.values()){
                    bw.write(properties.trim());
                    bw.newLine();
                }
                bw.close();
                files.add(subFile);
                lineCount = 0l;
                subData.clear();
            }
        }

        br.close();
        return files;
    }

    /**
     * 多路归并
     * @param list
     * @throws IOException
     */
    private File multipleMerge(ArrayList<File> list) throws IOException {

        int fileSize = list.size();

        if(fileSize == 1){
            return list.get(0);
        }

        ArrayList<BufferedReader> brList = new ArrayList<BufferedReader>();
        for (int i=0; i<fileSize; i++){
            brList.add(new BufferedReader(new FileReader(list.get(i))));
        }

        File mergeFile = new File(preProcessProps.getProp("TMP_DIR") + File.separator + "mergeFile_"+UUID.randomUUID()+".txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(mergeFile));
        System.out.println(mergeFile.getName());
        // 比较数组
        Map<Integer, String> cmpTelMap = new HashMap<Integer, String>();
        Map<Integer, String> propertiesMap = new HashMap<Integer, String>();
        for (int i=0; i<fileSize; i++){
            String line = brList.get(i).readLine();
            cmpTelMap.put(i, line.trim().split(",")[0]);
            propertiesMap.put(i, line.trim());
        }
        int count = fileSize;
        while (count > 1){
            int minIndex = getMinIndex(cmpTelMap);
            bw.write(propertiesMap.get(minIndex));
            bw.newLine();
            String nextLine = brList.get(minIndex).readLine();
            if (nextLine==null){
                --count;
                // cmpTelMap.remove(minIndex);
                // propertiesMap.remove(minIndex);
                // brList.get(minIndex).close();
                // brList.remove(minIndex);
            }else {
                cmpTelMap.put(minIndex, nextLine.trim().split(",")[0]);
                propertiesMap.put(minIndex, nextLine.trim());
            }
        }

        int lastFileIndex = (Integer)cmpTelMap.keySet().toArray()[0];
        bw.write(propertiesMap.get(lastFileIndex));
        bw.newLine();
        String lastFileLine = null;
        while ((lastFileLine=brList.get(lastFileIndex).readLine())!=null){
            bw.write(lastFileLine);
            bw.newLine();
        }
        brList.get(lastFileIndex).close();
        bw.close();

        for (BufferedReader br: brList){
            if (br!=null){
                br.close();
            }
        }

        // delete tmp
        for (File file: list){
            file.delete();
        }
        return mergeFile;
    }

    //找到数据中最小的一个
    private int getMinIndex(Map<Integer, String> cmpMap){
        int index = (Integer)cmpMap.keySet().toArray()[0];
        String min = cmpMap.get(index);
        for (Map.Entry<Integer,String> entry: cmpMap.entrySet()){
            if (entry.getValue().compareTo(min) < 0) {
                index = entry.getKey();
            }
        }
        return index;
    }
}
