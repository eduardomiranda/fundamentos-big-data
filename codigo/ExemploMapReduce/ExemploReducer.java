package Exemplo;

import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.FloatWritable;
import java.io.IOException;


public class ExemploReducer  extends Reducer <Text,Text,Text,FloatWritable> {

   public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

     long min=Long.MAX_VALUE;
     Text minYear=new Text();

     long tempValue = 0L;
     Text tempYear=new Text();
     String tempString;
     String[] keyString;
     
     for (Text value: values) {
         tempString = value.toString();
         keyString = tempString.split("_"); 
         tempYear = new Text(keyString[0]);
         tempValue = new Long(keyString[1]).longValue(); 

        if(tempValue < min) {
            min=tempValue;
            minYear=tempYear;
         }      
     }

     Text keyText=new Text("minimo" + "(" + minYear.toString() + "): ");

     context.write(keyText, new FloatWritable(min)); 
   }
}
