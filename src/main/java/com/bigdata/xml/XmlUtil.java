package com.bigdata.xml;


import org.jdom2.Document;
import org.jdom2.Element;


/**
 * Created by songshiyu on 2018/8/9.
 *
 *  使用jdom2按照要求生成xml格式的文件
 */
public class XmlUtil {

    public static void main(String[] args) {
        Document xml = createXml();
        FileUtil.XMLOut("D:\\","result.xml",xml);
    }

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
}
