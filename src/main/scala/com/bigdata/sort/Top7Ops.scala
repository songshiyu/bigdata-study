package com.bigdata.sort

import org.apache.spark.{SparkConf, SparkContext}

import scala.util.control.Breaks._

/**
  * Created by limu on 2016/9/4.
  * class1 90
    class2 56
    class1 87
    class1 76
    class2 88
    class1 95
    class1 74
    class2 87
    class2 67
    class2 77
    class1 98
    class2 96
    class1 32
    class2 33
    class2 12
    class1 14
  */
object Top7Ops {
  def main(args: Array[String]) {
    val conf = new SparkConf().setMaster("local").setAppName("Top7Ops")
    val sc = new SparkContext(conf)
    val line = sc.textFile("C:\\chinare\\files\\demosdata\\5.txt")

    val pairs = line.map(transform(_))
    val groupedPairs = pairs.groupByKey()
    val top7Score = groupedPairs.map(classScore =>{
      val top7 = Array[Int](-1,-1,-1,-1,-1,-1,-1)
      val className = classScore._1
      val scores = classScore._2
      for(score <- scores){
        //scala没有提供相同的Java中的Break语句，但是可以使用boolean类型变量，return或者Breaks的Break函数来解决
        breakable{
          for( i<- 0 until 7){
            if(top7(i) == -1){
              top7(i) = score;
              break;
            }else if(score > top7(i)){
              var j = 6
              while(j > i){
                top7(j) = top7(j - 1);
                j = j -1;

              }
              top7(i) = score
              break;
            }
          }
        }
      }

      (className,top7)
    })

    top7Score.foreach(line=>{
      println(line._1)
      for(i <- line._2){
        println(i)
      }
      println("==========================================")
    })
    sc.stop()

  }

  /**
    * 利用封装来解决业务复杂的问题
    * @param v1
    * @return
    */
  def transform(v1:String):(String,Integer)={
    val splited = v1.split(" ")
    (splited(0),splited(1).toInt)
  }

}
