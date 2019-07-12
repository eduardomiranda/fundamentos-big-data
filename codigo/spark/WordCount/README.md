# Exemplo de Spark: WordCount

Contar a ocorrência de cada palavra em um arquivo texto.

## Compilando o projeto

```bash
$ cd fundamentos-big-data/
$ cd codigo/
$ cd spark/
$ cd WordCount/
$ mvn clean package
```

Isso irá compilar o projeto e gerar um jar em `target/sparkwordcount-0.0.1-SNAPSHOT.jar`.

## Importando o dataset para o HFS

Um dataset de exemplo está disponivel em `dados/center_earth.txt`.

Assumindo que você está dentro desta pasta:

```bash
$ hdfs dfs -mkdir -p /user/treinamento/spark/wordcount/input/
$ hdfs dfs -copyFromLocal ../fundamentos-big-data/dados/center_earth.txt /user/treinamento/spark/wordcount/input/
```

## Rodando o programa Spark

```bash
$ spark-submit   \
--class com.fundamentosbigdata.SparkWordCount   \
--master local     \
../fundamentos-big-data/codigo/spark/WordCount/target/sparkwordcount-0.0.1-SNAPSHOT.jar  \
/user/treinamento/spark/wordcount/input/center_earth.txt
```
