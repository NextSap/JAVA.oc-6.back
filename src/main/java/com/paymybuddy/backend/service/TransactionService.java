package com.paymybuddy.backend.service;

import com.paymybuddy.backend.exception.TransactionException;
import com.paymybuddy.backend.exception.UserException;
import com.paymybuddy.backend.mapper.TransactionMapper;
import com.paymybuddy.backend.object.entity.TransactionEntity;
import com.paymybuddy.backend.object.entity.UserEntity;
import com.paymybuddy.backend.object.request.TransactionRequest;
import com.paymybuddy.backend.object.response.PaginationInfoResponse;
import com.paymybuddy.backend.object.response.TransactionResponse;
import com.paymybuddy.backend.repository.TransactionRepository;
import com.paymybuddy.backend.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final TransactionMapper transactionMapper = TransactionMapper.getInstance();
    private final JwtUtils jwtUtils;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, UserService userService, JwtUtils jwtUtils) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    public TransactionResponse getTransaction(Long id) {
        String token = jwtUtils.getToken();
        String email = jwtUtils.getEmail(token, true);

        TransactionEntity transactionEntity = transactionRepository.findById(id).stream().findFirst().orElseThrow(() -> new TransactionException.TransactionNotFoundException("Transaction with id `" + id + "` not found"));

        if (!transactionEntity.getSender().getEmail().equals(email) && !transactionEntity.getReceiver().getEmail().equals(email))
            throw new UserException.BadCredentialsException("Unauthorized access to transaction with id `" + id + "`");

        return transactionMapper.toTransactionResponse(transactionEntity);
    }

    public List<TransactionResponse> getTransactions(int page, int size) {
        String token = jwtUtils.getToken();
        String email = jwtUtils.getEmail(token, true);

        if (page <= 0 || size <= 0 || size > 100)
            throw new TransactionException.TransactionInvalidPagingException("Page and size must be greater than 0 and lower than 100");

        List<TransactionEntity> transactionEntityList = transactionRepository.findAll().stream()
                .filter(transactionEntity -> (transactionEntity.getSender().getEmail().equals(email) || transactionEntity.getReceiver().getEmail().equals(email)))
                .sorted((transactionEntity1, transactionEntity2) -> transactionEntity2.getTimestamp().compareTo(transactionEntity1.getTimestamp()))
                .skip((long) (page - 1) * size).limit(size).toList();

        return transactionMapper.toTransactionResponseList(transactionEntityList);
    }

    public TransactionResponse createTransaction(TransactionRequest transactionRequest) {
        String token = jwtUtils.getToken();
        String email = jwtUtils.getEmail(token, true);
        final double fees = 0.005;

        if (BigDecimal.valueOf(transactionRequest.getAmount()).compareTo(BigDecimal.valueOf(0)) <= 0)
            throw new TransactionException.TransactionInvalidAmountException("Transaction amount must be greater than 0");

        transactionRequest.setSender(email);

        if (transactionRequest.getSender().equals(transactionRequest.getReceiver()))
            throw new TransactionException.TransactionInvalidReceiverException("Transaction sender and receiver must be different");

        UserEntity sender = userService.getUserEntityByEmail(transactionRequest.getSender());
        UserEntity receiver = userService.getUserEntityByEmail(transactionRequest.getReceiver());

        if (BigDecimal.valueOf(transactionRequest.getAmount()).compareTo(sender.getBalance()) > 0)
            throw new TransactionException.TransactionInvalidAmountException("Transaction amount must be lower than sender balance");

        BigDecimal amount = BigDecimal.valueOf(transactionRequest.getAmount());

        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount.multiply(BigDecimal.valueOf(1 - fees))));

        userService.updateUser(sender);
        userService.updateUser(receiver);

        return transactionMapper.toTransactionResponse(transactionRepository.save(transactionMapper.toTransactionEntity(transactionRequest, sender, receiver)));
    }

    public PaginationInfoResponse getPaginationInfo(int size) {
        String token = jwtUtils.getToken();
        String email = jwtUtils.getEmail(token, true);

        if (size <= 0 || size > 100)
            throw new TransactionException.TransactionInvalidPagingException("Page and size must be greater than 0 and lower than 100");

        List<TransactionEntity> transactionEntityList = transactionRepository.findAll().stream()
                .filter(transactionEntity -> (transactionEntity.getSender().getEmail().equals(email) || transactionEntity.getReceiver().getEmail().equals(email)))
                .toList();

        int totalElements = transactionEntityList.size();
        double totalPages = (double) totalElements / size;

        return PaginationInfoResponse.builder().totalPages((int) Math.ceil(totalPages)).build();
    }
}
