package com.test.recruitment.dao;

import com.test.recruitment.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends PagingAndSortingRepository<Transaction, String> {

    /**
     * Find all {@link Transaction} by {@link com.test.recruitment.entity.Account} id
     * <p>
     * Pageable support
     *
     * @param accountId the account id
     * @param pageable  the paging info
     * @return List of {@link Transaction} inside of {@link Page}
     */
    Page<Transaction> findAllByAccountId(String accountId, Pageable pageable);
}