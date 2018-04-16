package com.bigdata.elasticsearch;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用java api操作ES集群
 *
 *      es 客户端api的入口类是Transport,代码ES内部节点与客户端的交互方式，默认
 *  内部使用tcp协议进行交互，同时他支持http协议、thrift、servlet、memcached、zreoMQ等传输协议
 * */
public class ElasticSearchTest {
    private TransportClient tc;
     @Before
    public void setUp() throws UnknownHostException {
         Settings settings = Settings.builder().put("cluster.name","bigdate").build();
         tc = TransportClient.builder().settings(settings).build();
         TransportAddress ta1 = new InetSocketTransportAddress(InetAddress.getByName("songshiyu001"),9300);
         TransportAddress ta2 = new InetSocketTransportAddress(InetAddress.getByName("songshiyu001"),9300);
         TransportAddress ta3 = new InetSocketTransportAddress(InetAddress.getByName("songshiyu001"),9300);
         tc.addTransportAddresses(ta1,ta2,ta3);
         System.out.println(tc);
     }

     private String index = "bigdate";
     private String type = "test";

     /**
      *     向ES中插入数据
      *     json
      *     map
      *     对象
      *     XContentBuilder
      * */
     @Test
     public void  testInsertJson(){
         String resource = "{\"name\":\"flume\",\"author\":\"cloudera\",\"last_version\":\"1.7.0\"}";
         System.out.println(resource);
         IndexResponse response = tc.prepareIndex(index, type, "2").setSource(resource).get();
         System.out.println("version:" + response.getVersion());
     }
     @Test
     public void testInsertMap(){
         Map<String,Object> map = new HashMap<>();
         map.put("name","kafka");
         map.put("author","LinkedIn");
         map.put("version","1.10-0.10.0.1");
         IndexResponse response2 = tc.prepareIndex(index, type, "4").setSource(map).get();
         System.out.println("version:" + response2.getVersion());
     }

     @Test
     public void testDelete(){
         DeleteResponse response = tc.prepareDelete(index, type, "2").get();   //三个参数值分别为：索引名称、类型type、版本号
         long version = response.getVersion();
         System.out.println("version:" + version);
     }

     @After
    public void cleanUp(){

     }

}
