package com.fundamentosbigdata.sparkwordcount

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

object SparkWordCount {
  def main(args: Array[String]) {
val sc = new SparkContext(new SparkConf().setAppName("Spark Count"))

val centerEarthRDD = sc.textFile(args(0))

	// Separa as palavras do documento
	val tokenizedRDD = centerEarthRDD.flatMap(linha => linha.split(" "))

	// Conta a ocorrência de cada palavra
	val wordCounts = tokenizedRDD.map( palavra => (palavra,1) ).reduceByKey( (x, y) => x + y)
	
	System.out.println(wordCounts.collect().mkString(", "))
  }
}