package com.test.recruitment.dao;

import com.test.recruitment.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, String> {

    /**
     * Find all {@link Account}
     * <p>
     * Pageable support
     *
     * @param pageable the paging info
     * @return List of {@link Account} inside of {@link Page}
     */
    Page<Account> findAll(Pageable pageable);
}
