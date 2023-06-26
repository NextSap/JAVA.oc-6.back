package com.paymybuddy.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.backend.object.entity.TransactionEntity;
import com.paymybuddy.backend.object.entity.UserEntity;
import com.paymybuddy.backend.object.model.TransactionModel;
import com.paymybuddy.backend.object.model.UserModel;
import com.paymybuddy.backend.repository.TransactionRepository;
import com.paymybuddy.backend.repository.UserRepository;
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
    private final UserRepository userRepository;

    public JacksonConfig(ObjectMapper objectMapper, TransactionRepository transactionRepository, UserRepository userRepository) {
        this.objectMapper = objectMapper;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    @Value("classpath:data.json")
    private Resource data;

    @Value("${feedDatabase}")
    private boolean feedDatabase;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(!feedDatabase) return;
        logger.info("Feeding database");

        Models models = objectMapper.readValue(data.getInputStream(), Models.class);

        Entities entities = map(models);

        transactionRepository.saveAll(entities.getTransactions());
        userRepository.saveAll(entities.getUsers());

        logger.info("Database feeded");
    }

    public Entities map(Models models) {
        List<TransactionEntity> transactionEntityList = new ArrayList<>();
        List<UserEntity> userEntityList = new ArrayList<>();
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

        for (UserModel user : models.users) {
            userEntityList.add(UserEntity.builder()
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .balance(user.getBalance())
                    .build());
        }

        return new Entities(transactionEntityList, userEntityList);
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Models {
        private TransactionModel[] transactions;
        private UserModel[] users;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Entities {
        private List<TransactionEntity> transactions;
        private List<UserEntity> users;
    }
}
