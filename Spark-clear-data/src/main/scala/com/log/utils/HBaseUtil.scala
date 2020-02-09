package com.log.utils

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.{Put, Result, Scan}
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp
import org.apache.hadoop.hbase.filter.{RegexStringComparator, RowFilter}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.protobuf.ProtobufUtil
import org.apache.hadoop.hbase.util.Base64
import org.apache.hadoop.mapred.JobConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions

object HBaseUtil {


  //0.准备参数
  val zkPath = "hadoop0,hadoop1,hadoop2";
  val zkPort = "2181";

  /**
    * 查询hbase中指定表中的指定数据
    * 返回的数据格式：
    * rdd{
    *	Map{
    *			("rk"->"xxx"),
    *			("url"->"xxx"),
    *			("urlname"->"xxx"),
    *			...
    *		}
    * }
    */
  def queryHBase(sc:SparkContext,tab:String,start:Array[Byte],stop:Array[Byte],rkRegex:String)={
    //1.创建配置对象
    val hbaseConf = HBaseConfiguration.create();
    hbaseConf.set("hbase.zookeeper.quorum", zkPath)
    hbaseConf.set("hbase.zookeeper.property.clientPort", zkPort)
    hbaseConf.set(TableInputFormat.INPUT_TABLE, tab)
    //2.创建scan
    val scan = new Scan()
    if(start!=null){
      scan.setStartRow(start)
    }
    if(stop!=null){
      scan.setStopRow(stop)
    }
    if(rkRegex!=null){
      val filter = new RowFilter(CompareOp.EQUAL,new RegexStringComparator(rkRegex))
      scan.setFilter(filter)
    }
    hbaseConf.set(TableInputFormat.SCAN, Base64.encodeBytes(ProtobufUtil.toScan(scan).toByteArray()));
    //3.查询数据
    val rdd = sc.newAPIHadoopRDD(hbaseConf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    //4.处理数据
    val resultRdd = rdd.map(t=>{
      val result = t._2

      var m:Map[String,Array[Byte]] = Map()
      m = m + ("rk"->result.getRow)
      val it = result.listCells().iterator()
      while(it.hasNext()){
        val cell = it.next()
        val c = cell.getQualifierArray
        val v = cell.getValue
        m = m + (new String(c) -> v)
      }
      m
    })
    resultRdd
  }

  /**
    * 向hbase中写入数据
    * 数据格式：
    * 	tab="tabx1"
    * 	rk=rk.getBytes
    * 	Map(
    * 		"cf1:c1"->"v1111".getBytes
    * 		"cf1:c2"->"v1121".getBytes
    * 		"cf2:c3"->"v1231".getBytes
    * 	)
    *
    */
  def saveHbase(sc:SparkContext,tab:String,rk:Array[Byte],dataMap:Map[String,Array[Byte]]){
    //1.准备配置信息
    val hbaseConf = HBaseConfiguration.create();
    hbaseConf.set("hbase.zookeeper.quorum", zkPath)
    hbaseConf.set("hbase.zookeeper.property.clientPort", zkPort)
    val jobConf = new JobConf(hbaseConf)
    jobConf.setOutputFormat(classOf[TableOutputFormat])
    jobConf.set(TableOutputFormat.OUTPUT_TABLE,tab)
    //2.准备数据
    val put = new Put(rk);
    dataMap.foreach(t=>{
      put.add(t._1.split(":")(0).getBytes, t._1.split(":")(1).getBytes, t._2)
    })
    val rdd = sc.makeRDD(Array((new ImmutableBytesWritable,put)))
    //3.写入数据
    rdd.saveAsHadoopDataset(jobConf);
  }
}
