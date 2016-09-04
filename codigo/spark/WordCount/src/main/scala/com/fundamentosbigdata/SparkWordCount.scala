package com.fundamentosbigdata

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.SparkContext._

object SparkWordCount {
  def main(args: Array[String]) {
    val context = new SparkContext(new SparkConf().setAppName("Spark Word Count"))
    val input = context.textFile(args(0))

    val tokens = input.flatMap(line => line.split(" "))
    val wordCounts = tokens.
      map(word => (word, 1)).
      reduceByKey((x, y) => x + y)

    System.out.println(wordCounts.collect().mkString(", "))
  }
}
