# Comandos HDFS

## Listar arquivos e diretórios no HDFS
Lista o conteúdo do diretório especificado pelo caminho, mostrando o nome, as permissões, o proprietário, o tamanho e a data de modificação de cada entrada.
```bash
$ hadoop fs -ls <diretório>
```
Opções:
* **-d**: Diretórios são listados como arquivos simples.
* **-h**: Formata o tamanhos dos arquivo em uma forma legível (por exemplo: 64.0m em vez de 67.108.864).
* **-R**: Recursivamente lista os subdiretórios encontrado.

## Criação de diretórios
Cria um caminho de diretório nomeado no HDFS. 
```bash
$ hadoop fs -mkdir <diretório>
```
Opções:
* **-p**: Cria qualquer diretório pai no caminho que ainda não foram criados.


## Copiar arquivo do sistema local para o HDFS
Copia o arquivo ou diretório do sistema de arquivos local para o destino dentro do HDFS.

```bash
$ hadoop fs -put <origem> <destino>
```

```bash
$ hadoop fs -copyFromLocal <origem> <destino>
```
**copyFromLocal** é semelhante ao comando **put**, exceto pelo fato de que a **origem** é restrita a uma arquivo local.

Opções:
* **-f**: Sobrescreve o destino caso ele já exista.


## Copiar arquivo do HDFS para o sistema local de arquivos
Copia o arquivo ou diretório no HDFS identificadas por origem para o caminho do sistema de arquivos local identificado por destino. Uma verificação cíclica de redundância (CRC) é um código de detecção de erro utilizada em redes digitais e dispositivos de armazenamento para detectar alterações acidentais dados brutos.
```bash
$  hadoop fs -get [-crc] <origem> <destino>
```

```bash
$  hadoop fs -copyToLocal <origem> <destino>
```
**copyToLocal** é semelhante ao comando **get**, exceto pelo fato de que o destino é restrita a referência a uma arquivo local.


## Copiar o arquivo ou diretório no HDFS
```bash
$  hadoop fs -cp <origem> <destino>
```
pções:
* **-f**: Sobrescreve o destino caso ele já exista.
* **-p**: Preserva os atributos de arquivo (data e hora, proprietário, permissões, ACL, Xattr). Se -p é especificado sem arg, preserva-se  data e hora, proprietário e as permissões, Se -pa for especificado, preserva-se a permissão também porque ACL é um super-conjunto de permissão.


## Mover arquivo do sistema local para o HDFS
Copia o arquivo ou diretório do sistema de arquivos local identificado por **origem** para o **destino** dentro HDFS. Em seguida, exclui a cópia local em caso de sucesso.
```bash
$ hadoop fs -moveFromLocal <origem> <destino>
```

## Mover arquivo ou diretório do HDFS para o sistema local
```bash
$  hadoop fs -mv <origem> <destino>
```

## Exibir o conteúdo do arquivo na saída padrão
```bash
$  hadoop fs -cat <arquivo>
```


## Remover o arquivo ou pasta vazia
```bash
$  hadoop fs -cp rm <diretório>
```
Opções:
* **-f**: Não exibirá uma mensagem de diagnóstico ou modificará o status de saída para refletir um erro se o arquivo não existe.
* **-r** ou **-R**: Exclui o diretório e qualquer conteúdo sob ele de forma recursiva.
* **-skipTrash**: Irá ignorar a lixeira, se habilitado, e apagar o(s) arquivo(s) especificados imediatamente. 


## Concatenar em um arquivo único todos os arquivos presentes em um diretório no HDFS
Recupera todos os arquivos presentes no diretório origem no HDFS, e os copia para um arquivo único, resultante da concatenação no sistema de arquivos local identificado por destino. Opcionalmente -nl pode ser utilizado para adicionar uma nova linha no final de cada arquivo.
```bash
$  hadoop fs -getmerge <origem> <destino>
```

## Exibir o uso do disco, em bytes, para todos os arquivos que casam com o diretório especificado
```bash
$  hadoop fs -du <diretório>
```
Opções:
* **-s**: Resumo agregado do tamanho dos arquivos que estão sendo exibidos, em vez dos arquivos individuais.
* **-h**: Formata o tamanhos dos arquivo em uma forma legível (por exemplo: 64.0m em vez de 67.108.864).


## Alterar o fator de replicação de um arquivo
Se o caminho é um diretório, o comando de forma recursiva alterar o fator de replicação de todos os arquivos sob a árvore de diretório raiz diretório.
```bash
$  hadoop fs -setrep [-R] [-w] <número réplicas> <diretório>
```
Opções:
* **-w**: Pede para que o comando espere até as replicação se complete
* **-R**: A opção -R é aceito para compatibilidade com versões anteriores. Não tem nenhum efeito.


## Criar um arquivo de tamanho zero que contém o timestamp
```bash
$  hadoop fs -touchz <diretório>
```

## Verificações
```bash
$  hadoop fs -test <diretório>
```
Opções:
* **-d**: Se o caminho for um diretório, então retorna 0.
* **-e**: Se o caminho existir, então retorna 0.
* **-f**: Se o caminho for um arquivo, então retorna 0.
* **-s**: Se o caminho não estiver vazio, então retorna 0.
* **-z**: Se o caminho for um arquivo de tamanho zero, então retorna 0.


## Imprimir informações sobre o caminho (arquivo ou diretório)
```bash
$  hadoop fs -stat [formato] <caminho>
```
Formatos: 
* **%F**: Tipo.
* **%g**: Nome do grupo do prorietário.
* **%n**: Nome do arquivo.
* **%r**: Fator de replicação.
* **%u**: Nome do proprietário.
* **%y**, **%Y**: Data de modificação. **%y** mostra a data no formato UTC “yyyy-MM-dd HH:mm:ss” e **%Y** mostra o total de milisegundos desde o dia 01 de Janeiro de 1970. Se o formato não for especificado, **%y** é utilizado como padrão.

Exemplo: 
```bash
$  hadoop fs -hadoop fs -stat "%F %u:%g %b %y %n" <arquivo>
```

## Mostra o último 1 KB do arquivo no stdout
```bash
$  hadoop fs -hadoop fs -tail <arquivo>
```
Opção:
* **-f**: Mostra dados adicionados caso o arquivo cresça.


## Alterar as permissões do arquivos
O usuário deve ser o proprietário do arquivo, ou então um super-usuário.
```bash
$  hadoop fs -hadoop fs -chmod [-R], modo, ...  <diretório> ...
```
Opções:
* **-R**: Faz a mudança de forma recursiva através da estrutura de diretórios. 


## Alterar o proprietário do diretório
```bash
$  hadoop fs -hadoop fs -chown [-R] [proprietário] [:[grupo]] <diretório> ...
```
Opções:
* **-R**: Faz a mudança de forma recursiva através da estrutura de diretórios. 

