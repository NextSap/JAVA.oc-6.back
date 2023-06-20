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

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getTransactions(@RequestParam int page, @RequestParam int size, @RequestParam TransactionType filter, @RequestHeader(value = "Authorization", required = false) String token) {
        List<TransactionResponse> transactionResponseList = transactionService.getTransactions(page, size, token, filter);
        logger.info("Successful request GET /transaction?page={}&size={}&filter={}", page, size, filter);
        return new ResponseEntity<>(transactionResponseList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> postTransaction(@RequestParam TransactionType type, @Valid @RequestBody TransactionRequest transactionRequest, @RequestHeader(value = "Authorization", required = false) String token) {
        TransactionResponse transactionResponse = transactionService.createTransaction(transactionRequest, type, token);
        logger.info("Successful request POST /transaction");
        return new ResponseEntity<>(transactionResponse, HttpStatus.CREATED);
    }
}
