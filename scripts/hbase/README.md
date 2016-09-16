# Comandos HBase utilizados no exercício em aula.
O objetivo do exercício é criar no HBase a tabela *clientes* com as famílias de colunas *endereco* e *pedido* com as seguintes colunas:
 
endereco | pedido
---------|--------
cidade   | data
estado   | nitem

Chave da linha | cidade | estado | data | nitem
---------------|--------|--------|--------|--------
emiranda | Belo Horizonte | Minas Gerais | 2/2/2016 | 1234D
abarcelos | Manaus | Amazonas | 10/3/2016 | 8547S

Fonte do exercício: [Getting Started with HBase Shell](https://www.mapr.com/products/mapr-sandbox-hadoop/tutorials/tutorial-getting-started-with-hbase-shell)

## HBase shell
Uma tabela HBase pode ser criada através do HBase shell ou da API Java. O HBase shell é um script ruby que auxilia na manipulação do HBase através da linha de comando. Os comandos estão categorizados em 6 grupos principais:

* Comandos gerais
* Gestão das tabelas
* Manipulação de dados
* Ferramentas de cirurgia
* Replicação de cluster
* Ferramentas de segurança

## Iniciar o HBase shell
```bash
$ hbase shell
```

## Criando uma tabela no diretório home
As familias de colunas devem ser definidas antes da inserção dos dados. Mas as colunas podem ser adicionadas dinamicamente.

```bash
> create 'clientes', {NAME => 'endereco'}, {NAME => 'pedido'}
```

## Obtendo uma descrição da tabela
```bash
> describe 'clientes'
```

## Inserindo dados na tabela
```bash
> put 'clientes', 'emiranda', 'endereco:cidade', 'Belo Horizonte'
> put 'clientes', 'emiranda', 'endereco:estado', 'Minas Gerais'
> put 'clientes', 'emiranda', 'pedido:data', '2/2/2016'
> put 'clientes', 'emiranda', 'pedido:nitem', '1234D'
```

## Consultando a tabela 
### Para o id *emiranda*
```bash
> get 'clientes', 'emiranda'
```

### Refinando a consulta para a família *endereco*
```bash
> get 'clientes', 'emiranda',{COLUMNS=>['endereco']}
```

### Refinando a consulta para a coluna *cidade* da família *endereco*
```bash
> get 'clientes', 'emiranda',{COLUMNS=>['endereco:cidade']}
```

### Consultando os números dos itens dos pedidos do id *emiranda*
```bash
> get 'clientes', 'emiranda',{COLUMNS=>['pedido:nitem']}
```

### Consultando todas as versões dos números dos itens dos pedidos do id emiranda
```bash
> get 'clientes', 'emiranda',{COLUMNS=>['pedido:nitem'], VERSIONS=>5}
```

### Consultando todas as linhas e todas as colunas
```bash
> scan 'clientes'
```

### Consultando todas as linhas para a família de coluna *endereco*
```bash
> scan 'clientes', {COLUMNS=>['endereco']}
```

### Consultando todas as linhas para todas as 5 versões do número do item
```bash
> scan 'clientes', {COLUMNS=>['pedido:nitem'], VERSIONS=>5}
```

### Consultando as linhas cuja chave inicia com 'emi' e família de coluna *endereco*
```bash
> scan 'clientes', {STARTROW=>'emi', COLUMNS=>['endereco']}
```

## Alterando a tabela 

### Para armazenar mais versões para a família de coluna *pedido*
```bash
> alter 'clientes', NAME=>'pedido', VERSIONS=>5
```

## Count

### Contando o número de registros na tabela
```bash
> count 'clientes'
```

## Delete

### Deletando uma coluna
```bash
> delete 'clientes', 'emiranda', 'endereco:cidade'
```

### Deletando uma família de coluna
```bash
> delete 'clientes', 'emiranda', 'endereco'
```

### Deletando uma linha
```bash
> deleteall 'clientes', 'emiranda'
```
