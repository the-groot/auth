package com.example.auth.Repository;


import com.example.auth.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    public List<User> findAll();
    public Optional<User> findOneByUsername(String username);

}
