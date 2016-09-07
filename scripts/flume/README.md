# Analisando dados do Twitter usando Apache Flume, Apache Hadoop e Apache Hive
Grande parte das instruções abaixo são traduções do repositório *Analyzing Twitter Data Using CDH*.

## Clonar o repositório Analyzing Twitter Data Using CDH
Este repositório possui códigos fonte para um source customizado do Flume e para um JSON SerDe para o Hive.

```bash
$ git clone https://github.com/cloudera/cdh-twitter-example.git
```
O diretório *flume-sources* contém um projeto Maven com um source Flume projetado para se conectar à API do Twitter e ingerir no HDFS os tweets em um formato JSON bruto. O diretório *hive-serdes* contém um projeto Maven para um JSON SerDe que permite ao Hive fazer consultas em JSON puro.

## Source customizado para o Flume
O source customizado do Flume pode ser baixado em http://files.cloudera.com/samples/flume-sources-1.0-SNAPSHOT.jar ou criado por meio dos comandos listados abaixo. 
```bash
$ cd cdh-twitter-example
$ cd flume-sources  
$ mvn package
```
Será criado o arquivo *flume-sources-1.0-SNAPSHOT.jar* no diretório target.

## Adicionar o arquivo *flume-sources-1.0-SNAPSHOT.jar* ao classpath do Flume
Copie o arquivo *flume-sources-1.0-SNAPSHOT.jar* para as pastas:
* /usr/lib/flume-ng/plugins.d/twitter-streaming/lib/
* /var/lib/flume-ng/plugins.d/twitter-streaming/lib/

Caso as pastas não existam, crie-as:
```bash
$ sudo mkdir -p /usr/lib/flume-ng/plugins.d/twitter-streaming/lib/
$ sudo mkdir -p /var/lib/flume-ng/plugins.d/twitter-streaming/lib/ 
```

## JSON SerDe para o Hive
O JSON SerDe para o Hive pode ser baixado em http://files.cloudera.com/samples/hive-serdes-1.0-SNAPSHOT.jar ou criado por meio dos comandos listados abaixo. 
```bash
$ cd cdh-twitter-example
$ cd hive-serdes  
$ mvn package
```
Será criado o arquivo *hive-serdes-1.0-SNAPSHOT.jar* no diretório target.

## Criação da tabela no Hive
### Conectando ao beeline
```bash
$ beeline 
$ !connect jdbc:hive2://localhost:10000 username password
```
Username e password na máquina virtual CDH 5.7: *cloudera*

### Adicionando o JAR do JSON SerDe
```bash
$ ADD JAR /home/cloudera/Desktop/cdh-twitter-example/hive-serdes/target/hive-serdes-1.0-SNAPSHOT.jar;
```

### Criação da tabela no Hive
```sql
CREATE EXTERNAL TABLE tweets_raw (
   id BIGINT,
   created_at STRING,
   source STRING,
   favorited BOOLEAN,
   retweet_count INT,
   retweeted_status STRUCT<
      text:STRING,
      user:STRUCT<screen_name:STRING,name:STRING>>,
   entities STRUCT<
      urls:ARRAY<STRUCT<expanded_url:STRING>>,
      user_mentions:ARRAY<STRUCT<screen_name:STRING,name:STRING>>,
      hashtags:ARRAY<STRUCT<text:STRING>>>,
   text STRING,
   user STRUCT<
      screen_name:STRING,
      name:STRING,
      friends_count:INT,
      followers_count:INT,
      statuses_count:INT,
      verified:BOOLEAN,
      utc_offset:STRING, -- was INT but nulls are strings
      time_zone:STRING>,
   in_reply_to_screen_name STRING,
   year int,
   month int,
   day int,
   hour int
)
ROW FORMAT SERDE 'com.cloudera.hive.serde.JSONSerDe'
LOCATION '/user/flume/twitter_data';
```

## Iniciando o agente Flume
Crie um diretório no HDFS onde o Flume vai descarregar os dados.
```bash
$ hadoop fs -mkdir -p /user/flume/twitter_data/
```

### Twitter Consumer Key, Consumer Secret, Access Token, Access Token Secret
Edite o arquivo *twitter-flume.conf* substituindo os valores das propriedades:

* TwitterAgent.sources.Twitter.consumerKey 
* TwitterAgent.sources.Twitter.consumerSecret 
* TwitterAgent.sources.Twitter.accessToken  
* TwitterAgent.sources.Twitter.accessTokenSecret 


Em um outro terminal, inicie o agente Flume
```bash
$ flume-ng agent \
--conf /home/cloudera/Desktop/fundamentos-big-data/scripts/flume /conf/ \
-f /home/cloudera/Desktop/fundamentos-big-data/scripts/flume/twitter-flume.conf \ -n TwitterAgent
```

## Analisando os dados

### Qual usuário tem o maior número de seguidores?
```sql
SELECT user.screen_name, user.followers_count c 
FROM tweets_raw 
ORDER BY c DESC;
```
 
### Qual usuário é o mais influente?
Uma das maneira de determinar quem é a pessoa mais influente em um campo, basta  descobrir os tweets que são mais retwitteados. Dê tempo suficiente para Flume  coletar os tweets do Twitter e execute a consulta a seguir:
```sql
SELECT t.retweeted_screen_name, sum(retweets) AS total_retweets, count(*) AS tweet_count 
FROM (
    SELECT retweeted_status.user.screen_name as retweeted_screen_name, retweeted_status.text, max(retweet_count) as retweets 
    FROM tweets_raw 
    GROUP BY retweeted_status.user.screen_name, retweeted_status.text
    ) t 
GROUP BY t.retweeted_screen_name 
ORDER BY total_retweets 
DESC LIMIT 10;
```
Fonte: [How-to: Analyze Twitter Data with Apache Hadoop](http://blog.cloudera.com/blog/2012/09/analyzing-twitter-data-with-hadoop/)

