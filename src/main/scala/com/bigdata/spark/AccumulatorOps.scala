package com.bigdata.spark

import org.apache.spark.SparkContext


/**
  * 共享变量之累加器：Accumulator
  *     操作：使用val a = sparkContext.accumulator(initValue)
  *               a.add()
  *  累加器的一个问题：
  *      1.如果执行累加器的时候，程序RDD被多次调用，则累加器的数据会重复累加
  *           要想解决这个问题，将改RDD持久化
  *      2.累加器的执行必须有
  * */
object AccumulatorOps {
  def main(args: Array[String]): Unit = {
    val sc = SparkContext
  }
}
