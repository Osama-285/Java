package com.auth.Authorization.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auth.Authorization.Model.RefreshTokenEntity;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshTokenEntity, Long> {
     Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);
}
