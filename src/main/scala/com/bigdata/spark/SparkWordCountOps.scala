package com.bigdata.spark

import org.apache.spark.{SparkConf, SparkContext}

object SparkWordCountOps {

  def main(args: Array[String]): Unit = {

      val conf = new SparkConf()
        .setAppName("SparkWordCountOps")
        .setMaster("spark://songshiyu001:7077")
      val sc = new SparkContext(conf)
      val linesRDD = sc.textFile("hdfs://ns1/input/mr/hello")
    //第一种写法：普通写法
//      val wordsRDD = linesRDD.flatMap(line => line.split(" "))
//      val mapRDD =  wordsRDD.map(word => (word,1))
//      val retRDD = mapRDD.reduceByKey((v1,v2) => v1+v2)
//      retRDD.foreach(t => println(t._1 + " " + t._2))

    //第二种写法：占位符的形式
//    val wordsRDD = linesRDD.flatMap(_.split(" "))
//    val mapRDD =  wordsRDD.map((_,1))
//    val retRDD = mapRDD.reduceByKey(_+_)
//    retRDD.foreach(println)

    //第三种写法：简化写法
    linesRDD.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).foreach(println)
    sc.stop()
  }

}
