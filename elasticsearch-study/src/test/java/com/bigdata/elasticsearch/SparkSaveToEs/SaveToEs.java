package com.bigdata.elasticsearch.SparkSaveToEs;

import com.bigdata.elasticsearch.bean.UserWriteable;
import com.bigdata.elasticsearch.utils.ConvertUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import org.elasticsearch.hadoop.cfg.ConfigurationOptions;
import org.elasticsearch.hadoop.mr.EsOutputFormat;
import org.elasticsearch.hadoop.util.WritableUtils;
import scala.Tuple2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by songshiyu on 2018/7/17.
 *
 *  测试向ES中插入数据,其中部分数据来自hbase
 */
public class SaveToEs {

    private final static String MAPPING_ID = "songshiyu";
    private final static String ES_RESOURCE = "songshiyu/demo";
    /**
     * elasticsearch地址列表
     */
    private static String esIP = "songshiyu001,songshiyu002,songshiyu003";
    private static String esPort = "9200";


    public static void main(String[] args) throws ClassNotFoundException {
        SparkConf sparkConf = new SparkConf().setAppName("SaveToEs").setMaster("local[6]").set("hadoop.home.dir", "D:\\hadoop\\hadoop-2.2.0").registerKryoClasses(new Class<?>[]{
                Class.forName("org.apache.hadoop.io.Text")
        });
        sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        sparkConf.set("spark.shuffle.consolidateFiles", "true");

        JavaSparkContext jsc = new JavaSparkContext(sparkConf);

        //连接ES配置
        Configuration configuration = new Configuration();
        //设置要操作的ES集群名称以及索引
        configuration.set(ConfigurationOptions.ES_RESOURCE, ES_RESOURCE);
        //设置要操作ES的mapping的id(也就是插入操作时的主键)
        configuration.set(ConfigurationOptions.ES_MAPPING_ID, MAPPING_ID);
        //集群地址
        configuration.set(ConfigurationOptions.ES_NODES, esIP);
        //集群的端口号
        configuration.set(ConfigurationOptions.ES_PORT, esPort);
        //此处是ES2.3的一个bug,此处如果不设置的话,会报一些意想不到的错误
        configuration.set("mapred.tip.id", "task_200707121733_0003_m_000005");

        SparkSession sparkSession = SparkSession.builder()
                .config(sparkConf)
                .config("hive.metastore.uris", "thrift://songshiyu001:9083")
                .config("spark.sql.warehouse.dir", "hdfs://songshiyu/user/hive/warehouse")
                .enableHiveSupport()
                .getOrCreate();

        SQLContext sqlContext = new SQLContext(sparkSession);

        JavaPairRDD<Text, MapWritable> hBaseRDD = getHBaseRDD(jsc);

        save2ES(jsc, configuration, sqlContext, sparkSession, hBaseRDD);

        jsc.stop();
    }

    public static void save2ES(JavaSparkContext jsc, Configuration configuration, SQLContext sqlContext, SparkSession sparkSession, JavaPairRDD<Text, MapWritable> hBaseRDD) {
        //测试开始  构建一个ES中的文档对象
        List<UserWriteable> list = new LinkedList<UserWriteable>();
        UserWriteable userWriteable = new UserWriteable();
        userWriteable.setUserid("3660593213");
        userWriteable.setAgerange(10);
        userWriteable.setBirthday("20170405");
        userWriteable.setCntFollowers(10);
        userWriteable.setCntFriends(10);
        userWriteable.setCntPosts(100);
        userWriteable.setGender("F");
        userWriteable.setModifiedtime("20180405");
        userWriteable.setLevelcode(30);
        userWriteable.setLoccountry(104561l);
        userWriteable.setLocProvince(1015464l);
        userWriteable.setLocCity(1245454l);
        userWriteable.setLocation("河北 石家庄");
        userWriteable.setScreenName("哈哈哈");
        userWriteable.setProfileImageUrl("http://tva2.sinaimg.cn/crop.0.0.179.179.50/da30383djw1e7ad08cco1j2050050aac.jpg");
        userWriteable.setProfileurl("www.baidu.com");
        userWriteable.setRegDate("20100506");
        userWriteable.setTagset("哈哈,嘿嘿,呵呵");
        userWriteable.setFriends("11111,2222,3333,4444");
        userWriteable.setEntitySectionUrn(195861l);

        list.add(userWriteable);

        JavaRDD<UserWriteable> testRDD = jsc.parallelize(list);

        JavaPairRDD<Text, MapWritable> hiveJavaPairRDD = testRDD.mapPartitionsToPair(new PairFlatMapFunction<Iterator<UserWriteable>, Text, MapWritable>() {
            @Override
            public Iterator<Tuple2<Text, MapWritable>> call(Iterator<UserWriteable> userWriteableIterator) throws Exception {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                List<Tuple2<Text, MapWritable>> resultList = new LinkedList<Tuple2<Text, MapWritable>>();

                //ES中插入时,是以MapWritable的格式进行插入,MapWritable中存放(key,value)的键值对
                //key是Text类型,代表ES mapping中具体的属性名称
                //value是Writable类型,mapping中具体的属性的值

                MapWritable mapWritable = new MapWritable();
                while (userWriteableIterator.hasNext()) {
                    UserWriteable user = userWriteableIterator.next();
                    String userUrn = null;
                    if (user.getUserid() != null && !"".equals(user.getUserid())) {
                        userUrn = user.getEntitySectionUrn() + "-" + user.getUserid();
                        Writable writable = WritableUtils.toWritable(user.getUserid());
                        mapWritable.put(new Text("userId"), writable);
                    }
                    if (user.getAgerange() != null && NumberUtils.isNumber(user.getAgerange().toString())) {
                        Writable writable = WritableUtils.toWritable(Integer.valueOf(user.getAgerange()));
                        mapWritable.put(new Text("agerange"), writable);
                    }
                    if (user.getBirthday() != null && !"".equals(user.getBirthday())) {
                        try {
                            Writable writable = WritableUtils.toWritable(sdf.format(new Date(Integer.valueOf(user.getBirthday().toString()))));
                            mapWritable.put(new Text("birthday"), writable);
                        } catch (Exception e) {
                            continue;
                        }
                    }
                    if (!"".equals(user.getBirthday())) {
                        Writable writable = WritableUtils.toWritable(user.getBirthday());
                        mapWritable.put(new Text("cntFollowers"), writable);
                    }
                    if (!"".equals(user.getCntFriends())) {
                        Writable writable = WritableUtils.toWritable(user.getCntFriends());
                        mapWritable.put(new Text("cntFriends"), writable);
                    }
                    if (!"".equals(user.getCntPosts())) {
                        Writable writable = WritableUtils.toWritable(user.getCntPosts());
                        mapWritable.put(new Text("cntPosts"), writable);
                    }
                    if (user.getGender() != null && !"".equals(user.getGender())) {
                        Writable writable = WritableUtils.toWritable(user.getGender());
                        mapWritable.put(new Text("gender"), writable);
                    }
                    if (user.getModifiedtime() != null && !"".equals(user.getModifiedtime())) {
                        Writable writable = WritableUtils.toWritable(user.getModifiedtime());
                        mapWritable.put(new Text("modifiedtime"), writable);
                    }
                    if (!"".equals(user.getLevelcode())) {
                        Writable writable = WritableUtils.toWritable(user.getLevelcode());
                        mapWritable.put(new Text("levelcode"), writable);
                    }
                    if (user.getLoccountry() != null && !"".equals(user.getLoccountry())) {
                        Writable writable = WritableUtils.toWritable(user.getLoccountry());
                        mapWritable.put(new Text("loccountry"), writable);
                    }
                    if (user.getLocProvince() != null && !"".equals(user.getLocProvince())) {
                        Writable writable = WritableUtils.toWritable(user.getLocProvince());
                        mapWritable.put(new Text("locProvince"), writable);
                    }
                    if (user.getLocCity() != null && !"".equals(user.getLocCity())) {
                        Writable writable = WritableUtils.toWritable(user.getLocCity());
                        mapWritable.put(new Text("locCity"), writable);
                    }
                    if (user.getLocation() != null && !"".equals(user.getLocation())) {
                        Writable writable = WritableUtils.toWritable(user.getLocation());
                        mapWritable.put(new Text("location"), writable);
                    }
                    if (user.getScreenName() != null && !"".equals(user.getScreenName())) {
                        Writable writable = WritableUtils.toWritable(user.getScreenName());
                        mapWritable.put(new Text("screenName"), writable);
                    }
                    if (user.getProfileImageUrl() != null && !"".equals(user.getProfileImageUrl())) {
                        Writable writable = WritableUtils.toWritable(user.getProfileImageUrl());
                        mapWritable.put(new Text("profileImageUrl"), writable);
                    }
                    if (user.getProfileurl() != null && !"".equals(user.getProfileurl())) {
                        Writable writable = WritableUtils.toWritable(user.getProfileurl());
                        mapWritable.put(new Text("profileurl"), writable);
                    }
                    if (user.getRegDate() != null && !"".equals(user.getRegDate())) {
                        try {
                            Writable writable = WritableUtils.toWritable(sdf.format(new Date(Integer.valueOf(user.getRegDate()))));
                            mapWritable.put(new Text("regDate"), writable);
                        } catch (Exception e) {
                            continue;
                        }
                    }
                    if (user.getTagset() != null && !"".equals(user.getTagset())) {
                        Writable writable = WritableUtils.toWritable(user.getTagset());
                        mapWritable.put(new Text("tagset"), writable);
                    }
                    if (user.getWeibotags() != null && !"".equals(user.getWeibotags())) {
                        Writable writable = WritableUtils.toWritable(user.getWeibotags());
                        mapWritable.put(new Text("weibotags"), writable);
                    }
                    if (user.getFriends() != null && !"".equals(user.getFriends())) {
                        Writable writable = WritableUtils.toWritable(user.getFriends());
                        mapWritable.put(new Text("friends"), writable);
                    }
                    mapWritable.put(new Text("entitySectionUrn"), WritableUtils.toWritable(user.getEntitySectionUrn()));
                    mapWritable.put(new Text("userUrn"), WritableUtils.toWritable(userUrn));
                    resultList.add(new Tuple2<Text, MapWritable>(new Text(userUrn), mapWritable));
                }
                return resultList.iterator();
            }
        });

        JavaPairRDD<Text, Tuple2<MapWritable, Optional<MapWritable>>> textTuple2JavaPairRDD = hiveJavaPairRDD.leftOuterJoin(hBaseRDD);

        textTuple2JavaPairRDD.mapPartitionsToPair(new PairFlatMapFunction<Iterator<Tuple2<Text, Tuple2<MapWritable, Optional<MapWritable>>>>, Text, MapWritable>() {
            @Override
            public Iterator<Tuple2<Text, MapWritable>> call(Iterator<Tuple2<Text, Tuple2<MapWritable, Optional<MapWritable>>>> tuple2Iterator) throws Exception {
                MapWritable mapWritable = new MapWritable();
                List<Tuple2<Text, MapWritable>> resultList = new LinkedList<Tuple2<Text, MapWritable>>();
                while (tuple2Iterator.hasNext()) {
                    Tuple2<Text, Tuple2<MapWritable, Optional<MapWritable>>> nextTuple = tuple2Iterator.next();
                    Text userUrn = nextTuple._1;
                    Tuple2<MapWritable, Optional<MapWritable>> mapWritableOptionalTuple2 = nextTuple._2;
                    MapWritable mapWritable1 = mapWritableOptionalTuple2._1;
                    Optional<MapWritable> mapWritableOptional = mapWritableOptionalTuple2._2;
                    MapWritable mapWritable2 = mapWritableOptional.get();
                    mapWritable1.put(WritableUtils.toWritable("fans"), mapWritable2.get(new Text("fans")));
                    resultList.add(new Tuple2<Text, MapWritable>(userUrn, mapWritable1));
                }
                return resultList.iterator();
            }
        }).saveAsNewAPIHadoopFile("/post/hadoop/", Text.class, MapWritable.class, EsOutputFormat.class, configuration);
    }


    public static JavaPairRDD<Text, MapWritable> getHBaseRDD(JavaSparkContext jsc) {
        //连接HBase集群
        Configuration conf = HBaseConfiguration.create();

        Scan scan = new Scan();
        conf.setLong(HConstants.HBASE_CLIENT_SCANNER_TIMEOUT_PERIOD,600000);//设置scan扫描的超时时间
        conf.setLong(HConstants.HBASE_RPC_TIMEOUT_KEY,5000); //解决:java.io.IOException: Connection reset by peer 一次RPC请求的超时时间
        conf.setLong(HConstants.HBASE_CLIENT_RETRIES_NUMBER,5);//连接RegionServer失败后的重试次数,默认为35次

        conf.set(TableInputFormat.INPUT_TABLE,"demo");
        conf.set(TableInputFormat.SCAN, ConvertUtils.scanToString(scan));

        //宋时雨测试开始
        List<String> list = new LinkedList<String>();
        String str = "fans-195861-3660593213-1111111";
        String str1 = "fans-195861-3660593213";
        String str2 = "fans-195861-3660593213";
        list.add(str);
        list.add(str1);
        list.add(str2);

        JavaRDD<String> javaRDD = jsc.parallelize(list);

        JavaPairRDD<Text, MapWritable> textMapWritableJavaPairRDD = javaRDD.mapToPair(new PairFunction<String, String, String>() {
            @Override
            public Tuple2<String, String> call(String s) throws Exception {
                String[] split = null;
                if (s.contains("-") && s.split("-").length >= 4) {
                    split = s.split("-");
                    Tuple2<String, String> Tuple2 = new Tuple2<String, String>((split[1] + "-" + split[2]), split[3].trim());
                    return Tuple2;
                }
                return new Tuple2<String, String>(null,null);
            }
        }).reduceByKey(new Function2<String, String, String>() {
            @Override
            public String call(String v1, String v2) throws Exception {
                if (v1 != null&& v2 != null){
                    return v1.toString() + v2.toString();
                }
                return null;
            }
        }).mapToPair(new PairFunction<Tuple2<String, String>, Text, MapWritable>() {
            @Override
            public Tuple2<Text, MapWritable> call(Tuple2<String, String> tuple2) throws Exception {
                if (tuple2._1 != null && tuple2._2 != null){
                    MapWritable mapWritable = new MapWritable();
                    mapWritable.put(new Text("fans"), WritableUtils.toWritable(tuple2._2.trim()));
                    mapWritable.put(new Text("userUrn"), WritableUtils.toWritable(tuple2._1.trim()));
                    return new Tuple2<Text, MapWritable>(new Text(tuple2._1), mapWritable);
                }
                return new Tuple2<Text, MapWritable>(null,null);
            }
        });
        return textMapWritableJavaPairRDD;
    }
}

