package com.bigdata.sort;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.List;

public class Top7OpsOfJava {

    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setMaster("local").setAppName(Top7OpsOfJava.class.getSimpleName());
        JavaSparkContext jc = new JavaSparkContext(conf);

        //按行读取文件，将文件按行读取到RDD内
        JavaRDD<String> linesWord = jc.textFile("F:\\data\\5.txt");
        //将每一行的数据按空格分割，组成key-value的格式
        JavaPairRDD<Integer, String> pairsRDD = linesWord.mapToPair(new PairFunction<String, Integer, String>() {
            public Tuple2<Integer, String> call(String line) throws Exception {
                return new Tuple2<Integer, String>(Integer.valueOf(line.split(" ")[1]),line.split(" ")[0]);
            }
        });
        //根据key由大到小进行排序
        JavaPairRDD<Integer, String> sortPairsRDD = pairsRDD.sortByKey(false);
        //取得top7的list集合
        List<Tuple2<Integer, String>> wordlist = sortPairsRDD.take(7);
        //遍历集合并打印
        for (Tuple2 word:wordlist){
            System.out.println(word._2 +" "+word._1);
        }
        jc.close();
    }
}
