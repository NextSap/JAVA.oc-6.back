package com.paymybuddy.backend.service;

import com.paymybuddy.backend.exception.TransactionException;
import com.paymybuddy.backend.mapper.TransactionMapper;
import com.paymybuddy.backend.object.TransactionType;
import com.paymybuddy.backend.object.entity.TransactionEntity;
import com.paymybuddy.backend.object.request.TransactionRequest;
import com.paymybuddy.backend.object.response.TransactionResponse;
import com.paymybuddy.backend.repository.TransactionRepository;
import com.paymybuddy.backend.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper = TransactionMapper.getInstance();
    private final JWTUtils jwtUtils = JWTUtils.getInstance();

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<TransactionResponse> getTransactions(int page, int size, String token, TransactionType filter) {
        String email = jwtUtils.verify(token);

        if(page <= 0 || size <= 0)
            throw new TransactionException.TransactionInvalidPagingException("Page and size must be greater than 0");

        List<TransactionEntity> transactionEntityList = transactionRepository.findAll(Pageable.ofSize(size).withPage(page-1)).stream().filter(transactionEntity -> (transactionEntity.getSender().equals(email) || transactionEntity.getReceiver().equals(email)) && transactionEntity.getTransactionType().equals(TransactionType.valueOf(filter.name()))).toList();

        if (transactionEntityList.isEmpty())
            throw new TransactionException.TransactionNotFoundException("No transaction found for user `" + email + "`");

        return transactionMapper.toTransactionResponseList(transactionEntityList);
    }

    public TransactionResponse createTransaction(TransactionRequest transactionRequest, TransactionType type, String token) {
        String email = jwtUtils.verify(token);
        String platformName = "PayMyBuddy";

        if(transactionRequest.getAmount().compareTo(BigDecimal.valueOf(0)) <= 0)
            throw new TransactionException.TransactionInvalidAmountException("Transaction amount must be greater than 0");

        transactionRequest.setSender(email);

        TransactionEntity transactionEntity = transactionMapper.toTransactionEntity(transactionRequest, type);

        switch (type) {
            case DEPOSIT -> {
                transactionEntity.setReceiver(email);
                transactionEntity.setSender(platformName);
                transactionEntity.setFees(BigDecimal.valueOf(0));
            }
            case WITHDRAWAL -> {
                transactionEntity.setReceiver(platformName);
                transactionEntity.setSender(email);
                transactionEntity.setFees(BigDecimal.valueOf(0));
            }
            case TRANSFER -> transactionEntity.setSender(email);
        }

        return transactionMapper.toTransactionResponse(transactionRepository.save(transactionEntity));
    }
}
