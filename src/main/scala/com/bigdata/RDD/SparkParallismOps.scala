package com.bigdata.RDD

import org.apache.spark.{SparkConf, SparkContext}

/**
  * SparkRDD的创建之并行化集合方式
  * */
object  SparkParallismOps {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("SparkParallismOps")
    val sc = new SparkContext(conf)

    val list = List(1,2,3,4,5,6,7,8,9)
    val listRDD = sc.parallelize(list)
    listRDD.foreach(println)
    println()
    listRDD.map(_ * 10).foreach(println)
  }


}
