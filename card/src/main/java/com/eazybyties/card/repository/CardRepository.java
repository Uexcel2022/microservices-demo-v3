package com.eazybyties.card.repository;

import com.eazybyties.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByCardNumberOrMobileNumber(String cardNumber, String mobileNumber);
    boolean existsByMobileNumber(String mobileNumber);
}