package com.fundamentosbigdata;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

class DeficitSuperavitReducer extends Reducer<Text, Text, Text, FloatWritable> {

    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        long minDelta = Long.MAX_VALUE;
        Text minYear = new Text();

        for (Text value : values) {
            String[] keyArguments = value.toString().split("_");
            long delta = Long.parseLong(keyArguments[1]);

            if (delta < minDelta) {
                minDelta = delta;
                minYear = new Text(keyArguments[0]);
            }
        }

        Text reducedKey = new Text("min" + "(" + minYear.toString() + "): ");
        context.write(reducedKey, new FloatWritable(minDelta));
    }
}
