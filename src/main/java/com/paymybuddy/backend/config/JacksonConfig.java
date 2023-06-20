package com.paymybuddy.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.backend.object.entity.TransactionEntity;
import com.paymybuddy.backend.object.model.TransactionModel;
import com.paymybuddy.backend.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class JacksonConfig implements ApplicationRunner {

    private final Logger logger = LogManager.getLogger(JacksonConfig.class);

    private final ObjectMapper objectMapper;

    private final TransactionRepository transactionRepository;

    public JacksonConfig(ObjectMapper objectMapper, TransactionRepository transactionRepository) {
        this.objectMapper = objectMapper;
        this.transactionRepository = transactionRepository;
    }

    @Value("classpath:data.json")
    private Resource data;

    @Value("${env}")
    private String env;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(!env.equals("test")) return;

        Models models = objectMapper.readValue(data.getInputStream(), Models.class);

        Entities entities = map(models);

        transactionRepository.saveAll(entities.getTransactions());
    }

    public Entities map(Models models) {
        List<TransactionEntity> transactionEntityList = new ArrayList<>();
        double fees = 0.005;

        for(TransactionModel transactionModel : models.getTransactions()) {
            transactionEntityList.add(TransactionEntity.builder()
                    .sender(transactionModel.getSender())
                    .receiver(transactionModel.getReceiver())
                    .amount(transactionModel.getAmount())
                    .fees(transactionModel.getAmount().multiply(BigDecimal.valueOf(fees)))
                    .description(transactionModel.getDescription())
                    .timestamp(transactionModel.getTimestamp())
                    .transactionType(transactionModel.getTransactionType())
                    .build());
        }

        return new Entities(transactionEntityList);
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Models {
        private TransactionModel[] transactions;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Entities {
        private List<TransactionEntity> transactions;
    }
}
