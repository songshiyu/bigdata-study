package com.bigdata.storm.parallelism;

import com.bigdata.storm.remote.RemoteSumTopology;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 1、实现数字的累加求和案例：
 *      数据源不断的产生递增数字，对产生的数字累加求和
 *
 *      调整strom worker并行度的数量
 */

public class ParallelismWorkerSumTopology {

    static class SumSpout extends BaseRichSpout {
        private Map conf;
        private TopologyContext context;
        private SpoutOutputCollector collector;

        @Override
        public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
            this.conf = conf;
            this.context = context;
            this.collector = collector;
        }

        /**
         * 产生数据源的核心方法
         * 每产生一条记录调用一次该方法。
         */
        long num = 0;

        @Override
        public void nextTuple() {
            try {
                num++;
                System.out.println("当前订单的交易额:" + num);
                this.collector.emit(new Values(num));
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /**
         * 改方法就是对collector发射去的数据进行说明，说白了就是加一个字段用于后续方便获取
         */
        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("num"));
        }
    }

    static class SumBolt extends BaseRichBolt {
        private Map boltConf;
        private TopologyContext boltContext;
        private OutputCollector boltCollector;

        @Override
        public void prepare(Map boltConf, TopologyContext boltContext, OutputCollector boltCollector) {
            this.boltConf = boltConf;
            this.boltContext = boltContext;
            this.boltCollector = boltCollector;
        }

        /**
         * 这里是核心的业务逻辑处理单元
         */
        long sum = 0;

        @Override
        public void execute(Tuple input) {
            Long num = input.getLongByField("num");
            sum += num;
            System.out.println("截止到" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "当前订单总交易额：" + sum);
        }

        /**
         * 如果没有下游操作的话，declareOutputFields()这个方法可以不用复写
         */
        @Override
        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

        }
    }

    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("id_sum_spout", new SumSpout());
        topologyBuilder.setBolt("bolt", new SumBolt())
                .shuffleGrouping("id_sum_spout");  //shuffleGrouping的作用是将bolt的输入指向了spout的或者其他bolt的输出
        //构建拓扑结构
        StormTopology topology = topologyBuilder.createTopology();
        //构建一个本地的模拟集群，执行本地测试任务
        Config config = new Config();
        String topolgyName = RemoteSumTopology.class.getSimpleName();
        if (args.length < 1) {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology(topolgyName, config, topology);
        }else{
            config.setNumWorkers(2);
            StormSubmitter.submitTopology(topolgyName,config,topology);
        }
    }

}
