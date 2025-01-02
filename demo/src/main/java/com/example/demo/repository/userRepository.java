package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.io.entity.userEntity;

@Repository
public interface userRepository extends CrudRepository<userEntity, Long> {

    userEntity findByEmail(String email);

    userEntity findByUserId(String userId);

}
