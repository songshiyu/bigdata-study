package com.bigdata.storm;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;

/**
 * 使用MapReduce统计文件（hdfs://ns1/input/mr/hello）中每一个单词出现的次数
 * 整个解题思路：
 *          使用map函数进行单词的拆分，使用reduce函数进行一个汇总，中间进行shuffle
 *          要想让我们的map函数和reduce函数进行接替运行，需要一个驱动程序
 *
 * 1、编写一个类继承Mapper，成为自定义的Mapper，主要业务逻辑就是复写其中的map函数
 *      map
 *      首先要确定清楚Mapper类或者map函数，它的数据类型/类型参数-----------泛型
 *      Mapper<K1,V1,K2,V2>
 * 2、编写一个类继承Reduce，成为自定义的Reduce，主要业务逻辑就是复写其中的reduce函数
 *      reduce
 *      首先要确定清楚Reduce类或者reduce函数的数据类型/类型参数------------泛型
 *      Reduce<K2,V2S,K3,V3>
 *  需要我们用户自定义的类型就是k2,v2,k3,v3,  其中K1和V1一般情况下是固定的，只要数据格式确定的，其类型就确定
 *      比如我们操作的是普通的文本文件，k1=LongWritable,V1=Text
 *       K1===》代表的都是这一行记录在整个文本中的偏移量
 *       K2===》代表这一行文本的内容
 *      注意：我们的Hadoop中需要使用Hadoop中提供的这些类型，而不能使用java中提供的基本数据类型
 * */
public class WorldCountJob {

        /**
         * 这里的主函数，就是组织map函数和reduce函数
         * 最终的mr的运行会转变成为一个个的Job
         * */
        public static void main(String[] args) throws Exception {
            Configuration conf = new Configuration();
            String jobName = WorldCountJob.class.getSimpleName();
             Job job =  Job.getInstance(conf,jobName);
            //添加我们要运行的主函数所在的类
            job.setJarByClass(WorldCountJob.class);

            //TODO 设置mr的输入参数，设置计算的文件  指定 使用哪个mapper来进行计算
            FileInputFormat.setInputPaths(job,"hdfs://songshiyu001:9000/input/mr/hello");
            job.setInputFormatClass(TextInputFormat.class);  //指定解析数据源的Format类

            job.setMapperClass(WorldCountMapper.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(LongWritable.class);

            //TODO 设置mr的输出参数，设置输出目录，指定谁来执行汇总操作

            Path outPath = new Path("hdfs://ns1/output/mr/wc");       //如果该目录已经存在，会抛异常，目录存在异常
            outPath.getFileSystem(conf).delete(outPath,true);         //若目录存在，则递归删除该目录，即不会抛异常
            FileOutputFormat.setOutputPath(job,outPath);

            job.setOutputFormatClass(TextOutputFormat.class);
            job.setReducerClass(WordCountReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(LongWritable.class);

            //设置有几个reduce执行我们的mr程序，默认1个
            job.setNumReduceTasks(1);
            //提交mapredece job
            job.waitForCompletion(true);
        }

        /**
         * 自定义的Mapper类，主要完成map阶段业务的编写
         * */
    static class WorldCountMapper extends Mapper<LongWritable,Text,Text,IntWritable> {
        /**
         * LongWritable====> 偏移量
         * Text ===========> value值
         * Text ===========> 单个字符串
         * IntWritable ====> 单个字符串出现的次数
         * */
            @Override
            protected void map(LongWritable k1, Text v1, Context context) throws IOException, InterruptedException {
                String line = v1.toString();
                String[] words = line.split(" ");
                Text k2 = null;
                IntWritable v2 = null;
                for (String word:words){
                    k2 = new Text(word);
                    v2 = new IntWritable(1);
                    context.write(k2,v2);
                }
            }
        }

        static class WordCountReducer extends Reducer<Text,IntWritable,Text,IntWritable> {
            /**
             * Text ======> 单个字符串
             * IntWritable =====> 单个字符串出现的次数
             * Text ======> reduce完成后的字符串
             * IntWritable =====> 每个字符串出现的总次数
             * */
            @Override
            protected void reduce(Text k2, Iterable<IntWritable> v2s, Context context) throws IOException, InterruptedException {
                int sum = 0;
                for (IntWritable iw:v2s){
                    sum += iw.get();
                }
                Text k3 = k2;
                IntWritable v3 = new IntWritable(sum);
                context.write(k3,v3);
            }
        }
}
