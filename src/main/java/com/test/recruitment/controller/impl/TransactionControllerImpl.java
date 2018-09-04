package com.test.recruitment.controller.impl;

import com.test.recruitment.json.SuccessResponse;
import com.test.recruitment.json.request.CreateTransaction;
import com.test.recruitment.json.request.UpdateTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.test.recruitment.controller.TransactionController;
import com.test.recruitment.json.TransactionResponse;
import com.test.recruitment.service.TransactionService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Implementation of {@link TransactionController}
 * 
 * @author A525125
 */
@Slf4j
@RestController
public class TransactionControllerImpl implements TransactionController {

    private TransactionService transactionService;

	@Autowired
	public TransactionControllerImpl(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@Override
	public ResponseEntity<Page<TransactionResponse>> getTransactionsByAccount(
			@PathVariable("accountId") String accountId,
			@PageableDefault Pageable p) {
		Page<TransactionResponse> page = transactionService
				.getTransactionsByAccount(accountId, p);
		if (null == page || page.getTotalElements() == 0) {
			log.debug("Cannot find transaction for account {}", accountId);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		return ResponseEntity.ok().body(page);
	}

    @Override
    // TODO add security @PreAuthorize("hasRole('ROLE_ADMIN')") after integration with Spring Security(for example)
    public ResponseEntity<Void> delete(@PathVariable("accountId") String accountId,
                                       @PathVariable("transactionId") String transactionId) {
        transactionService.deleteById(accountId, transactionId);

        return ResponseEntity.noContent().build();
    }

    @Override
    // TODO add security @PreAuthorize("hasRole('ROLE_ADMIN')") after integration with Spring Security(for example)
    public ResponseEntity<SuccessResponse> addTransactionIntoAccount(@RequestBody CreateTransaction createTransaction,
                                                                     @PathVariable("accountId") String accountId) {
        String transactionId = transactionService.addTransactionIntoAccount(createTransaction, accountId);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(transactionId).toUri();

        log.debug("Transaction with id {} has been added into account {}", transactionId, accountId);

        return ResponseEntity.created(location).body(new SuccessResponse("Transaction has been added successfully"));
    }

    @Override
    // TODO add security @PreAuthorize("hasRole('ROLE_ADMIN')") after integration with Spring Security(for example)
    public ResponseEntity<Void> updateTransactionByAccountId(@RequestBody UpdateTransaction updateTransaction,
                                                             @PathVariable("accountId") String accountId,
                                                             @PathVariable("transactionId") String transactionId) {
        transactionService.updateTransactionByAccountId(updateTransaction, accountId, transactionId);

        return ResponseEntity.noContent().build();
    }
}
