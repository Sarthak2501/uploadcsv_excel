package com.fileUploadbatch.csv_to_Db.config;


import com.fileUploadbatch.csv_to_Db.model.PMDealsDetails;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

//import static sun.jvm.hotspot.runtime.PerfMemory.end;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<PMDealsDetails> reader(){
      FlatFileItemReader<PMDealsDetails> reader = new FlatFileItemReader<>();

      reader.setResource(new ClassPathResource("PM Deal Details.csv"));

      reader.setLineMapper(getLineMapper());

      reader.setLinesToSkip(1);

      return reader;
    }

    private LineMapper<PMDealsDetails> getLineMapper(){

        DefaultLineMapper<PMDealsDetails> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames(new String[] {"PM Deal Id", "Game ID","Area ID","PMS Purchased"});
        lineTokenizer.setIncludedFields(new int[] {0,1,2,3});

        BeanWrapperFieldSetMapper<PMDealsDetails> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(PMDealsDetails.class);

        lineMapper.setLineTokenizer(lineTokenizer);

        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;

    }

    @Bean
    public PMItemProcessor processor(){
        return new PMItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<PMDealsDetails> writer(){
        JdbcBatchItemWriter<PMDealsDetails> wri = new JdbcBatchItemWriter<>();
        wri.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        wri.setSql("insert into pm_deals_details(area_id,game_id,pm_deal_id,pms_purchased) values(:pMDealId,:gameId,:areaId,:pMSPurchased)");
        wri.setDataSource(this.dataSource);
        return wri;

    }

    @Bean
    public Job importPMJob(){
       return this.jobBuilderFactory.get("PM-IMPORT-JOB")
               .incrementer(new RunIdIncrementer())
               .flow(step1())
                .end()
               .build();
    }

    @Bean
    public Step step1(){
        return this.stepBuilderFactory.get("step1")
                .<PMDealsDetails,PMDealsDetails>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
}
