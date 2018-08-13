package com.bigdata.hadoop.java.IO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by songshiyu on 2018/8/10.
 *
 */
public class IOUtil {
    private static Logger log = LoggerFactory.getLogger(IOUtil.class);

    public static void main(String[] args) {
        String fileName = "D:\\test.txt";
        String outPutPath = "D:\\IO\\";
        String outPutName1 = "result_1.txt";
        String outPutName2 = "result_2.txt";
        String outPutName3 = "result_3.txt";
        String outPutName4 = "result_4.txt";
        //saveFile(fileName,outPutPath,outPutName1);
        //testBufferedWriter(fileName,outPutPath,outPutName2);
        //testCharSaveFile(fileName,outPutPath,outPutName3);
        testBianMa(fileName,outPutPath,outPutName4);
    }

    /**
     *  1.使用字节流读取文件,并保存在指定目录
     * */
    public static void saveFile(String fileName,String outPutPath,String outPutName){
        FileInputStream fis = null;
        FileOutputStream fos = null;

        File inputFile = new File(fileName);
        if (!inputFile.exists()){
            log.warn("输入的文件不存在,请确认是否输入有误");
        }

        File outputFile = new File(outPutPath);
        if (!outputFile.exists()){
            log.warn("输出路径不存在,开始创建输出目录...");
            outputFile.mkdirs();
            log.warn("输出目录创建成功");
        }
        try {
            fis = new FileInputStream(fileName);
            fos = new FileOutputStream(outPutPath + outPutName);
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = fis.read(bytes)) != -1){
                fos.write(bytes,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fis.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *    2.使用BufferedWriter缓冲区
     * */
    public static void testBufferedWriter(String fileName,String outPutPath,String outPutName){
        FileWriter fileWriter = null;
        FileInputStream fis = null;
        BufferedReader br = null;
        BufferedWriter bw = null;
        File inputFile = new File(fileName);
        if (!inputFile.exists()){
            log.warn("输入的文件不存在,请确认是否输入有误");
        }
        File outputFile = new File(outPutPath);
        if (!outputFile.exists()){
            log.warn("输出路径不存在,开始创建输出目录...");
            outputFile.mkdirs();
            log.warn("输出目录创建成功");
        }
        try {
            fis = new FileInputStream(fileName);
            br = new BufferedReader(new InputStreamReader(fis));
            //创建一个字符写入流对象
            fileWriter = new FileWriter(outPutPath + outPutName);

            //将需要被提高效率的流作为参数传递给缓冲区中的构造函数
            bw = new BufferedWriter(fileWriter);

            String str = null;
            while ((str = br.readLine()) != null){
                bw.write(str);
                bw.newLine();
                bw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bw.flush();         //刷新缓冲区
                bw.close();         //关闭缓冲区,但是必须要先刷新
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  3.使用字符数组读取文件
     * */
    public static void testCharSaveFile(String fileName,String outPutPath,String outPutName){
        FileReader fileReader = null;
        BufferedWriter br = null;
        File inputFile = new File(fileName);
        if (!inputFile.exists()){
            log.warn("输入的文件不存在,请确认是否输入有误");
        }
        File outputFile = new File(outPutPath);
        if (!outputFile.exists()){
            log.warn("输出路径不存在,开始创建输出目录...");
            outputFile.mkdirs();
            log.warn("输出目录创建成功");
        }

        try {
            fileReader = new FileReader(fileName);
            FileWriter fileWriter = new FileWriter(outPutPath + outPutName);
            //FileWriter fileWriter = new FileWriter(outPutPath + outPutName,true);  //结尾加个true,表示不覆盖原有内容,并在已有内容末尾进行追加
            br = new BufferedWriter(fileWriter);
            char[] chars = new char[10];
            int num = 0;
            while ((num = fileReader.read(chars)) != -1){
                br.write(chars,0,num);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                br.close();
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 若需要指字符编码 使用
     *      new OutputStreamWriter(new FileOutputStream("demo.txt"),"UTF-8")
     * */
    public static void testBianMa(String fileName,String outPutPath,String outPutName){
        FileInputStream fis = null;
        BufferedReader br = null;
        BufferedWriter bw = null;
        File inputFile = new File(fileName);
        if (!inputFile.exists()){
            log.warn("输入的文件不存在,请确认是否输入有误");
        }
        File outputFile = new File(outPutPath);
        if (!outputFile.exists()){
            log.warn("输出路径不存在,开始创建输出目录...");
            outputFile.mkdirs();
            log.warn("输出目录创建成功");
        }

        try {
            fis = new FileInputStream(fileName);
            br = new BufferedReader(new InputStreamReader(fis));
            String str = null;
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outPutPath + outPutName),"UTF-8"));
            while ((str = br.readLine()) != null){
                bw.write(str);
                bw.newLine();
                bw.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                fis.close();
                br.close();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
