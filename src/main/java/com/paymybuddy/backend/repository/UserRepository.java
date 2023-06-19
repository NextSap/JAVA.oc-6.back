package com.paymybuddy.backend.repository;

import com.paymybuddy.backend.object.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<String, UserEntity> {
}
