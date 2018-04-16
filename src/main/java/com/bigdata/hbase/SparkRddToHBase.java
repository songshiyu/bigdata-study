package com.bigdata.hbase;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.util.Arrays;

public class SparkRddToHBase {

    /**
     * 基于java的Spark的运行案例操作
     * SparkContext
     *      1.构建SparkContext
     *          javaSparkContext
     *          hiveSparkContext
     *          SQLcontext
     *      2.加载数据源，形成RDD
     *      3.对RDD执行各种transformation操作
     *      4.对RDD执行action操作进行触发
     * */
    public static void main(String[] args) {

        SparkConf conf = new SparkConf();
        //为当前spark application设置appName
        conf.setAppName(SparkRddToHBase.class.getSimpleName());
        //为当前spark application设置运行方式
        /**
         * setMaster可以添加的参数：
         *      local****
         *             local
         *                  运行spark作业的过程中只开启一个线程
         *             local[M]
         *                  运行spark作业的过程中只开启N个cpu的核
         *             local[*]
         *                  运行spark作业的过程中根据计算机的能力动态的分配相应的cpu的核数
         *             local[M,N]
         *                   就比local[M]多了第二个参数N,在创建sparkContext过程中，如果失败还会有N次重新创建的机会，而上面三种没有
         *      spark://master_host_ip:7077
         *              将spark程序部署到spark standlone的集群中去运行
         *      yarn
         *          是依赖于yarn资源调度集群
         *           yarn-client
         *                  sparkContext的创建在本地，spark运行程序在yarn集群中
         *           yarn-cluster
         *                  sparkContext的创建和spark程序的运行都在yarn集群中
         *      mesos
         *          不是我们主要关注的
         * */
        conf.setMaster("local");
        /**
         * 第一步：构建SparkContext，其依赖于SparkConf
         * */
        JavaSparkContext jsc = new JavaSparkContext(conf);
        /**
         * 第二步：通过SparkContext加载外部数据源
         * 本地文件：E:/data/spark/core/hello.txt
         * 形成弹性式分布式数据集
         * */
        JavaRDD<String> linesRDD = jsc.textFile("F:/data/spark/core/hello.txt");

        /**
         * 第三步：
         * 是将：hello you ===》 Arrays.asList(line.split(" ")) ====》 hello
         * */
        JavaRDD<String> wordsRDD = linesRDD.flatMap(new FlatMapFunction<String, String>() {
            public Iterable<String> call(String line) throws Exception {
                return Arrays.asList(line.split(" "));
            }
        });
        /**
         * hello   ====> <hello,1>
         * you           <you,1>
         * */
        JavaPairRDD<String, Integer> pairRDD = wordsRDD.mapToPair(new PairFunction<String, String, Integer>() {
            public Tuple2<String, Integer> call(String word) throws Exception {
                return new Tuple2<String, Integer>(word, 1);
            }
        });

        /**
         * 将分散的key-value 键值对进行聚合操作，按照key进行分组，然后求和，实际上就是执行reduce操作
         * */
        JavaPairRDD<String, Integer> retRDD = pairRDD.reduceByKey(new Function2<Integer, Integer, Integer>() {
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1 + v2;
            }
        });

        //step 4：extcute action
        retRDD.foreach(new VoidFunction<Tuple2<String, Integer>>() {
            public void call(Tuple2<String, Integer> kv) throws Exception {
                System.out.println(kv._1 + "----" + kv._2);
            }
        });
        //step 5：关闭资源 close resources
        jsc.close();
    }
}
