# Comandos Spark utilizados no exercício em aula.

Apache Spark é uma plataforma de computação em cluster que roda no topo de uma camada de armazenamento. Ele estende o MapReduce com suporte para mais tipos de componentes, tais como streaming e análises interativas.

Spark oferece a capacidade de executar cálculos na memória, mas também é mais eficiente que o MapReduce quando precisa processar dados armazenados no disco.  Spark processa os dados 10 vezes mais rápido que o MapReduce no disco e 100 vezes mais rápido na memória. 

Spark permite construir muito rapidamente algoritmos complexos para processamento de dados. Ele fornece suporte para muitos mais operadores que MapReduce como Joins, reduceByKey, combineByKey. Spark também fornece a capacidade de escrever programas interativamente usando o spark shell disponível para Scala e Python.

Spark é totalmente compatível com Hadoop. Ele roda com YARN e acessa os dados no HDFS, Hbase e Hive. Além disso, ele permite a utilização do Apache Mesos, um gerenciador de  recursos mais geral. 

Spark possui um framework integrado para análises avançadas como processamento de grafos, consultas avançadas, processamento de fluxo e machine learning. É possível combinar essas bibliotecas na mesma aplicação e usar uma única linguagem de programação. As aplicações podem ser desenvolvidas em Scala, um linguagem relativamente nova, Python, Java e a partir da versão 1.4.1, SparkR.


## Copiando dataset para o HDFS
```bash
$ cd fundamentos-big-data/
$ cd dados/
$ hadoop fs -mkdir -p /user/eduardo/data/
$ hadoop fs -copyFromLocal ./adult.data.csv /user/eduardo/data/
$ hadoop fs -ls /user/eduardo/data/ 
```

## Iniciando o Shell iterativo
```bash
$ spark-shell
```

## Contando o total de países distintos presentes no dataset
```scala
scala> val adultDatasetRDD = sc.textFile("/user/eduardo/data/adult.data.csv")

scala> val splitAdultDatasetRDD = adultDatasetRDD.map(linha => linha.split(","))

// 13 é o índice da coluna native-country
scala> val Paises = splitAdultDatasetRDD.map(linha => linha(13)).distinct

scala> Paises.count()
	res4: Long = 42

scala> Paises.collect()
    res5: Array[String] = Array(" El-Salvador", " Scotland", " Yugoslavia", " Canada", " Hungary", " Jamaica", " United-States", " Cambodia", " Outlying-US(Guam-USVI-etc)", " Peru", " Honduras", " Vietnam", " Hong", " Portugal", " Iran", " Japan", " Poland", " Columbia", " Taiwan", " ?", " India", " Germany", " Philippines", " Laos", " Thailand", " Haiti", " England", " Puerto-Rico", " France", " Guatemala", " Trinadad&Tobago", " Ecuador", " China", " Mexico", " Italy", " Cuba", " Nicaragua", " South", " Greece", " Holand-Netherlands", " Ireland", " Dominican-Republic")

```

As ações *count* e *collect* agora irão utilizar os dados no cache, em vez de recarregar o arquivo e executar tudo desde o início.
```scala
scala> Paises.cache()

scala> Paises.count()

scala> Paises.collect()
```


## Copiando *San Francisco Police Department Incidents* dataset para o HDFS

Este conjunto de dados contém incidentes extraídos do sistema de incidentes criminais do departamento de polícia de São Francisco no período entre janeiro de 2013 e julho de 2015.
```bash
$ cd fundamentos-big-data/
$ cd dados/
$ unzip sfpd.csv.zip
$ hadoop fs -copyFromLocal ./sfpd.csv /user/eduardo/data/
$ hadoop fs -ls /user/eduardo/data/ 
```

## Iniciando o Shell iterativo
```bash
$ spark-shell
```

## Processando o dataset
```scala
// map(_.split(","))  é equivalente a  map(linha => linha.split(","))
scala> val sfpdRDD = sc.textFile("/user/eduardo/data/sfpd.csv").map(_.split(","))

// Verificando o primeiro elemento do dataset. Esquema dos dados: 
// Número do Incidente | Categoria | Descrição | Dia da semana | Data | Hora | Distrito policial | Resolução | Endereço | Coordenada X | Coordenada Y | PDID
scala> sfpdRDD.first()

    res9: Array[String] = Array(150599321, OTHER_OFFENSES, POSSESSION_OF_BURGLARY_TOOLS, Thursday, 7/9/15, 23:45, CENTRAL, ARREST/BOOKED, JACKSON_ST/POWELL_ST, -122.4099006, 37.79561712, 15059900000000)

// Verificando os 5 primeiros elementos do dataset
scala> sfpdRDD.take( 5 )
    
// Qual o total de incidentes?
scala> sfpdRDD.count()
    res12: Long = 383775

// Qual o total de resoluções distintas?
// 7 → Posição da coluna Resolução
scala> sfpdRDD.map(inc => inc( 7 ) ).distinct.count()
    res13: Long = 17 

// Quais são as distintas resoluções?
scala> sfpdRDD.map(inc => inc( 7 ) ).distinct.collect()

    res14: Array[String] = Array(PROSECUTED_BY_OUTSIDE_AGENCY, LOCATED, NONE, PSYCHOPATHIC_CASE, JUVENILE_DIVERTED, NOT_PROSECUTED, PROSECUTED_FOR_LESSER_OFFENSE, ARREST/BOOKED, DISTRICT_ATTORNEY_REFUSES_TO_PROSECUTE, UNFOUNDED, JUVENILE_CITED, JUVENILE_ADMONISHED, ARREST/CITED, COMPLAINANT_REFUSES_TO_PROSECUTE, EXCEPTIONAL_CLEARANCE, CLEARED-CONTACT_JUVENILE_FOR_MORE_INFO, JUVENILE_BOOKED)
```

## *Pair* RDD
Existem várias maneiras de se criar um pair RDD. A forma mais comum é usar uma transformação MAP. A maneira de criar pair RDDs par é diferente para cada linguagem. Em Python e Scala precisamos retornar um RDD de tuplas.

### Quais são os 3 distritos que mais tiveram incidentes? 

```scala
scala> val incidentesPorDistrito = sfpdRDD.map(incidente => ( incidente( 6 ), 1 ) )

scala> incidentesPorDistrito.reduceByKey( ( a , b ) => a + b ).map( x => (x._2, x._1) ).sortByKey(false).take(3)


    res25: Array[(Int, String)] = Array((73308,SOUTHERN), (50164,MISSION), (46877,NORTHERN))
```
A função sortByKey() recebe como argumento o parâmetro chamado ascendente, que indica que os resultados devem ser ordenados de forma ascentente. Por padrão este argumento é definido como true. Mas como queremos os top 3, precisamos que os resultados sejam em ordem decrescente. Assim, nós passamos o valor false para a função sortByKey.


### Outra forma de obter os 3 distritos que mais tiveram incidentes? 
```scala
scala> incidentesPorDistrito.groupByKey.map( x => ( x._1, x._2.size ) ).map( x => (x._2, x._1) ).sortByKey(false).take(3)

    res26: Array[(Int, String)] = Array((73308,SOUTHERN), (50164,MISSION), (46877,NORTHERN))
```

A função groupByKey agrupa todos os valores que têm a mesma chave.


## DataFrames

Um DataFrame é uma coleção distribuída de dados organizados em colunas nomeadas. É conceitualmente equivalente a uma tabela em um banco de dados relacional, com muitas otimizações. DataFrames podem ser construídos a partir de uma ampla variedade de fontes, tais como: arquivos de dados estruturados (JSON, CSV, etc.), tabelas em Hive, bancos de dados externos, ou RDDs já existentes.

DataFrames são coleções de objetos que têm um esquema associado. As informações acerca do esquema torna possível fazer muito mais otimizações. A linguagem SQL pode ser usada para consultar os dados diretamente.

RDDs por outro lado, são coleções de objetos sem informações sobre o formato dos dados. Os dados podem ser consultados usando a transformação e ações RDD, mas não diretamente usando SQL.

## Etapas para criar um DataFrame a partir de um RDD existente

1. **Criar um SQLContext** que é o ponto inicial para a criação de um DataFrame;
2. **Importar as classes necessárias**;
    * sqlContext.implicits
3. **Criar um RDD**;
4. **Definir uma case class **. Uma case class em Scala é equivalente a uma classe Plain Old Java Objects (POJO) do Java.
5. **Converter o RDD em um RDD de objetos da classe case** usando uma transformação map para mapear a classe case em todos os elementos do RDD;
6. **Implicitamente converter o RDD resultante em um DataFrame**;
7. **Registrar o DataFrame como um tabela**.


```scala
// Um SparkContext existente.
scala> val sqlContext = new org.apache.spark.sql.SQLContext(sc)

// Usado para converter implicitamente RDD em um DataFrame.
scala> import sqlContext.implicits._

scala> val adultDatasetRDD = sc.textFile("/user/eduardo/data/adult.data.csv").map(linha => linha.split(","))

// Define o esquema
scala> case class esquemaAdult( age: Int, workclass: String, fnlwgt: Float, education: String, education_num: String, marital_status: String, occupation: String, relationship: String, race: String, sex: String, capital_gain: Float, Capital_loss: Float, hours_per_week: Int, native_country: String, income: String )

// Mapeia os dados para a classe esquemaAdult
scala> val adult = adultDatasetRDD.map(a => esquemaAdult ( a(0).toInt, a(1), a(2).toFloat, a(3), a(4), a(5), a(6), a(7), a(8), a(9), a(10).toFloat, a(11).toFloat, a(12).toInt, a(13), a(14) ) )

// Converte o RDD para DataFrame
scala> val adultDF = adult.toDF()

// Registra o DataFrame como uma tabela
scala> adultDF.registerTempTable("adultDF")

// Checar o esquema
scala> adultDF.printSchema()

// Imprimir na tela todos os dados
scala> adultDF.show()

// Número total de registros
scala> adultDF.count()

// Mostra as colunas do DataFrame
scala> adultDF.columns

// Salvando um DataFrame
scala> adultDF.write.format("json").mode("overwrite").save("/user/eduardo/data/savedDF")
```