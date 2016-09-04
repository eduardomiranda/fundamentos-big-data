# Comandos HDFS

## Listando arquivos e diretórios no HDFS
```bash
$ hadoop fs -ls /user
```

## Criando um diretório para colocar o arquivo
```bash
$ hadoop fs -mkdir /user/input
```

## Transferindo arquivo do sistema local para o HDFS
```bash
$ hadoop fs -put ./center_earth  /user/input
```

## Verificando o conteúdo do arquivo texto
```bash
$  hadoop fs -cat /user/input/center_earth
```

## Transferindo arquivo do HDFS para o sistema local de arquivos
```bash
$  hadoop fs -get /user/input/center_earth ./ce
```