package com.bigdata.hbase;

import com.twitter.chill.Base64;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.protobuf.ProtobufUtil;
import org.apache.hadoop.hbase.protobuf.generated.ClientProtos;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import scala.Tuple2;

import java.util.Iterator;

public class HbaseTest {
    public static void main(String[] args) {

        SparkConf conf1 = new SparkConf();
        conf1.setAppName(HbaseTest.class.getSimpleName());
        conf1.setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf1);

        Configuration conf = HBaseConfiguration.create();
        Scan scan = new Scan();
        scan.addFamily(Bytes.toBytes("course"));//column family

        scan.addColumn(Bytes.toBytes("course"), Bytes.toBytes("course"));

        try {

            String tableName = "stu";

            conf.set(TableInputFormat.INPUT_TABLE, tableName);

            ClientProtos.Scan proto = ProtobufUtil.toScan(scan);

            String ScanToString = Base64.encodeBytes(proto.toByteArray());

            conf.set(TableInputFormat.SCAN, ScanToString);
            JavaPairRDD<ImmutableBytesWritable, Result> myRDD = sc.newAPIHadoopRDD(conf, TableInputFormat.class,
                    ImmutableBytesWritable.class, Result.class);
            System.out.println(myRDD.count());

            myRDD.mapPartitions(new FlatMapFunction<Iterator<Tuple2<ImmutableBytesWritable,Result>>, Object>() {
                public Iterable<Object> call(Iterator<Tuple2<ImmutableBytesWritable, Result>> tuple2Iterator) throws Exception {
                    return null;
                }
            });

            JavaRDD<Object> javaRDD = myRDD.map(new Function<Tuple2<ImmutableBytesWritable, Result>, Object>() {
                public Object call(Tuple2<ImmutableBytesWritable, Result> v1) throws Exception {
                    byte[] course = v1._2().getValue(Bytes.toBytes("course"), Bytes.toBytes("math"));
                    if (course != null) {
                        return new Tuple2<Integer, Integer>(Bytes.toInt(course), 1);
                    }
                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        sc.close();


    }

}