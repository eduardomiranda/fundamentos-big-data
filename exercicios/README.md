# Exercícios extra

## Movielens
Importe os dados do dataset movielens (dados/ml-10m.zip) para o HDFS e responda as seguntes questões usando os seus conhecimentos em MapReduce, Pig, Hive e Spark.

### Questões

* Quantos filmes, avaliações e tags existem no dataset movielens?
* Quantos usuários únicos e filmes únicos existem no dataset movielens?
* Qual foi o usuário que avaliou o maior número de filmes?
* Qual foi o usuário que avaliou o menor número de filmes?
* Encontre os 50 filmes que tiveram mais avaliações.
* Qual é a média das avaliações?
* Qual o ano e nome do filme mais velho?
* Escreva um consulta Hive, um script Pig, um programa MapReduce ou um programa Spark para selecionar apenas os ratings acima de 4.
* Obtenha a média de avaliações por filme.
* Qual a média de avaliação do filme Shawshank Redemption?
* Obtenha 50 filmes com as melhores médias de avaliação.
* Pegue o resultado da questão anterior e importe no MySQL usando o Sqoop.
* Escreva um programa MapReduce para obter os 5 gêneros mais comuns.


## Airline on-time performance

As questões deste exercício foram retiradas da página [Airline on-time performance](http://stat-computing.org/dataexpo/2009/) que se trata de um *dataset* com detalhes de chegada e partida de todos os voos comerciais nos EUA a partir de outubro de 1987 a abril de 2008. Este é um grande conjunto de dados com cerca de 120 milhões de registros no total. Os dados e sua descrição podem ser acessados em [link](http://stat-computing.org/dataexpo/2009/the-data.html).

### Questões
* Quais são as melhores companhias e aeroportos mais pontuais?
* Quando é a melhor hora do dia / dia da semana / época do ano para voar de modo a minimizar os atrasos?
* Os aviões mais velhos sofrem mais atrasos?
* Como o número de pessoas voando entre locais diferentes mudou ao longo do tempo?
