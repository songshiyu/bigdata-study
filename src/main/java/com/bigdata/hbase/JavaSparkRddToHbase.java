package com.bigdata.hbase;

import com.bigdata.bean.Stu;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;

public class JavaSparkRddToHbase {
    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName(JavaSparkRddToHbase.class.getSimpleName());
        sparkConf.setMaster("local");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);

        String tableName = "Stu";
        Configuration conf = HBaseConfiguration.create();
//        conf.set("hbase.zookeeper.property.clientPort","2181");
//        conf.set("hbase.zookeeper.quorum","songshiyu001,songshiyu002,songshiyu003");
        conf.set(TableInputFormat.INPUT_TABLE,tableName);
        createTable(conf,tableName);

        Broadcast<String> broadcastTableName = jsc.broadcast(tableName);
        SQLContext sqlContext = new SQLContext(jsc);

        JavaRDD<String> linesRDD = jsc.textFile("");
        JavaRDD<Stu> stuRDD = linesRDD.map(new Function<String, Stu>() {
            public Stu call(String line) throws Exception {
                String[] parts = line.split("\\|");
                Stu stu = new Stu();
                stu.setName(parts[0]);
                stu.setGrade(parts[1]);
                stu.setMath(parts[2]);
                stu.setArt(parts[3]);
                return stu;
            }
        });
    }

    /**
     * 创建表
     * */
    private static void createTable(Configuration conf, String tableName) {

    }
}
