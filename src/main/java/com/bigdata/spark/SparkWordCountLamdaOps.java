package com.bigdata.spark;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;

/**
 * 基于jdk1.8 lamda表达式的spark作业的开发
 * */
public class SparkWordCountLamdaOps {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName(SparkWordCountLamdaOps.class.getSimpleName()).setMaster("local");
        JavaSparkContext jc = new JavaSparkContext(conf);
        JavaRDD<String> linesRDD = jc.textFile("F:\\data\\5.txt");

        JavaRDD<String> flatMapRDD = linesRDD.flatMap(line -> {
            return Arrays.asList(line.split(" "));
        });

        JavaPairRDD<String, Integer> mapToPairRDD = flatMapRDD.mapToPair(word -> {
            return new Tuple2<String, Integer>(word, 1);
        });

        JavaPairRDD<String, Integer> reduceByKeyRDD = mapToPairRDD.reduceByKey((v1, v2) -> {
            return v1 + v2;
        });

        reduceByKeyRDD.foreach(t -> {
            System.out.println('(' + t._1 + " " + t._2 + ')');
        });

        jc.close();
    }
}
