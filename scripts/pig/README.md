# Comandos Pig utilizados no exercício em aula.

## Iniciando o grunt shell
```bash
$ pig 
```

## LOAD
Note que o arquivo de dados deve estar no HDFS. Se o caminho completo não for informado, o Pig irá buscar os dados em /user/*usuário*/.

### Leitura de dados sem a definição do esquema
```
dividends = LOAD 'NYSE_dividends';
```

### Definição explícita do esquema sem a definição dos tipos de dados
```
dividends = LOAD 'NYSE_dividends' AS (exchange, symbol, date, dividend);
```

### Definição explícita do esquema com a definição dos tipos de dados
```
dividends = LOAD 'NYSE_dividends' AS (exchange:CHARARRAY, symbol:CHARARRAY, date:CHARARRAY, dividend:FLOAT);
```

### Lendo dados a partir de uma tabela no HBase
```
divs = LOAD 'NYSE_dividends' USING HBaseStorage();
```

### Lendo dados textuais utilizando a vírgula como separador
```
divs = LOAD '/user/pigusr/NYSE_dividends' USING PigStorage(',') AS (exchange, symbol, date, dividends);
```

## STORE
### Armazenando a relação dadosProcessados no HBase
```
STORE dadosProcessados INTO 'processed' USING HBaseStorage();
```

### Armazenando a relação dadosProcessados. 
Por padrão, o Pig armazena os dados no HDFS em um arquivo delimitado por tab 
```
STORE dadosProcessados INTO '/data/examples/processed';
```
Quando salvando a relação dadosProcessados no HDFS, será criado um diretório composto por vários arquivos part-***. O número de arquivos criados depende do paralelismo da última tarefa executada antes do store.

## DUMP
DUMP direciona a saída para a tela 
```
divs  = LOAD 'NYSE_dividends' AS (exchange, symbol, date, dividends);

DUMP divs;
    (NYSE,CPO,2009-12-30,0.14)
    (NYSE,CPO,2009-09-28,0.14)
    (NYSE,CPO,2009-06-26,0.14)
    (NYSE,CPO,2009-03-27,0.14)
    (NYSE,CPO,2009-01-06,0.14)
    (NYSE,CCS,2009-10-28,0.414)
    (NYSE,CCS,2009-07-29,0.414)
    (NYSE,CCS,2009-04-29,0.414)
```

## DESCRIBE
DESCRIBE mostra o esquema e o tipo inferido do dado 
```
divs  = LOAD 'NYSE_dividends' AS (exchange, symbol, date, dividends);

DESCRIBE divs;
    divs: {exchange:BUTEARRAY, symbol:BUTEARRAY, date:BUTEARRAY, dividends:BUTEARRAY}
```

## ILLUSTRATE
O comando ILLUSTRATE imprime o esquema da relação e um exemplo do dado
```
divs  = LOAD 'NYSE_dividends' AS (exchange, symbol, date, dividends);

ILLUSTRATE divs;
```

## FOREACH e GENERATE
FOREACH pega um conjunto de expressões e aplica elas em todos os registros. O resultado de um FOREACH é uma nova tupla, normalmente com um esquema diferente do original. No exemplo abaixo, todo os campos exceto user e id são removidos
```
A = LOAD 'input' AS (user:CHARARRAY, id:LONG, address:CHARARRAY, phone:CHARARRAY, preferences:MAP[]);

B = FOREACH A GENERATE user, id;

prices = LOAD 'NYSE_daily' AS (exchange, symbol, date, open, high, low, close, volume, adj_close);
```

### Referenciando campos pelo nome
```
gain   = FOREACH prices GENERATE close - open;
```

### Referenciando campos pela sua posição, contada a partir do zero
```
gain2  = FOREACH prices GENERATE $6 - $3;
```

## Obtendo as primeiras colunas: *exchange*, *symbol*, *date* e *open*
```
beginning = FOREACH prices GENERATE ..open;
```
## Obtendo uma sequência de colunas: *open*, *high*, *low* e *close*
```
middle = FOREACH prices GENERATE open..close;
```

## Obtendo as últimas colunas: *volume* e *adj_close*
```
end = FOREACH prices GENERATE volume..; 
```

### Acessando um objeto do tipo *map*
Para extrair dados de um map utiliza-se o # seguido do nome da chave como string. Se o nome utilizado não existir então será retornado um valor null
```
bball = LOAD 'baseball' AS (name:CHARARRAY, team:CHARARRAY, position:bag{t:(p:CHARARRAY)}, bat:MAP[]);

avg = FOREACH bball GENERATE bat#'batting_average';
```

### Acessando um objeto do tipo *tuple*
Para extrair dados de uma tupla utiliza-se o ponto ( . ) seguido do nome do campo ou posição
```
A = LOAD 'input' AS (t:tuple(x:int, y:int));
B = FOREACH A GENERATE t.x, t.$1;
```

### Inferência de tipos
Pig consegue inferir os tipos de dados de um novo esquema mas nem sempre consegue inferir o nome. Quando não ocorre nenhuma operação sobre o campo, o Pig mantém o mesmo nome.
```
divs = LOAD 'NYSE_dividends' AS (exchange:CHARARRAY, symbol:CHARARRAY, date:CHARARRAY, dividends:FLOAT);

sym  = FOREACH divs GENERATE symbol;

DESCRIBE sym;
     sym: {symbol: chararray}
```

No entanto, se houver alguma operação sobre o campo e não for explicitamente definido um nome para ele, o campo não terá nome e poderá ser acessado apenas pela posição.
```
in_cents = FOREACH divs GENERATE dividends * 10.0 AS dividend, dividends * 10.0; 

DESCRIBE in_cents;
    in_cents: {dividend: double,double}
```

## FILTER
O Pig aceita os operadores de comparação  ==  !=  >  >=  <  <=. O exemplo abaixo filtra todos os registros que possuem devidents maiores que 0.5 . 
```
divs = LOAD 'NYSE_dividends' AS (exchange:CHARARRAY, symbol:CHARARRAY, date:CHARARRAY, dividends:FLOAT);

filtro = FILTER divs BY dividend > 0.5;
```

Além disso, é possível ainda utilizar expressões regulares
```
startswithcm = FILTER divs BY symbol matches 'CM.*';
```

## GROUP BY
O exemplo abaixo agrupa os registros pela chave *stock* e depois conta o total de registros por chave. A região resultante do GROUP BY possui dois campos: a chave e uma bag de registros. A chave é nomeada group. Enquanto a bag terá o nome da região agrupada, no caso *daily*. GROUP é um operador que geralmente força uma fase de redução. Ela reúne todos os registros que possuem a mesma chave.
```
daily = LOAD 'NYSE_daily' AS (exchange, stock);

grpd = GROUP daily BY stock;

cnt = FOREACH grpd GENERATE group, COUNT(daily);
```

## ILLUSTRATE
O comando ILLUSTRATE imprime na tela o esquema da nova relação e um exemplo do dado, além da relação que originou a relação sendo ilustrada.
```
ILLUSTRATE cnt
---------------------------------------------------------
| daily	| exchange:bytearray    	| stock:bytearray	| 
---------------------------------------------------------
|   	| NYSE		            	| CII		        | 
|	    | NYSE		            	| CII	            | 
---------------------------------------------------------
-------------------------------------------------------------------------------------
| grpd	| group:bytearray	| daily:bag{:tuple(exchange:bytearray,stock:bytearray)}	| 
-------------------------------------------------------------------------------------
|   	| CII	        	| {}				                        			| 
|   	| CII			    | {}				                        			| 
-------------------------------------------------------------------------------------
-------------------------------------
| cnt	| group:bytearray	| :long	| 
-------------------------------------
|	    | CII		    	| 2     | 
-------------------------------------
```

## ORDER BY
O comando ORDER BY cria uma ordenação total dos dados de saida. Quando os dados forem armazenados no HDFS, onde cada partição se tornará um arquivo part- diferente, o dado presente no arquivo part-x é sempre menor na ordem, que os dados presente em part-(x+1). ```
```
daily = LOAD 'NYSE_daily' AS (exchange:CHARARRAY, symbol:CHARARRAY, date:CHARARRAY, open:FLOAT, high:FLOAT, low:FLOAT, close:FLOAT, volume:INT, adj_close:FLOAT);

bydate = ORDER daily BY date;

bydatensymbol  = ORDER daily BY date, symbol;

byclose  = ORDER daily BY close DESC, open; -- ordem descendente
```

## JOIN BY
```
daily = LOAD 'NYSE_daily' AS (exchange, symbol, date, open, high, low, close, volume, adj_close);

divs  = LOAD 'NYSE_dividends' AS (exchange, symbol, date, dividends);
```
Em um JOIN, quando as chaves são iguais, as duas linhas são unificadas.
```
jnd = JOIN daily BY symbol, divs BY symbol;
jnd = JOIN daily BY (symbol, date), divs BY (symbol, date);
```
```
DESCRIBE jnd;
    jnd: {daily::exchange: bytearray, daily::symbol: bytearray, daily::date: bytearray, daily::open: bytearray, daily::high: bytearray, daily::low: bytearray, daily::close: bytearray, daily::volume: bytearray, daily::adj_close: bytearray, divs::exchange: bytearray, divs::symbol: bytearray, divs::date: bytearray, divs::dividends: bytearray}
```

## LIMIT e SAMPLE
```
divs  = LOAD 'NYSE_dividends';
```
### Seleciona no máximo 10 linhas. 
A ordem não é garantida. a menos que seja utilizado ORDER exatamente antes de LIMIT.
```
first10  = LIMIT divs 10; 
```

### Obtem 10% aleatórios de divs
```
some  = SAMPLE divs 0.1;
```

## PARALLEL
A cláusula PARALLEL pode ser adicionada a qualquer operador relacional. No entanto, ele controla apenas o paralelismo nos reducers. Assim, ele só faz sentido para operadores que forçam uma fase de redução: GROUP, ORDER, DISTINCT, JOIN, LIMIT, COGROUP e CROSS. GROUP, JOIN e COGROUP possuem várias implementações e nem todas forçam um reduce.
```
daily = LOAD 'NYSE_daily' AS (exchange, symbol, date, open, high, low, close, volume, adj_close);

bysymbl = GROUP daily BY symbol PARALLEL 10;

average = FOREACH bysymbl GENERATE group, AVG(daily.close) AS avg;

sorted  = ORDER average BY avg DESC PARALLEL 2;
```

## Operador ternário
Retorna 1 
```
2 == 2 ? 1 : 4 
```
Retorna 4 
```
2 == 3 ? 1 : 4 
```

Retorna null
```
NULL == 2 ? 1 : 4
```

Erro de tipo. Ambos os valores devem ser do mesmo tipo
```
2 == 2 ? 1 : 'fred' 
```
