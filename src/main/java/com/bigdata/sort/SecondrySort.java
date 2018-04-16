package com.bigdata.sort;

import com.bigdata.bean.Secondry;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

public class SecondrySort {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local").setAppName(SecondrySort.class.getSimpleName());
        JavaSparkContext jc = new JavaSparkContext(conf);
        JavaRDD<String> linesWord = jc.textFile("F:\\data\\5.txt");

        JavaPairRDD<Secondry,String> pairs = linesWord.mapToPair(new PairFunction<String, Secondry, String>() {
            @Override
            public Tuple2<Secondry, String> call(String s) throws Exception {
                String [] linesSplited = s.split(" ");
                Secondry key = new Secondry(linesSplited[0],Integer.valueOf(linesSplited[1]));

                return new Tuple2<Secondry, String>(key,s);
            }
        });

        JavaPairRDD<Secondry,String> sortedpairs =  pairs.sortByKey(false);
        JavaRDD<String> sortedListLines = sortedpairs.map(new Function<Tuple2<Secondry, String>, String>() {
            @Override
            public String call(Tuple2<Secondry, String> v1) throws Exception {
                return v1._2;
            }
        });
        sortedListLines.foreach(new VoidFunction<String>() {
            @Override
            public void call(String s) throws Exception {
                System.out.println(s);
            }
        });


        jc.close();

    }
}
