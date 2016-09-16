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
$ hadoop fs -mkdir -p /user/wordcount/input
$ hadoop fs -put ./center_earth.txt /user/wordcount/input
```

## Rodando o programa Spark

```bash
$ spark-submit --class com.fundamentosbigdata.SparkWordCount  --master local  target/sparkwordcount-0.0.1-SNAPSHOT.jar  /user/wordcount/input
```
