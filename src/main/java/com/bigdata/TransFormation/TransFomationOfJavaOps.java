package com.bigdata.TransFormation;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 基于java版本的transfomation操作
 * */
public class TransFomationOfJavaOps {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local[2]").setAppName(TransFomationOfJavaOps.class.getSimpleName());
        JavaSparkContext jsc = new JavaSparkContext(conf);

//        transfomation1(jsc);
//        transfomation2(jsc);
//        transfomation3(jsc);
        transfomationOps4(jsc);
        jsc.close();
    }

    //1.map 将集合中所有元素乘以7
    private static void transfomation1(JavaSparkContext jsc){
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7);
        JavaRDD<Integer> listRDD = jsc.parallelize(list);
        JavaRDD<Integer> mapRDD = listRDD.map(new Function<Integer, Integer>() {
            @Override
            public Integer call(Integer num) throws Exception {
                return num * 7;
            }
        });
        mapRDD.foreach(new VoidFunction<Integer>() {
            @Override
            public void call(Integer num) throws Exception {
                System.out.println("num ====>" + num);
            }
        });
    }

    //2.filter 过滤掉集合中所有的奇数
    private static void transfomation2(JavaSparkContext jsc){
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7);
        JavaRDD<Integer> listRDD = jsc.parallelize(list);

        JavaRDD<Integer> filterRdd = listRDD.filter(new Function<Integer, Boolean>() {
            @Override
            public Boolean call(Integer num) throws Exception {
                return num % 2 == 0;
            }
        });
        filterRdd.foreach(new VoidFunction<Integer>() {
            @Override
            public void call(Integer num) throws Exception {
                System.out.println("num ===>" + num);
            }
        });
    }
    /**
     * 3.flatMap 拆分字符串 ==》 类似于map，但是每输入一个元素，会被映射为0到多个输出元素
     * （因此，function函数的返回值是一个Seq，而不是一个单一元素）
     * */
    private static void transfomation3(JavaSparkContext jsc){
        List<String> list = Arrays.asList("hello you","hello me","hello she","hello it","hello all of you");
        JavaRDD<String> listRDD = jsc.parallelize(list);

        JavaRDD<String> words = listRDD.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterable<String> call(String line) throws Exception {
                System.out.println("拆分前的元素为：" + line);
                return Arrays.asList(line.split(" "));
            }
        });
        words.foreach(new VoidFunction<String>() {
            @Override
            public void call(String word) throws Exception {
                System.out.println(word);
            }
        });
    }

    /**4.sample 根据给定的随机种子seed，随机抽样出数量为frac的数据
     *      sample(withReplacement,frac,seed);根据给定的随机种子seed，随机抽样出数量为frac的数据
     *      这三个参数分别为：
     *          withReplacement：有放回的采集和无放回的抽样方式。
     *          frac：采集样本数据占总体数据的比例（0.0~~1.0）
     *              返回的结果不是严格的按照这个比例，会有上下浮动
     *          seed：随机数种子
     *          sample多在spark性能优化，比如数据倾斜的时候常用
     * */
    private static void transfomationOps4(JavaSparkContext jsc){
        List<Integer> list = new ArrayList<Integer>();
        for (int i =0; i < 10000; i++){
            list.add(i);
        }
        JavaRDD<Integer> listRDD = jsc.parallelize(list);
        JavaRDD<Integer> sampleRDD = listRDD.sample(true, 0.01);
        System.out.println("随机数的个数为：" + sampleRDD.count());
        sampleRDD.foreach(new VoidFunction<Integer>() {
            @Override
            public void call(Integer i) throws Exception {
                System.out.println(i);
            }
        });
    }


}
