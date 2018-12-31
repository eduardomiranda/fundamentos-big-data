# Spark Streaming + Kafka 0.10

Este código é uma alteração do original [Spark Streaming + Kafka 0.10 Demo](https://github.com/joanvr/spark-streaming-kafka-010-demo)


## Download do Apache Kafka 0.10

```
$ cd Desktop/
```


Download do Apache Kafka 0.10 
```
$ wget https://archive.apache.org/dist/kafka/0.10.2.1/kafka_2.11-0.10.2.1.tgz
```


Descompactando
```
$ tar -xzf kafka_2.11-0.10.2.1.tgz
```


## Download do Apache Spark 2.1.3

```
$ cd Desktop/
```


Download do Apache Spark 2.1.3  
```
$ wget  http://mirror.nbtelecom.com.br/apache/spark/spark-2.1.3/spark-2.1.3-bin-hadoop2.7.tgz
```


Descompactando
```
$ tar -zxvf spark-2.1.3-bin-hadoop2.7.tgz
```


## Executando o exemplo


Iniciando o Kafka com as configurações padrão
```
$ cd kafka_2.11-0.10.2.1/

$ bin/kafka-server-start.sh config/server.properties
```


Criando O tópico Kafka `text` consumido pela aplicação **Spark Streaming**
```
$ /usr/bin/kafka-topics  \
   --create  \
   --zookeeper ip-10-0-105-204:2181  \
   --replication-factor 1  \
   --partitions 1  \
   --topic text 
```


Gerando o código da aplicação **Spark Streaming**
```
$ cd /fundamentos-big-data/codigo/spark/SparkStreamingkafka010
$ mvn clean package
```


Executando a aplicação **Spark Streaming**
```
$ cd spark-2.1.3-bin-hadoop2.7/

$ bin/spark-submit \
   --class com.fundamentosbigdata.SparkStreamingKafka \
   --master local \
   /fundamentos-big-data/codigo/spark/SparkStreamingkafka010/target/spark-streaming-kafka-1.0.jar
```


Produzindo mensagens no tópico `text`
```
$ bin/kafka-console-producer.sh  \
   --broker-list localhost:9092  \
   --topic text  
```

Verificando no **HDFS** o resultado do processamento
```
$ hdfs dfs -cat /output/words-1546281540000/part-00000
```