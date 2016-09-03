# Exemplo de MapReduce: WordCount

Conta a ocorrência de palavras em um arquivo texto.

## Requisitos

- JDK 1.7+
- Maven 3+

## Compilando o projeto

```bash
$ mvn clean package
```

Isso irá compilar o projeto e gerar um jar em `target/WordCount-1.0-SNAPSHOT.jar`.

## Importando o dataset para o HFS

Um dataset de exemplo está disponivel em `dados/center_earth.txt`.

Assumindo que você está dentro desta pasta:

```bash
$ hadoop fs -mkdir -p /user/wordcount/input
$ hadoop fs -put ../../dados/center_earth.txt /user/wordcount/input
```

## Rodando a tarefa de MapReduce

Se você já rodou um MapReduce anteriormente, é necessário remover o diretório de saída, conforme mostra o comando abaixo. Se esta é a sua primeira vez, ignore esta etapa.

```bash
$ hadoop fs -rm -r /user/wordcount/output
```

## Lendo o resultado do MapReduce

Após ter compilado o projeto e copiado o dataset para o HFS:

```bash
$ hadoop jar target/WordCount-1.0-SNAPSHOT.jar com.fundamentosbigdata.WordCount /user/wordcount/input /user/wordcount/output
```

Após a execução da tarefa, o resultado estará disponível em `/user/wordcount/output`. Você pode listar os arquivos gerados usando:

```bash
$ hadoop fs -ls /user/wordcount/output
```

Você pode também exibir o conteúdo de um arquivo usando, por exemplo:

```bash
$ hadoop fs -cat /user/wordcount/output/part-r-00000
```
