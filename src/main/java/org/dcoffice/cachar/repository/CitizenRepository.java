package org.dcoffice.cachar.repository;

import org.dcoffice.cachar.entity.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CitizenRepository extends JpaRepository<Citizen, Long> {

    /**
     * Find citizen by mobile number
     * @param mobileNumber The mobile number to search for
     * @return Optional containing the citizen if found
     */
    Optional<Citizen> findByMobileNumber(String mobileNumber);

    /**
     * Check if a citizen exists with the given mobile number
     * @param mobileNumber The mobile number to check
     * @return true if citizen exists, false otherwise
     */
    boolean existsByMobileNumber(String mobileNumber);

    /**
     * Count all verified citizens
     * @return Number of verified citizens
     */
    @Query("SELECT COUNT(c) FROM Citizen c WHERE c.isVerified = true")
    Long countVerifiedCitizens();

    /**
     * Find all citizens who have pending OTP verification
     * @return List of citizens with pending OTP
     */
    @Query("SELECT c FROM Citizen c WHERE c.otp IS NOT NULL AND c.otpExpiry > CURRENT_TIMESTAMP")
    java.util.List<Citizen> findCitizensWithPendingOTP();

    /**
     * Find citizens by verification status
     * @param isVerified The verification status
     * @return List of citizens with the specified verification status
     */
    java.util.List<Citizen> findByIsVerified(boolean isVerified);

    /**
     * Find citizens by name (case-insensitive)
     * @param name The name to search for
     * @return List of citizens with matching names
     */
    @Query("SELECT c FROM Citizen c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    java.util.List<Citizen> findByNameContainingIgnoreCase(String name);

    /**
     * Find citizens by email domain
     * @param domain The email domain to search for (e.g., 'gmail.com')
     * @return List of citizens with matching email domain
     */
    @Query("SELECT c FROM Citizen c WHERE c.email LIKE CONCAT('%@', :domain)")
    java.util.List<Citizen> findByEmailDomain(String domain);

    /**
     * Find citizens who registered within a specific time period
     * @param startDate Start date
     * @param endDate End date
     * @return List of citizens registered within the time period
     */
    @Query("SELECT c FROM Citizen c WHERE c.createdAt BETWEEN :startDate AND :endDate")
    java.util.List<Citizen> findByRegistrationDateBetween(
            java.time.LocalDateTime startDate,
            java.time.LocalDateTime endDate
    );

    /**
     * Count citizens by verification status
     * @param isVerified The verification status
     * @return Count of citizens with the specified verification status
     */
    Long countByIsVerified(boolean isVerified);

    /**
     * Delete expired OTP records (cleanup operation)
     * @return Number of records updated
     */
    @Query("UPDATE Citizen c SET c.otp = NULL, c.otpExpiry = NULL WHERE c.otpExpiry < CURRENT_TIMESTAMP")
    @org.springframework.data.jpa.repository.Modifying
    int clearExpiredOTPs();
}