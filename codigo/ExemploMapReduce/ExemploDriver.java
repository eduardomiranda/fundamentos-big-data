package Exemplo; 

import java.util.*;


import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;


public class ExemploDriver extends Configured implements Tool {

   public int run(String[] args) throws Exception {
      // Verifica a passagem do arquivo de dados e do diretório de saída
      if (args.length != 2) {
         System.err.printf("Argumentos exigidos pela classe %s: <ArquivoEntrada> <DiretorioSaida>\n", getClass().getSimpleName());
         System.exit(1);
      }

      Job job = new Job(getConf(), "Exemplo de MapReduce");

      job.setJarByClass(ExemploDriver.class);
      job.setMapperClass(ExemploMapper.class);
      job.setReducerClass(ExemploReducer.class);

      job.setInputFormatClass(TextInputFormat.class);
      
      job.setMapOutputValueClass(Text.class);
      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(FloatWritable.class);

      // Passa o arquivo de entrada e o diretório de saída
      FileInputFormat.addInputPath(job, new Path(args[0]));
      FileOutputFormat.setOutputPath(job, new Path(args[1])); 

      // Executa a tarefa de forma síncrona
      return  job.waitForCompletion(true) ? 0 : 1;
   }

   public static void main(String[] args) throws Exception { 
      Configuration conf = new Configuration();
      System.exit(ToolRunner.run(conf, new ExemploDriver(), args));
   } 
}
