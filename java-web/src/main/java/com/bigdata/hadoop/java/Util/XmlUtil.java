package com.bigdata.hadoop.java.Util;

import org.dom4j.DocumentHelper;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by songshiyu on 2018/8/13.
 */
public class XmlUtil {


    /**
     *  生成xml格式的文件
     * */
    public static Document createXml(){
        Element docRoot = new Element("xml");
        docRoot.setAttribute("version","1.0");
        docRoot.setAttribute("encoding","UTF-8");
        Element edits = new Element("EDITS");
        edits.addContent(new Element("EDITS_VERSION").addContent("-60"));
        Element firstRecord = new Element("RECORD");
        firstRecord.addContent(new Element("OPCODE").addContent("OP_START_LOG_SEGMENT"));
        Element data = new Element("DATA");
        data.addContent(new Element("TXID").addContent("1221873876"));
        firstRecord.addContent(data);
        edits.addContent(firstRecord);
        docRoot.addContent(edits);
        Document rssDoc = new Document(docRoot);

        Element record = null;
        Element recordData = null;
        Long atime = 1533696528000l;
        for (Long i = 1221873877l; i < 1223111793l; i++){
            atime = atime + 3000l;
            record = new Element("RECORD");
            record.addContent(new Element("OPCODE").addContent("OP_TIMES"));
            recordData = new Element("DATA");
            recordData.addContent(new Element("TXID").addContent(String.valueOf(i)));
            recordData.addContent(new Element("LENGTH").addContent(String.valueOf(0)));
            recordData.addContent(new Element("PATH").addContent(String.valueOf("/user/hive/warehouse/rtb.db/log_req/exchange_id=114/dt=20180505/hr=18/rtb-back04.domob.cn.2833.77.done")));
            recordData.addContent(new Element("MTIME").addContent(String.valueOf("-1")));
            recordData.addContent(new Element("ATIME").addContent(String.valueOf(atime)));
            record.addContent(recordData);
            edits.addContent(record);
        }
        return rssDoc;
    }

    /**
     *  读取某个xml文件,匹配其中的某个字段,并将其生成到另一个xml中
     */
    public static org.dom4j.Document parseXml(String fileName){
        File file = new File(fileName);
        //用于读取xml
        SAXReader saxReader = new SAXReader();
        org.dom4j.Document document = null;
        org.dom4j.Document newDocument = null;
        List<org.dom4j.Element> list = new LinkedList<org.dom4j.Element>();
        try {
            //创建一个新的xml
            newDocument = DocumentHelper.createDocument();
            //设置新xml的根节点
            org.dom4j.Element editsElement = newDocument.addElement("EDITS");
            //读取存在的xml到内存中
            document = saxReader.read(file);
            //得到原xml的根节点
            org.dom4j.Element rootElement = document.getRootElement();
            for (Iterator r = rootElement.elementIterator(); r.hasNext();){
                org.dom4j.Element newRecord = (org.dom4j.Element) r.next();
                Iterator elementIterator = newRecord.elementIterator();
                while (elementIterator.hasNext()){
                    org.dom4j.Element next = (org.dom4j.Element)elementIterator.next();
                    if (next.elementText("PATH") != null && next.elementText("PATH").contains("hbase")){
                        list.add(newRecord);
                    }
                }
            }
            //向新xml的根节点中添加新的字段属性以及文本
            editsElement.setContent(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newDocument;
    }

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
