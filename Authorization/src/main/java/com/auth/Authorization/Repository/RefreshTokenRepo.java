package com.auth.Authorization.Repository;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.auth.Authorization.Model.RefreshTokenEntity;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);
     
     @Query(value = "SELECT rt.* FROM REFRESH_TOKENS rt " +
              "INNER JOIN USER_DETAILS ud ON rt.user_id = ud.id " +
              "WHERE ud.EMAIL = :userEmail and rt.revoked = false ", nativeQuery = true)
      List<RefreshTokenEntity> findAllRefreshTokenByUserEmailId(String userEmail);
}
