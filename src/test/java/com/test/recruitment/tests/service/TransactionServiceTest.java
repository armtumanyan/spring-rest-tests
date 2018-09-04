package com.test.recruitment.tests.service;

import com.test.recruitment.dao.TransactionRepository;
import com.test.recruitment.entity.Transaction;
import com.test.recruitment.exception.ServiceException;
import com.test.recruitment.json.AccountDetailsResponse;
import com.test.recruitment.json.ErrorCode;
import com.test.recruitment.json.request.CreateTransaction;
import com.test.recruitment.json.request.UpdateTransaction;
import com.test.recruitment.service.AccountService;
import com.test.recruitment.service.TransactionService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@Test(testName = "TransactionServiceTest")
public class TransactionServiceTest {
    private static final String VALID_ACCOUNT_ID = "2";
    private static final String INVALID_ACCOUNT_ID = "4";
    private static final String VALID_TRANSACTION_ID = "3";
    private static final String VALID_TRANSACTION_NUMBER = "12151885122";
    private static final BigDecimal VALID_BALANCE = BigDecimal.valueOf(25);

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void processAddTransactionIntoAccountSuccess() {
        when(accountService.getAccountDetails(VALID_ACCOUNT_ID))
                .thenReturn(buildAccountDetailsResponse(VALID_ACCOUNT_ID, true));
        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(buildTransaction(VALID_TRANSACTION_ID, VALID_ACCOUNT_ID));

        String transactionId = transactionService
                .addTransactionIntoAccount(buildCreateTransaction(VALID_BALANCE, VALID_TRANSACTION_NUMBER), VALID_ACCOUNT_ID);

        Assert.assertNotNull(transactionId);

        verify(accountService).getAccountDetails(VALID_ACCOUNT_ID);
        verify(transactionRepository).save(any(Transaction.class));
        verifyNoMoreInteractions(accountService, transactionRepository);
    }

    @Test(expectedExceptions = ServiceException.class, expectedExceptionsMessageRegExp = "Account blocked")
    public void processAddTransactionAccountBlocked() {
        AccountDetailsResponse accountDetailsResponse = buildAccountDetailsResponse(VALID_ACCOUNT_ID, false);

        when(accountService.getAccountDetails(VALID_ACCOUNT_ID))
                .thenReturn(accountDetailsResponse);

        transactionService
                .addTransactionIntoAccount(buildCreateTransaction(VALID_BALANCE, VALID_TRANSACTION_NUMBER), VALID_ACCOUNT_ID);
    }

    @Test(expectedExceptions = ServiceException.class, expectedExceptionsMessageRegExp = "Account doesn't exist")
    public void processAddTransactionAccountNotFound() {
        doThrow(new ServiceException(ErrorCode.NOT_FOUND_ACCOUNT,
                "Account doesn't exist")).when(accountService).getAccountDetails(INVALID_ACCOUNT_ID);

        transactionService
                .addTransactionIntoAccount(buildCreateTransaction(VALID_BALANCE, VALID_TRANSACTION_NUMBER), INVALID_ACCOUNT_ID);
    }

    @Test(dataProvider = "missingInputDataForCreate", expectedExceptions = ServiceException.class, expectedExceptionsMessageRegExp = "Missing required parameters")
    public void processAddTransactionMissingRequiredParameters(CreateTransaction createTransaction, String accountId) {
        transactionService.addTransactionIntoAccount(createTransaction, accountId);
    }

    @Test
    public void processUpdateTransactionByAccountIdSuccess() {
        UpdateTransaction updateTransaction = buildUpdateTransaction(VALID_BALANCE, VALID_TRANSACTION_NUMBER);
        Transaction transaction = buildTransaction(VALID_TRANSACTION_ID, VALID_ACCOUNT_ID);
        AccountDetailsResponse accountDetailsResponse = buildAccountDetailsResponse(VALID_ACCOUNT_ID, true);

        when(accountService.getAccountDetails(VALID_ACCOUNT_ID)).thenReturn(accountDetailsResponse);
        when(transactionRepository.findOne(VALID_TRANSACTION_ID)).thenReturn(transaction);

        Assert.assertNotEquals(transaction.getBalance(), updateTransaction.getBalance());
        Assert.assertNotEquals(transaction.getNumber(), updateTransaction.getNumber());

        transactionService.updateTransactionByAccountId(updateTransaction, VALID_ACCOUNT_ID, VALID_TRANSACTION_ID);

        Assert.assertEquals(transaction.getBalance(), updateTransaction.getBalance());
        Assert.assertEquals(transaction.getNumber(), updateTransaction.getNumber());

        verify(accountService).getAccountDetails(VALID_ACCOUNT_ID);
        verify(transactionRepository).findOne(VALID_TRANSACTION_ID);
        verifyNoMoreInteractions(accountService, transactionRepository);
    }

    @Test(expectedExceptions = ServiceException.class)
    public void processUpdateTransactionAccountDoesntExist() {
        doThrow(ServiceException.class).when(accountService).getAccountDetails(VALID_ACCOUNT_ID);

        transactionService.updateTransactionByAccountId(new UpdateTransaction(), VALID_ACCOUNT_ID, VALID_TRANSACTION_ID);
    }

    @Test
    public void processDeleteTransactionByIdSuccess() {
        when(transactionRepository.findOne(VALID_TRANSACTION_ID)).thenReturn(buildTransaction(VALID_TRANSACTION_ID, VALID_ACCOUNT_ID));

        transactionService.deleteById(VALID_ACCOUNT_ID, VALID_TRANSACTION_ID);

        verify(transactionRepository).delete(VALID_TRANSACTION_ID);
    }

    @Test(expectedExceptions = ServiceException.class, expectedExceptionsMessageRegExp = "Transaction don't belongs to account")
    public void processDeleteTransactionByIdAccountsDontMatch() {
        when(transactionRepository.findOne(VALID_TRANSACTION_ID)).thenReturn(buildTransaction(VALID_TRANSACTION_ID, INVALID_ACCOUNT_ID));

        transactionService.deleteById(VALID_ACCOUNT_ID, VALID_TRANSACTION_ID);
    }

    @Test(expectedExceptions = ServiceException.class, expectedExceptionsMessageRegExp = "Transaction doesn't exist")
    public void processDeleteTransactionByIdNotFound() {
        when(transactionRepository.findOne(VALID_TRANSACTION_ID)).thenReturn(null);

        transactionService.deleteById(VALID_ACCOUNT_ID, VALID_TRANSACTION_ID);
    }

    @Test(dataProvider = "missingInputDataForDelete", expectedExceptions = ServiceException.class)
    public void processDeleteTransactionByIdMissingRequiredParams(String accountId, String transactionId) {
        transactionService.deleteById(accountId, transactionId);
    }

    @DataProvider(name = "missingInputDataForDelete")
    private Object[][] missingInputDataForDelete() {
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

    @DataProvider(name = "missingInputDataForCreate")
    private Object[][] missingInputDataForCreate() {
        return new Object[][]{
                {
                        null, null
                },
                {
                        null, VALID_ACCOUNT_ID
                },
                {
                        new CreateTransaction(), null
                },
                {
                        buildCreateTransaction(VALID_BALANCE, VALID_TRANSACTION_NUMBER), null
                },
                {
                        buildCreateTransaction(null, VALID_TRANSACTION_NUMBER), null
                },
                {
                        buildCreateTransaction(VALID_BALANCE, null), null
                }
        };
    }

    private Transaction buildTransaction(String transactionId, String accountId) {
        Transaction transaction = new Transaction();
        transaction.setId(transactionId);
        transaction.setAccountId(accountId);
        return transaction;
    }

    private AccountDetailsResponse buildAccountDetailsResponse(String accountId, boolean isActive) {
        AccountDetailsResponse accountDetailsResponse = new AccountDetailsResponse();
        accountDetailsResponse.setId(accountId);
        accountDetailsResponse.setActive(isActive);
        return accountDetailsResponse;
    }

    private CreateTransaction buildCreateTransaction(BigDecimal balance, String number) {
        CreateTransaction createTransaction = new CreateTransaction();
        createTransaction.setBalance(balance);
        createTransaction.setNumber(number);
        return createTransaction;
    }

    private UpdateTransaction buildUpdateTransaction(BigDecimal balance, String number) {
        UpdateTransaction updateTransaction = new UpdateTransaction();
        updateTransaction.setBalance(balance);
        updateTransaction.setNumber(number);
        return updateTransaction;
    }
}
