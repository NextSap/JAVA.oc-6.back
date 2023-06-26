package com.paymybuddy.backend.controller;

import com.paymybuddy.backend.object.TransactionType;
import com.paymybuddy.backend.object.request.TransactionRequest;
import com.paymybuddy.backend.object.response.TransactionResponse;
import com.paymybuddy.backend.service.TransactionService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/transaction")
public class TransactionController {

    private final Logger logger = LogManager.getLogger(TransactionController.class);
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TransactionResponse> getTransaction(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        TransactionResponse transactionResponse = transactionService.getTransaction(id);
        logger.info("Successful request GET /transaction/{}", id);
        return new ResponseEntity<>(transactionResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getTransactions(@RequestParam int page, @RequestParam int size, @RequestParam(required = false) TransactionType filter, @RequestHeader("Authorization") String token) {
        List<TransactionResponse> transactionResponseList = transactionService.getTransactions(page, size, filter);
        logger.info("Successful request GET /transaction?page={}&size={}&filter={}", page, size, filter);
        return new ResponseEntity<>(transactionResponseList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> postTransaction(@RequestParam TransactionType type, @Valid @RequestBody TransactionRequest transactionRequest, @RequestHeader("Authorization") String token) {
        TransactionResponse transactionResponse = transactionService.createTransaction(transactionRequest, type);
        logger.info("Successful request POST /transaction");
        return new ResponseEntity<>(transactionResponse, HttpStatus.CREATED);
    }
}
