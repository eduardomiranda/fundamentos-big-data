# Comandos Sqoop utilizados no exercício em aula.

## Clonando o repositório test_db
```bash
$ git clone https://github.com/datacharmer/test_db.git
$ cd teste_db
```

## Acessando MySQL Command-Line Tool
Não deve existir expaço entre -p e a senha
```bash
$ mysql -uroot -pcloudera
```

### Criação de usuário e banco no MySQL
Criando usuário arroz com senha feijao
```sql
CREATE USER 'arroz'@'localhost' IDENTIFIED BY 'feijao';
```

Apenas para efeitos didáticos, foi concedido todos os privilegios para o usuário arroz
```sql
GRANT ALL PRIVILEGES ON *.* TO 'arroz'@'localhost';
```

### Adicionando dados ao banco MySQL
```sql
SOURCE employees.sql
```

### Descrevendo a tabela employees importada
```sql
DESCRIBE employees;
	+------------+---------------+------+-----+---------+-------+
	| Field      | Type          | Null | Key | Default | Extra |
	+------------+---------------+------+-----+---------+-------+
	| emp_no     | int(11)       | NO   | PRI | NULL    |       |
	| birth_date | date          | NO   |     | NULL    |       |
	| first_name | varchar(14)   | NO   |     | NULL    |       |
	| last_name  | varchar(16)   | NO   |     | NULL    |       |
	| gender     | enum('M','F') | NO   |     | NULL    |       |
	| hire_date  | date          | NO   |     | NULL    |       |
	+------------+---------------+------+-----+---------+-------+
```

## Importando os dados do MySQL para o HDFS via Sqoop
```bash
$ sqoop import \
--connect jdbc:mysql://localhost:3306/employees \
--username arroz \
--password feijao \
--table employees
```

```bash
$ sqoop import \
--connect jdbc:mysql://localhost:3306/employees \
--username arroz \
--password feijao \
--table employees \
--columns "first_name, last_name" \
--where "emp_no > 100"
```

Opcionalmente, o usuário pode substituir os três argumentos *--table*, *--columns* e *--where* pelo argumento *--query*, onde se deve definir toda a consulta SQL.
```bash
$ sqoop import \
--connect jdbc:mysql://localhost:3306/employees \
--username arroz \
--password feijao \
--query "SELECT emp_no, first_name, last_name FROM employees WHERE \$CONDITIONS AND (emp_no > 100)" \
--target-dir /user/eduardo/employees \
--split-by emp_no
```

## Importação incremental
Importação incremental no modo *append* apenas dos registros que possuírem emp_no maior que 1000.
```bash
$ sqoop import \
--connect jdbc:mysql://localhost:3306/employees \
--username arroz \
--password feijao \
--table employees \
--incremental append \
--check-column emp_no \
--last-value 1000
```

## Importando dados para o Hive
```bash
$ sqoop import \
--connect jdbc:mysql://localhost:3306/employees \
--username arroz \
--password feijao \
--table employees \
--hive-import \
--hive-table empregados \
--hive-overwrite \
--hive-drop-import-delims \
-m 4
```

## Exportando dados do Hive para o MySQL
Acessar o MySQL para criação de uma tabela de destino dos dados.
```bash
$ mysql -uroot -pcloudera
```

Definição do banco de dados onde a tabela será criada
```sql
USE employees;
```

### Criação da tabela vazia no MySQL
```sql
CREATE TABLE export_empregados (
emp_no		INT				NOT NULL ,
Birth_date	DATE    		NOT NULL ,
first_name	VARCHAR(14)		NOT NULL ,
last_name	VARCHAR(16)		NOT NULL ,
gender		ENUM ('M','F')	NOT NULL ,   
hire_date	DATE	    	NOT NULL ,
PRIMARY KEY (emp_no)
);
```

### Exportando dados do Hive para o MySQL
```bash
$ sqoop export \
--connect jdbc:mysql://localhost:3306/employees \
--username arroz \
--password feijao \
--table export_empregados \
--export-dir /user/hive/warehouse/empregados \
--fields-terminated-by '\001' \
--lines-terminated-by '\n'
```

### Conferindo exportação
```bash
$ mysql -uroot -pcloudera
```
```sql
USE employees;
SHOW TABLES;
```