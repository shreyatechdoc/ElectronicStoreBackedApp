package com.lcwd.electronic.store.repositories;

import com.lcwd.electronic.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// for automatically perform  dao operations
public interface UserRepository extends JpaRepository<User,String>
{
    // we are using DataJPA custom finder methods ,it will implement automatically by data jpa
    Optional<User> findByEmail(String email);
    //Optional<User> findByPasswordAndEmail(String password,String email);
    List<User> findByNameContaining(String keywards);
}
