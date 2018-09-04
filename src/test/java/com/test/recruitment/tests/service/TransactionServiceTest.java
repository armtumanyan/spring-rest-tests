package com.test.recruitment.tests.service;

import com.test.recruitment.dao.TransactionRepository;
import com.test.recruitment.entity.Transaction;
import com.test.recruitment.exception.ServiceException;
import com.test.recruitment.service.TransactionService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Test(testName = "TransactionServiceTest")
public class TransactionServiceTest {
    private static final String VALID_ACCOUNT_ID = "2";
    private static final String INVALID_ACCOUNT_ID = "4";
    private static final String VALID_TRANSACTION_ID = "3";

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void processDeleteTransactionByIdSuccess() {
        when(transactionRepository.findById(VALID_TRANSACTION_ID)).thenReturn(buildTransaction(VALID_ACCOUNT_ID));

        transactionService.deleteById(VALID_ACCOUNT_ID, VALID_TRANSACTION_ID);

        verify(transactionRepository).deleteById(VALID_TRANSACTION_ID);
    }

    @Test(expectedExceptions = ServiceException.class, expectedExceptionsMessageRegExp = "Transaction don't belongs to account")
    public void processDeleteTransactionByIdAccountsDontMatch() {
        when(transactionRepository.findById(VALID_TRANSACTION_ID)).thenReturn(buildTransaction(INVALID_ACCOUNT_ID));

        transactionService.deleteById(VALID_TRANSACTION_ID, VALID_TRANSACTION_ID);
    }

    @Test(expectedExceptions = ServiceException.class, expectedExceptionsMessageRegExp = "Transaction doesn't exist")
    public void processDeleteTransactionByIdNotFound() {
        when(transactionRepository.findById(VALID_TRANSACTION_ID)).thenReturn(null);

        transactionService.deleteById(VALID_ACCOUNT_ID, VALID_TRANSACTION_ID);
    }

    @Test(dataProvider = "missingInputData", expectedExceptions = ServiceException.class)
    public void processDeleteTransactionByIdMissingRequiredParams(String accountId, String transactionId) {
        transactionService.deleteById(accountId, transactionId);
    }

    @DataProvider(name = "missingInputData")
    private Object[][] missingInputData() {
        return new Object[][]{
                {
                        null, null
                },
                {
                        VALID_ACCOUNT_ID, null
                },
                {
                        null, VALID_TRANSACTION_ID
                }
        };
    }

    private Transaction buildTransaction(String accountId) {
        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        return transaction;
    }
}
