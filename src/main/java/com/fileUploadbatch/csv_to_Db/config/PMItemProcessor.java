package com.fileUploadbatch.csv_to_Db.config;

import com.fileUploadbatch.csv_to_Db.model.PMDealsDetails;
import org.springframework.batch.item.ItemProcessor;

public class PMItemProcessor implements ItemProcessor<PMDealsDetails,PMDealsDetails> {
    @Override
    public PMDealsDetails process(PMDealsDetails pmDealsDetails) throws Exception {

        return pmDealsDetails;
    }
}
