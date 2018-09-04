package com.test.recruitment.service;

import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.test.recruitment.dao.TransactionRepository;
import com.test.recruitment.entity.Transaction;
import com.test.recruitment.json.ErrorCode;
import com.test.recruitment.json.TransactionResponse;
import com.test.recruitment.exception.ServiceException;

/**
 * Transaction service
 * 
 * @author A525125
 *
 */
@Slf4j
@Service
public class TransactionService {

	private AccountService accountService;

	private TransactionRepository transactionRepository;

	@Autowired
	public TransactionService(AccountService accountService,
			TransactionRepository transactionRepository) {
		this.accountService = accountService;
		this.transactionRepository = transactionRepository;
	}

	/**
	 * Get transactions by account
	 * 
	 * @param accountId
	 *            the account id
	 * @param p
	 *            the pageable object
	 * @return
	 */
	public Page<TransactionResponse> getTransactionsByAccount(String accountId,
			Pageable p) {
		if (!accountService.isAccountExist(accountId)) {
			throw new ServiceException(ErrorCode.NOT_FOUND_ACCOUNT,
					"Account doesn't exist");
		}
		return new PageImpl<TransactionResponse>(transactionRepository
				.getTransactionsByAccount(accountId, p).getContent().stream()
				.map(this::map).collect(Collectors.toList()));
	}

	/**
	 * Delete {@link Transaction} by provided id
	 * <p>
	 * May throw {@link ServiceException} in case of transaction not found with provided id
	 * or provided account id does not match to transaction's account id
	 *
	 * @param accountId     account id of transaction
	 * @param transactionId the transaction id
	 */
    public void deleteById(String accountId, String transactionId) {
        log.debug("Deleting transaction with id: {}", transactionId);

		if (accountId == null || transactionId == null) {
			log.warn("Missing required parameters");
			throw new ServiceException(ErrorCode.INVALID_PARAMETERS, "Account id or transaction id can`t be blank");
		}

        Transaction transaction = Optional.ofNullable(transactionRepository.findById(transactionId))
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND_TRANSACTION, "Transaction doesn't exist"));

        if (!transaction.getAccountId().equals(accountId)) {
            throw new ServiceException(ErrorCode.FORBIDDEN_TRANSACTION, "Transaction don't belongs to account");
        }

        transactionRepository.deleteById(transactionId);
    }

	/**
	 * Map {@link Transaction} to {@link TransactionResponse}
	 * 
	 * @param transaction
	 * @return
	 */
	private TransactionResponse map(Transaction transaction) {
		TransactionResponse result = new TransactionResponse();
		result.setBalance(transaction.getBalance());
		result.setId(transaction.getId());
		result.setNumber(transaction.getNumber());
		return result;
	}

}
