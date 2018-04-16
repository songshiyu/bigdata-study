package com.bigdata.storm.local;

import org.apache.commons.io.FileUtils;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
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

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalWordCountTopology {
    //读取F:/data 下的所有文件

    static class ReadFileSpout extends BaseRichSpout {
        private Map config;
        private TopologyContext topologyContext;
        private SpoutOutputCollector spoutOutputCollector;

        @Override
        public void open(Map config, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
            this.config = config;
            this.topologyContext = topologyContext;
            this.spoutOutputCollector = spoutOutputCollector;
        }

        @Override
        public void nextTuple() {
            File fileDir = new File("F:/data");
            Collection<File> listFiles = FileUtils.listFiles(fileDir, new String[]{"txt", "log"}, true);  //递归读取路径下的所有文件
            for (File file : listFiles) {
                try {
                    List<String> lines = FileUtils.readLines(file, "UTF-8");
                    for (String line : lines) {
                        System.out.println("读取到的文件内容为:" + line);
                        this.spoutOutputCollector.emit(new Values(line));
                    }
                    File destFile = new File(file.getAbsoluteFile() + ".completed." + System.currentTimeMillis());
                    FileUtils.moveFile(file, destFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
            outputFieldsDeclarer.declare(new Fields("line"));
        }
    }

    static class readFileBolt extends BaseRichBolt {
        private Map config;
        private TopologyContext topologyContext;
        private OutputCollector outputCollector;

        @Override
        public void prepare(Map config, TopologyContext topologyContext, OutputCollector outputCollector) {
            this.config = config;
            this.topologyContext = topologyContext;
            this.outputCollector = outputCollector;
        }

        @Override
        public void execute(Tuple tuple) {
            String line = tuple.getStringByField("line");
            String[] words = line.split(" ");
            for (String word:words){
                this.outputCollector.emit(new Values(word,1));
            }
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
            outputFieldsDeclarer.declare(new Fields("word","count"));
        }
    }

    static class wordCountBolt extends BaseRichBolt{
        private Map config;
        private  TopologyContext topologyContext;
        private  OutputCollector outputCollector;
        @Override
        public void prepare(Map config, TopologyContext topologyContext, OutputCollector outputCollector) {
            this.config = config;
            this.topologyContext = topologyContext;
            this.outputCollector = outputCollector;
        }

        Map<String,Integer> map = new HashMap<String,Integer>();
        @Override
        public void execute(Tuple tuple) {
            String word = tuple.getStringByField("word");
            Integer count = tuple.getIntegerByField("count");
            map.put(word,map.getOrDefault(word,0) + count);
            map.forEach((k,v) ->{
                System.out.println(k + "=>" + v);
            });
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

        }
    }

    public static void main(String[] args) {

        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("id_Spout_count", new ReadFileSpout());
        topologyBuilder.setBolt("id_Bolt_read", new readFileBolt()).shuffleGrouping("id_Spout_count");
        topologyBuilder.setBolt("id_Bolt_count",new wordCountBolt()).shuffleGrouping("id_Bolt_read");

        StormTopology stormTopology = topologyBuilder.createTopology();
        LocalCluster cluster = new LocalCluster();
        String name = LocalWordCountTopology.class.getSimpleName();
        Config map = new Config();
        cluster.submitTopology(name, map, stormTopology);

    }
}
