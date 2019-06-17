package com.sparkstreaming.study.sparkUseKafka;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import scala.Tuple2;

import java.util.*;
import java.util.logging.Logger;

/**
 * @author songshiyu
 * @date 2019/6/14 16:33
 */
public class SparkStreamingKafkaTest {
    private static Logger logger = Logger.getLogger(SparkStreamingKafkaTest.class.getSimpleName());

    public static void main(String[] args) throws InterruptedException {
        String brokers = "songshiyu001:9092,songshiyu002:9092,songshiyu003:9092";
        String topics = "kafka";

        SparkConf conf = new SparkConf().setAppName(SparkStreamingKafkaTest.class.getSimpleName()).setMaster("local[*]");
        JavaStreamingContext ssc = new JavaStreamingContext(conf, Duration.apply(10000));

        Collection<String> topicCollection = new HashSet<>(Arrays.asList(topics.split(",")));
        //kafka相关参数，必要！缺了会报错
        Map<String, Object> kafkaParams = new HashMap<>();

        kafkaParams.put("metadata.broker.list", brokers) ;
        kafkaParams.put("bootstrap.servers", brokers);
        kafkaParams.put("group.id", "group1");
        kafkaParams.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaParams.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaParams.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        //如果没有初始化偏移量或者当前的偏移量不存在任何服务器上，可以使用这个配置属性
        //earliest 当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费
        //latest 当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据
        //none topic各分区都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常
        kafkaParams.put("auto.offset.reset", "earliest");
        //kafkaParams.put("enable.auto.commit",false);

        JavaInputDStream<ConsumerRecord<String, String>> kafkaStream =
                KafkaUtils.createDirectStream(
                        ssc,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.<String, String>Subscribe(topicCollection, kafkaParams)
                );

        kafkaStream.flatMapToPair(new PairFlatMapFunction<ConsumerRecord<String,String>, String, Integer>() {
            @Override
            public Iterator<Tuple2<String, Integer>> call(ConsumerRecord<String, String> s) throws Exception {
                List<Tuple2<String,Integer>> list = new ArrayList<>();
                list.add(new Tuple2<>(s.value(),1));
                logger.info("s.key====>" + s.key());
                logger.info("s.topic====>" + s.topic());
                logger.info("s.offset====>" + s.offset());
                return list.iterator();
            }
        }).reduceByKey((v1, v2) -> v1 + v2).print();

        ssc.start();
        ssc.awaitTermination();

    }
}
