package com.bigdata.elasticsearch.utils;

import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.protobuf.ProtobufUtil;
import org.apache.hadoop.hbase.util.Base64;

import java.io.IOException;

/**
 * Created by songshiyu on 2018/7/20.
 */
public class ConvertUtils {

    public static String scanToString(Scan scan) {
        try {
            return Base64.encodeBytes(ProtobufUtil.toScan(scan).toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
