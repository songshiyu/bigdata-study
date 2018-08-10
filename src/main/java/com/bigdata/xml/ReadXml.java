package com.bigdata.xml;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by songshiyu on 2018/8/10.
 *
 *  读取某个xml文件,匹配其中的某个字段,并将其生成到另一个xml中
 */
public class ReadXml {

    public static void main(String[] args) {
        Document document = parseXml("D:\\test.xml");
        FileUtil.doc2XmlFile(document,"D:\\test_new.xml");
    }

    public static Document parseXml(String fileName){
        File file = new File(fileName);
        //用于读取xml
        SAXReader saxReader = new SAXReader();
        Document document = null;
        Document newDocument = null;
        List<Element> list = new LinkedList<Element>();
        try {
            //创建一个新的xml
            newDocument = DocumentHelper.createDocument();
            //设置新xml的根节点
            Element editsElement = newDocument.addElement("EDITS");
            //读取存在的xml到内存中
            document = saxReader.read(file);
            //得到原xml的根节点
            Element rootElement = document.getRootElement();
            for (Iterator r = rootElement.elementIterator();r.hasNext();){
                Element newRecord = (Element) r.next();
                Iterator elementIterator = newRecord.elementIterator();
                while (elementIterator.hasNext()){
                    Element next = (Element)elementIterator.next();
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
}
