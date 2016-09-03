package com.fundamentosbigdata;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class DeficitSuperavitDriver extends Configured implements Tool {

    private static Configuration configuration = new Configuration();

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(configuration, new DeficitSuperavitDriver(), args));
    }

    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.printf("Usage %s: <InputPath> <OutputPath>\n", getClass().getSimpleName());
            System.exit(1);
        }

        Job job = Job.getInstance(configuration, "deficit superávit");

        job.setJarByClass(DeficitSuperavitDriver.class);
        job.setMapperClass(DeficitSuperavitMapper.class);
        job.setReducerClass(DeficitSuperavitReducer.class);

        job.setInputFormatClass(TextInputFormat.class);

        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FloatWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        return job.waitForCompletion(true) ? 0 : 1;
    }
}
