package com.ehr.users.dao;

import com.ehr.users.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {
    @Query(value = "SELECT * FROM otp WHERE email = :email ORDER BY expiration_time DESC LIMIT 1", nativeQuery = true)
    Optional<Otp> findByEmail(@Param("email") String email);
}

