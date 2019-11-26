package com.invillia.banktestunitintegration.repository;

import com.invillia.banktestunitintegration.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}
