package com.bigdata.TransFormation


import org.apache.spark.{SparkConf, SparkContext}

import scala.reflect.ClassTag


/**
  * 基于scala版本的transfomation操作
  **/
object TransFomationOps {

  def main(args: Array[String]): Unit = {
      val conf = new SparkConf().setMaster("local[2]").setAppName("TransFomationOps")
      val sc = new SparkContext(conf)
//      transfomationOps1(sc)
//      transfomationOps2(sc)
//    transfomationOps3(sc)
//    transfomationOps4(sc)
//    transfomationOps5(sc)
//    transfomationOps6(sc)

//    transfomationOps8(sc)
    transfomationOps9(sc)
      sc.stop()
  }

  //1.map 将集合中所有元素乘以7
  def transfomationOps1(sc:SparkContext):Unit={
      val list = List(1,2,3,4,5,6,7)
      val listRDD = sc.parallelize(list)
      val mapRDD = listRDD.map(_ * 7)
      mapRDD.foreach(println)
  }

  //2.filter 过滤掉集合中所有的奇数
  def transfomationOps2(sc:SparkContext):Unit={
    val list = List(1,2,3,4,5,6,7)
    val listRDD = sc.parallelize(list)
    val filterRDD = listRDD.filter(_ % 2 ==0)
    filterRDD.foreach(println)
  }

  /**
    * 3.flatMap 拆分字符串 ==》 类似于map，但是每输入一个元素，会被映射为0到多个输出元素
    * （因此，function函数的返回值是一个Seq，而不是一个单一元素）
    **/
  def transfomationOps3(sc:SparkContext): Unit ={
    val list = List("hello you", "hello me", "hello she", "hello it", "hello all of you")
    val listRDD = sc.parallelize(list)

    val flatRDD = listRDD.flatMap(_.split(" "))


    flatRDD.foreach(println)
  }

  /** 4.sample 根据给定的随机种子seed，随机抽样出数量为frac的数据
    * sample(withReplacement,frac,seed);根据给定的随机种子seed，随机抽样出数量为frac的数据
    * 这三个参数分别为：
    * withReplacement：有放回的采集和无放回的抽样方式。
    * frac：采集样本数据占总体数据的比例（0.0~~1.0）
    * 返回的结果不是严格的按照这个比例，会有上下浮动
    * seed：随机数种子
    * sample多在spark性能优化，比如数据倾斜的时候常用
    * */
  def transfomationOps4(sc:SparkContext): Unit ={
    val range = 1 to(10000)
    val listRDD = sc.parallelize(range)
    var result = listRDD.sample(true,0.01,10L)
    println("产生的随机数个数为：" + result.count())
    result.foreach(println);
  }

  /**
    * 5.union 返回一个新的数据集，由原数据集和参数联合而成
    *     union(otherDataset):返回一个新的数据集，有原数据集和参数联合而成
    * */
  def transfomationOps5(sc:SparkContext): Unit ={

    val list1 = 1 to 5
    val list2 = 4 to 10
    val list1RDD = sc.parallelize(list1)
    val list2RDD = sc.parallelize(list2)
    val unionRDD = list1RDD.union(list2RDD)
    unionRDD.foreach(println)
  }

  /**
    * 6.groupByKey: 对数组进行group by key操作
    *   在一个有<K,V>对组成的数据集上调用，返回一个(K,Iterable[K])对的数据集。
    *     注意：默认情况下，使用8个并行任务进行分组，你可以传入numTask可选参数，根据数据量设置不同数目的Task
    * */
  def transfomationOps6(sc:SparkContext): Unit ={
    val list = List("hello you", "hello me", "hello she", "hello it", "hello all of you")
    val listRDD = sc.parallelize(list)
 //   listRDD.flatMap(_.split(" ")).map((_,1)).reduceByKey(_ + _).foreach(println)
    val flatRDD = listRDD.flatMap(_.split(" ")).map((_,1))
    val groupRDD = flatRDD.groupByKey()
    groupRDD.foreach(t =>{
      println(t._1 + " " + t._2.mkString("[",",","]"))
    })
    val lastRDD = groupRDD.map(t=>{
      val key = t._1
      val value = t._2.sum
      (key,value)
    })
    lastRDD.foreach(println)
  }

  /**
    * 7.reduceByKey
    * */

  /**
    * 8.join :打印关联的组合信息
    *   join(otherDataset,{numTasks});
    *   在类型为(K,V)和(K,W)类型的数据集上调用，返回一个(K,(V,W))对，每个key中的所有元素都在一起的数据集
    *
    * */
  def transfomationOps8(sc:SparkContext): Unit ={

    //模拟数据库中的两张表
    val deptList = List("1,研发","2,HR")
    val empList = List("1,张三,13,1","2,李四,14,2","3,王五,15,2")

    val deptListRDD = sc.parallelize(deptList)
    val empListRDD = sc.parallelize(empList)

    //<dept_id,dept_name>
    val deptPairRDD = deptListRDD.map(line => {
      val splits =  line.split(",")
      (splits(0).trim.toInt,splits(1).trim)
    }).filter(t =>t._2 == "研发")   //只要研发的人员
     //<dept_id,emp_id|emp_name|emp_age>
    val empPairsRDD = empListRDD.map( line =>{
      val splits = line.split(",")
      (splits(3).trim.toInt,splits(0) + "|" + splits(1) + "|" + splits(2))
    })
    //<dept_id,(dept_name,emp_id|emp_name|emp_age)>
    val joinRDD = deptPairRDD.join(empPairsRDD)
    joinRDD.foreach(println)
  }

  /**
    * sortByKey:将学生身高进行排序，如果身高相同，按照年龄排序
    *   spark中的排序是基于key的排序
    *   如果没有key value这种结构，也没有关系，同样可以进行排序
    * */
  def transfomationOps9(sc:SparkContext): Unit ={
    val persons = List("1 宋时雨 18 180","2 刘伟明 1 170","3 王博 30 130","4 赵军 23 176")
    val personRDD = sc.parallelize(persons)

    println("=========================sortBy============================================")
    val retRDD = personRDD.sortBy(line =>{
      val splits = line.split(" ")
      (splits(0).trim,line)
    },true,1)(new Ordering[(String,String)]() {
      override def compare(x: (String,String), y: (String,String)): Int = {
          val xSplits = x._2.split(" ")
          val ySplits = y._2.split(" ")
          val xHeight = xSplits(3).trim.toInt
          val yHeight = ySplits(3).trim.toInt
          var ret = xHeight - yHeight
          if(ret == 0){
            val xAge = xSplits(2).trim.toInt
            val yAge = ySplits(2).trim.toInt
            ret = xAge - yAge
          }
        ret
      }
    },ClassTag.Object.asInstanceOf[ClassTag[(String,String )]])
    retRDD.foreach(println)
    println("=========================sortBy============================================")
    println("=========================sortByKey============================================")
     val heightRDD = personRDD.map(line =>{
        val splits = line.split(" ")
       (splits(3),line)
     })
    val sortByKeyRDD = heightRDD.sortByKey(false,1)
    sortByKeyRDD.foreach(println)
    println("=========================sortByKey============================================")
  }
}
