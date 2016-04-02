package Exemplo;

import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;

import java.io.IOException;
import java.util.StringTokenizer;

public class ExemploMapper  extends Mapper <LongWritable,Text,Text,Text> {
   
   public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
      
      // Cria um iterador assumindo que o espaço é o caracter separador dos campos
      StringTokenizer iterator = new StringTokenizer(value.toString()," ");
      
      // Obtem o ano
      String year = new String(iterator.nextToken()).toString();

      // Pula as duas colunas após o ano
      iterator.nextToken();
      iterator.nextToken();

      // Obtém o superávit ou déficit
      String delta  = new String(iterator.nextToken()).toString();
      
      context.write(new Text("summary"), new Text(year + "_" + delta));
   }
}

