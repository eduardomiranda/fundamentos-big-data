package com.fundamentosbigdata

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.hdfs.DistributedFileSystem


object SparkStreamingKafka extends App {

  val conf = new SparkConf()
        .setAppName("SparkStreamingKafka")
        .setMaster("local[2]")
        .set("spark.streaming.kafka.maxRatePerPartition", "100")


  val sc = new SparkContext(conf)


  val streamingContext = new StreamingContext(sc, Seconds(60))

  val kafkaParams = Map[String, Object](
    "bootstrap.servers" -> "localhost:9092",
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "group.id" -> "grupo_kafka",
    "auto.offset.reset" -> "earliest",
    "enable.auto.commit" -> (true: java.lang.Boolean)
  )

  val topics = Array("text")

  val stream = KafkaUtils.createDirectStream[String, String](
    streamingContext,
    PreferConsistent,
    Subscribe[String, String](topics, kafkaParams)
  )


  stream.map(record => record.value)
        .flatMap(line => line.split("[ ,\\.;:\\-]+"))
        .map(word => word.toLowerCase)
        .filter(_.size > 0)
        .map(word => (word, 1))
        .reduceByKey(_ + _)
        .repartition(1)
        .transform(rdd => rdd.sortBy(-_._2))
        .saveAsTextFiles("hdfs://quickstart.cloudera:8020/output/words")

  streamingContext.start()
  streamingContext.awaitTermination()
}
