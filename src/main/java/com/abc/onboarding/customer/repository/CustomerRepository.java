package com.abc.onboarding.customer.repository;

import com.abc.onboarding.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springfox.documentation.annotations.Cacheable;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    boolean existsByUsername(String username);

    @Cacheable("customers")
    Optional<Customer> findByUsername(String username);
}
