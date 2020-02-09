package com.log

import java.util.{Calendar, Random}

import com.log.utils.{HBaseUtil, MysqlUtil}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.{SparkConf, SparkContext}

object ClearData {
  //0_0.配置序列化器//0_0.配置序列化器
  System.setProperty("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
  //0_1.创建sc
  val conf = new SparkConf();
  conf.setAppName("ClearData")
//  conf.setMaster("spark://spark0:7077")
  conf.setMaster("local[5]")
  val sc = new SparkContext(conf)

  def main(args: Array[String]): Unit = {
    //0_0.参数设定
    val zk = "hadoop0:2181,hadoop1:2181,hadoop2:2181";
    val gid = "gx01";

    //0_2.创建ssc
    val ssc = new StreamingContext(sc,Seconds(3))

    //1.从kafka中消费数据
    val kafkaStream = KafkaUtils.createStream(ssc, zk, gid, Map("fluxtopic1"->1))

    //2.清洗数据
    val clearStream = kafkaStream
      .map(_._2)
      .map(_.split(" - ")(2))
      .filter(_.matches("^([^\\|]*\\|){15}[^\\|]*$"))
      .map(_.split("\\|"))
      .map(arr=>{Map(
        "url"->arr(0)
        ,"urlname"->arr(1)
        ,"ref"->arr(11)
        ,"uagent"->arr(12)
        ,"uvid"->arr(13)
        ,"ssid"->arr(14).split("_")(0)
        ,"sscount"->arr(14).split("_")(1)
        ,"sstime"->arr(14).split("_")(2)
        ,"cip"->arr(15)
      )})
    //--处理数据
    val resultStream = clearStream.map(map=>{
      //pv
      //点击量 - 一天之内访问的数量 - 一条日志就是一个pv
      val pv = 1

      //uv
      //独立访客数 - 一天之内访客的数量，同一个用户一天之内只记一个uv
      //- 获取当前日志的uvid，到今天这条日志之间的数据中查看是否出现过，如果没有出现过uv+1,否则uv+0
      val uvid = map("uvid")
      val nowtime = map("sstime").toLong
      val calendar = Calendar.getInstance()
      calendar.setTimeInMillis(nowtime)
      calendar.set(Calendar.MILLISECOND, 0)
      calendar.set(Calendar.SECOND, 0)
      calendar.set(Calendar.MINUTE, 0)
      calendar.set(Calendar.HOUR_OF_DAY, 0)
      val zerotime = calendar.getTimeInMillis()
      val resultRdd = HBaseUtil.queryHBase(sc, "fluxtab", (zerotime+"").getBytes, (nowtime+"").getBytes, "^\\d+_"+uvid+"_.*$");
      val uv = if(resultRdd.count() == 0) 1 else 0;

      //vv
      //会话总数 - 一天之内会话的总的数量 - 如果当前日志sscount==0说明是一个新的会话 vv=1 否则vv=0
      val sscount = map("sscount")
      val vv = if(sscount == "0") 1 else 0

      //br
      //跳出率 - 一天内跳出的会话占总的会话的比率

      //newip
      //新增客户总数 - 当前日志的cip在历史数据寻找，如果从未出现过则 newip+1 否则newip+0
      val cip = map("cip")
      val resultRdd2 = HBaseUtil.queryHBase(sc, "fluxtab", null, (nowtime+"").getBytes, "^\\d+_\\d+_\\d+_"+cip+"_.*$")
      val newip = if(resultRdd2.count()==0) 1 else 0;

      //newcust
      //新增客户总数 - 当前日志的uvid在历史数据中寻找，如果从未出现过则newcust+1 否则newcust+0
      val resultRdd3 = HBaseUtil.queryHBase(sc, "fluxtab", null, (nowtime+"").getBytes, "^\\d+_"+uvid+"_.*$")
      val newcust = if(resultRdd3.count()==0) 1 else 0;

      //avgtime
      //平均访问时长 - 一天之内所有会话访问时长的平均值

      //avgdeep
      //平均访问深度 - 一天之内所有会话访问深度的平均值

      //返回结果
      (nowtime,pv,uv,vv,newip,newcust)
    })
    //--向hbase写入数据
    clearStream.foreachRDD(rdd=>{
      rdd.foreach(m=>{
        val dataMap = m.map(t=>{("cf1:"+t._1,t._2.getBytes)})
        //sstime(13)_uvid(20)_ssid(10)__cip(7~15)_rand(2~10)
        val rktmp = m("sstime")+"_"+m("uvid")+"_"+m("ssid")+"_"+m("cip")+"_"
        val rk = rktmp + (for(i<-0 to 64-rktmp.length()) yield {new Random().nextInt(10).toString()}).reduce(_+_)

        println(rk)
        HBaseUtil.saveHbase(sc, "fluxtab", rk.getBytes, dataMap)
      })
    });
    //4.将结果写入mysql
    resultStream.print()
    resultStream.foreachRDD(rdd=>{
      rdd.foreach(t=>{
        MysqlUtil.upsertTongji2(t);
      })
    })
      //启动程序
    ssc.start()
    ssc.awaitTermination();
  }
}
