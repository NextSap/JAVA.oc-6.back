package com.paymybuddy.backend.controller;

import com.paymybuddy.backend.object.request.TransactionRequest;
import com.paymybuddy.backend.object.response.PaginationInfoResponse;
import com.paymybuddy.backend.object.response.TransactionResponse;
import com.paymybuddy.backend.service.TransactionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Transaction", description = "Transaction management")
@RequestMapping(value = "/transaction")
public class TransactionController {

    private final Logger logger = LogManager.getLogger(TransactionController.class);
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping(value = "/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<TransactionResponse> getTransaction(@PathVariable Long id) {
        TransactionResponse transactionResponse = transactionService.getTransaction(id);
        logger.info("Successful request GET /transaction/{}", id);
        return new ResponseEntity<>(transactionResponse, HttpStatus.OK);
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<TransactionResponse>> getTransactions(@RequestParam int page, @RequestParam int size) {
        List<TransactionResponse> transactionResponseList = transactionService.getTransactions(page, size);
        logger.info("Successful request GET /transaction?page={}&size={}", page, size);
        return new ResponseEntity<>(transactionResponseList, HttpStatus.OK);
    }

    @GetMapping(value = "/paginationInfo")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<PaginationInfoResponse> getPaginationInfo(@RequestParam int size) {
        PaginationInfoResponse paginationInfo = transactionService.getPaginationInfo(size);
        logger.info("Successful request GET /transaction/paginationInfo?size={}", size);
        return new ResponseEntity<>(paginationInfo, HttpStatus.OK);
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<TransactionResponse> postTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {
        TransactionResponse transactionResponse = transactionService.createTransaction(transactionRequest);
        logger.info("Successful request POST /transaction");
        return new ResponseEntity<>(transactionResponse, HttpStatus.CREATED);
    }
}
