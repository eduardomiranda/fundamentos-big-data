# Exemplo de MapReduce: DeficitSuperavit

## Requisitos

- JDK 1.7+
- Maven 3+

## Compilando o projeto

```bash
$ mvn clean package
```

Isso irá compilar o projeto e gerar um jar em `target/DeficitSuperavit-1.0-SNAPSHOT.jar`.

## Importando o dataset para o HDFS

Um dataset de exemplo está disponivel em `dados/SummaryOfReceiptsOutlaysSurplusesDeficits.txt`.

Assumindo que você está dentro desta pasta:

```bash
$ hadoop fs -mkdir -p /user/deficitsuperavit/input
$ hadoop fs -put ../../dados/SummaryOfReceiptsOutlaysSurplusesDeficits.txt /user/deficitsuperavit/input
```

## Rodando a tarefa de MapReduce

Se você já rodou um MapReduce anteriormente, é necessário remover o diretório de saída, conforme mostra o comando abaixo. Se esta é a sua primeira vez, ignore esta etapa.

```bash
$ hadoop fs -rm -r /user/deficitsuperavit/output
```

## Lendo o resultado do MapReduce

Após ter compilado o projeto e copiado o dataset para o HDFS:

```bash
$ hadoop jar target/DeficitSuperavit-1.0-SNAPSHOT.jar com.fundamentosbigdata.DeficitSuperavitDriver /user/deficitsuperavit/input /user/deficitsuperavit/output
```

Após a execução da tarefa, o resultado estará disponível em `/user/deficitsuperavit/output`. Você pode listar os arquivos gerados usando:

```bash
$ hadoop fs -ls /user/deficitsuperavit/output
```

Você pode também exibir o conteúdo de um arquivo usando, por exemplo:

```bash
$ hadoop fs -cat /user/deficitsuperavit/output/part-r-00000
```
