# Comandos HBase utilizados no exercício em aula.
O objetivo do exercício é criar no HBase a tabela clientes com as famílias de colunas *endereco* e *pedido* com as seguintes colunas:

* endereco
    * cidade
    * estado
* pedido
    * data
    * nitem

## Iniciar o HBase Shell
```bash
$ hbase shell
```

## Criando uma tabela no diretório home
```
create '/user/hbase/clientes', {NAME=>'endereco'}, {NAME=>'pedido'}
```

## Obtendo uma descrição da tabela criada
```
describe '/user/hbase/clientes'
````

## Inserindo dados
```
put '/user/hbase/clientes', 'emiranda', 'endereco:cidade', 'Belo Horizonte'
put '/user/hbase/clientes', 'emiranda', 'endereco:estado', 'Minas Gerais'
put '/user/hbase/clientes', 'emiranda', 'pedido:data', '2/2/2016'
put '/user/hbase/clientes', 'emiranda', 'pedido:nitem', '1234D'
```

## Consultas
```
get '/user/hbase/clientes', 'emiranda'

COLUMN		        CELL
endereco:cidade		timestamp=1455857085164,    value=Belo Horizonte
endereco:estado		timestamp=1455857426625,    value=Minas Gerais
pedido:NITEM		timestamp=1455857602978,    value=1234D
pedido:data		    timestamp=1455857548664,    value=2/2/2016
```

### Refinando a consulta para a família *endereco*
```
get '/user/hbase/clientes', 'emiranda',{COLUMNS=>['endereco']}

COLUMN	        	CELL
endereco:cidade		timestamp=1455857085164,    value=Belo Horizonte
endereco:estado		timestamp=1455857426625,    value=Minas Gerais
```

### Refinando a consulta para a coluna *cidade* da família *endereco*
```
get '/user/hbase/clientes', 'emiranda',{COLUMNS=>['endereco:cidade']}

COLUMN		        CELL
endereco:cidade		timestamp=1455857085164,    value=Belo Horizonte
```

### Consultando as 5 últimas versões dos números dos itens dos pedidos do id *emiranda*
```
get '/user/hbase/clientes', 'emiranda',{COLUMNS=>['pedido:nitem'], VERSIONS=>5}

COLUMN		        CELL
pedido:nitem		timestamp=1455860777406,    value=1234W
pedido:nitem		timestamp=1455860754429,    value=1234J
pedido:nitem		timestamp=1455860721943,    value=1234G
pedido:nitem		timestamp=1455860659572,    value=1234E
pedido:nitem		timestamp=1455860659572,    value=1234D
```

### Consultando todas as linhas e todas as colunas da tabela
```
scan '/user/hbase/clientes'
```

### Consultando todas as linhas para a família de coluna *endereco*
```
scan '/user/hbase/clientes', {COLUMNS=>['endereco']}
```

### Consultando todas as linhas para todas as 5 versões do número do item
```
scan '/user/hbase/clientes', {COLUMNS=>['pedido:nitem'], VERSIONS=>5}
```

### Consultando as linhas cuja chave inicia com *'emi'* na família de coluna *endereco*
```
scan '/user/hbase/clientes', {STARTROW=>'emi', COLUMNS=>['endereco']}
```

### Consultando o número de registros na tabela
```
count '/user/hbase/clientes'
```

## Alteração da tabela
Alterando a tabela para armazenar mais versões para a família de coluna pedido.
```
alter '/user/hbase/clientes', NAME=>'pedido', VERSIONS=>5
```

##  Obtendo uma descrição da tabela
```
describe '/user/hbase/clientes'
```

## Deleção

### Deletando uma coluna
```
delete '/user/hbase/clientes', 'emiranda', 'endereco:cidade'
```

### Deletando uma família de coluna
```
delete '/user/hbase/clientes', 'emiranda', 'endereco'
```
### Deletando uma linha
```
deleteall '/user/hbase/clientes', 'emiranda'
```