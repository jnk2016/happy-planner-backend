package com.jnk2016.happyplannerbackend.payment;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUser(ApplicationUser user);
    List<Payment> findByUser(ApplicationUser user, Sort sort);
}
