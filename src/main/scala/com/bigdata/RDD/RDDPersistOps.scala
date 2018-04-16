package com.bigdata.RDD


/**
  * 关于spark rdd的持久化说明：
  *     调用RDD的cache()或者persist(持久化策略)进行持久化
  *     要想卸载持久化的rdd，执行rdd.unpersist()即可
  * 持久化策略：
  *   建议使用:
  *     1.MEMORY_ONLY
  *     2.MEMORY_ONLY_SWR
  *     3.MEMORY_AND_DISK_SER
  *     4.MEMORY_AND_DISK
  *   不建议使用
  *     4.DISK_ONLY
  *     5.xxxx_2
  *
  *   如果以上建议的四种方案还解决不了，请使用第三方存储管理系统，比如：Redis、Hbase、ElasticSearch、Ignite
  * */
object RDDPersistOps {
  def main(args: Array[String]): Unit = {

  }

}
