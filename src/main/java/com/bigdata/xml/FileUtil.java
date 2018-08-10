package com.bigdata.xml;


import org.dom4j.io.XMLWriter;
import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.*;

/**
 * Created by songshiyu on 2018/8/9.
 */
public class FileUtil {

    /**
     *
     *  保存xml的Document到本地  Document是org.jdom2包下的
     *
     * */
    public static void XMLOut(String filePath, String fileName, Document doc){
        Format format = Format.getPrettyFormat(); // 格式化xml（缩进等）
        format.setEncoding("UTF-8"); // 文档编码UTF-8
        XMLOutputter XMLOut = new XMLOutputter();
        XMLOut.setFormat(format);
        FileOutputStream write = null;
        try {
            if (!new File(filePath).exists()) {	// 如果文档的存放目录不存在，则创建该目录
                new File(filePath).mkdirs();
            }
            write = new FileOutputStream(filePath + fileName);
            XMLOut.output(doc, write);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (write != null) {
                try {
                    write.flush();
                    write.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     *
     *  保存xml的Document到本地  Document是org.jdom4包下的
     *
     * */
    public static boolean doc2XmlFile(org.dom4j.Document document, String filename) {
        boolean flag = true;
        try {
            Format format = Format.getPrettyFormat();

            XMLWriter writer = new XMLWriter(new OutputStreamWriter(
                    new FileOutputStream(filename), "UTF-8"));
            writer.write(document);
            writer.close();
        } catch (Exception ex) {
            flag = false;
            ex.printStackTrace();
        }
        System.out.println(flag);
        return flag;
    }
}
