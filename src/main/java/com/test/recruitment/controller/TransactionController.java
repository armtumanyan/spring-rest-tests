package com.test.recruitment.controller;

import com.test.recruitment.json.SuccessResponse;
import com.test.recruitment.json.request.CreateTransaction;
import com.test.recruitment.json.request.UpdateTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.test.recruitment.json.TransactionResponse;

/**
 * Transaction controller
 * 
 * @author A525125
 *
 */
@RequestMapping(value = "/accounts/{accountId}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
public interface TransactionController {

	/**
	 * Get transaction list by account
	 * 
	 * @param accountId
	 *            the account id
	 * @param p
	 *            the pageable information
	 * @return the transaction list
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	ResponseEntity<Page<TransactionResponse>> getTransactionsByAccount(
			@PathVariable("accountId") String accountId,
			@PageableDefault Pageable p);

	/**
	 * Delete {@link com.test.recruitment.entity.Transaction} with provided id
	 *
	 * @param accountId     account id of transaction
	 * @param transactionId the transaction id
	 * @return empty body with NO_CONTENT(204) status code
	 */
	@DeleteMapping(value = "/{transactionId}")
	ResponseEntity<Void> delete(@PathVariable("accountId") String accountId,
								@PathVariable("transactionId") String transactionId);

	/**
	 * Add a new transaction on an account
	 *
	 * @param createTransaction request body {@link CreateTransaction}
	 * @param accountId         the account id
	 * @return {@link SuccessResponse} in case of success
	 */
	@PostMapping
	ResponseEntity<SuccessResponse> addTransactionIntoAccount(@PathVariable("accountId") String accountId,
															  @RequestBody CreateTransaction createTransaction);


	/**
	 * Update existing transaction by transaction and account id
	 *
	 * @param updateTransaction request body {@link UpdateTransaction}
	 * @param accountId         the account id
	 * @param transactionId     the transaction id
	 * @return {@link SuccessResponse} in case of success
	 */
	@PutMapping(value = "/{transactionId}")
	ResponseEntity<Void> updateTransactionByAccountId(@PathVariable("accountId") String accountId,
													  @PathVariable("transactionId") String transactionId,
													  @RequestBody UpdateTransaction updateTransaction);
}
