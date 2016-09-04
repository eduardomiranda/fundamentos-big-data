# Comandos Hive

## Criação de um banco de dados
```sql
CREATE DATABASE nomeDoBanco;
```

Criar um banco de dados chamado nomeDoBanco utilizando um outro local de armazenamento diferente do definido nas configurações do Hive.
```sql
CREATE DATABASE nomeDoBanco
LOCATION '/diretorio/subdiretorio';
```

Criar um banco de dados chamado nomeDoBanco adicionando um comentário descritivo do banco e algumas outras metadados.
```sql
CREATE DATABASE nomeDoBanco
COMMENT 'Banco de dados particular'
WITH DBPROPERTIES ('criadoPor' = 'Eduardo', 'Data' = '02/05/2016');
```


## Listar todos os bancos de dados no Hive

```sql
SHOW DATABASE;
```

## Obtenção de algumas informações básicas sobre o banco de dados solicitado
```sql
DESCRIBE DATABASE default;
```

## Criação de tabela
```sql
CREATE TABLE flintstones (
	id		INT,
	name	STRING
)
PARTITIONED BY (date STRING) -- Particiona os dados em função da coluna date
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '|' -- Aponta para o Hive o delimitador das colunas
STORED AS TEXTFILE;  -- Armazena os dados como um arquivo texto
```

```sql
-- Criação de uma nova tabela a partir de uma consulta a outra tabela
CREATE TABLE new_key_value_store
AS
SELECT new_key, concat(key, value) key_value_pair
FROM key_value_store
SORT BY new_key, key_value_pair;
```

 ```sql
--Criação de uma tabela externa
CREATE EXTERNAL TABLE nyse_divs_external (stock_exchange STRING, stock_symbol STRING, stock_date STRING, stock_dividends FLOAT) 
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY ',' -- Aponta para o Hive o delimitador das colunas
STORED AS TEXTFILE 
LOCATION '<hdfs_location>'; -- Aponta para o Hive a localização dos dados
```
Por se tratar de uma tabela externa (EXTERNAL TABLE), a deleção da tabela externa (DROP TABLE) apaga apenas os metadados, mas não exclui os dados do sistema de arquivos. Tabelas externas são interessantes quando deseja-se compartilhar os dados com outras ferramentas.

## Índices
Indíces aumentam a velocidade de busca de certas colunas da tabela. A velocidade aumenta com o custo de processamento e de mais espaço em disco. Os dados indexados são armazenados em uma outra tabela.

 ```sql
 -- Criação de um índice
CREATE INDEX in1 ON TABLE flintstones(id) AS 'Compact'
WITH DEFERRED REBUILD;
```
```sql 
-- Listar todos os índices da tabela
SHOW INDEX ON flintstones;
```

```sql
-- Deletar índice 
DROP INDEX in1 ON flintstones;
 ```
## Inserção de dados
```sql
INSERT INTO TABLE flintstones
PARTITION (date='01012016')
VALUES (1, 'fred flintstone' ), (2, 'wilma flintstone' ), (3, 'pedrita flintstone' );
```

Inserindo dados a partir de um select
```sql
INSERT OVERWRITE TABLE nyse PARTITION (stock_date) 
SELECT stock_exchange , stock_symbol, stock_dividends, stock_date 
FROM nyse_divs_external 
LIMIT 100;
```
Nas cláusulas no formato INSERT … SELECT, as colunas de partições devem ser especificadas por último entre as colunas da cláusula SELECT e na mesma ordem em que aparecem em PARTITION.

## Carregando dados do sistemas local de arquivos para o Hive

```sql
LOAD DATA LOCAL INPATH '/home/cloudera/datasets/infochimps'
OVERWRITE INTO TABLE nyse;
```
```sql
LOAD DATA LOCAL INPATH '/home/cloudera/datasets/infochimps'
OVERWRITE INTO TABLE nyse
PARTITION (stock_date);
```
O argumento LOCAL indica para o Hive que os dados estão no sistema de arquivos local e não no HDFS. Durante o processo de leitura, o Hive não faz nenhuma transformação nos dados. Apenas copia os dados para a localização final. O argumento OVERWRITE indica para o Hive que os dados já existentes na tabela nyse devem ser removidos antes da inserção.

## Obtendo dados do Hive para o sistemas local de arquivos
```sql
INSERT OVERWRITE LOCAL DIRECTORY '/home/eduardo/vendas'
SELECT * 
FROM vendas;
```
Se o argumento LOCAL for utilizado, o Hive irá salvar os dados no sistema de arquivo local. Caso contrário, os dados serão salvos no HDFS. Os dados por padrão são serializados como texto onde ˆA separa as colunas e uma nova linha separa os registros. Além disso, as colunas não primitivas são serializadas em formato JSON.
O argumento INSERT OVERWRITE é uma boa forma de extrair dados do Hive porque ele instrui o Hive a sobrescrever o diretório de destino (tome cuidado) e assim, o Hive consegue exportar os dados em paralelo.



## Criação de views
```sql
CREATE VIEW onion_referrers(url COMMENT 'URL of Referring page')
COMMENT 'Referrers to The Onion website'
AS
SELECT DISTINCT referrer_url
FROM page_view
WHERE page_url='http://www.theonion.com';
```
Views no Hive permitem que consultas sejam salvas e tratadas como tabelas. 
O Hive não suporta views materializadas. Elas são apenas objetos lógicos e seu esquema é congelado no momento de criação da view. Ou seja, se forem realizadas mudanças nas tabelas referenciadas na view, como por exemplo, adição ou remoção de colunas, estas alterações não são refletidas na view.

## Funções existentes no Hive 
Utilize os comandos abaixo para descobrir e entender o funcionamento das funcções presentes no Hive.
```sql
SHOW FUNCTIONS; 
abs, acos, add_months, and, array, array_contains, ascii, asin, assert_true, atan, avg, base64, between, bin, case, ceil, ceiling, coalesce, collect_list, collect_set, compute_stats
```
```sql
DESCRIBE FUNCTION upper;
upper(str) - Returns str with all characters changed to uppercase
```
```sql
DESCRIBE FUNCTION EXTENDED upper;
upper(str) - Returns str with all characters changed to uppercase
Synonyms: ucase
Example:
  > SELECT upper('Facebook') FROM src LIMIT 1;
  'FACEBOOK'
```