package com.bigdata.action

/**
  * 基于scala的spark的action算子操作
  *  1.reduce;
  *  2.collect;
  *  3.count;
  *  4.take;
  *  5.first;
  *  6.saveAsTextFile;
  *  7.countByKey
  *  8.foreach
  * */
object ActionOps {

  def main(args: Array[String]): Unit = {

  }

  /**
    * 1.reduce
    *     注意：reduceByKey是一个transformation,而reduce是一个action
    *           和scala中集合的reduce函数作用一致
    * */

  /**
    * 2.collect：
    *     是一个将各个partition分区中的数据拉倒Driver之上的action操作算子
    *     但是在执行collect操作的时候一定要慎重！！
    *     一定要注意数据拉取的量的问题，以免造成Driver的OOM异常。
    *     所以一般不进行collect，那么就是进行collect也要过滤掉大量的数据。
    * */

  /**
    * 3.count：返回值为Long
    *   计算各个partition中的数据总条数。
    * */

  /**
    * 4.take(n):获取RDD中的前N条记录
    * 5.first，其实就是take(1)
    * */

  /**
    * 6.saveAsTextFile
    * */

}
