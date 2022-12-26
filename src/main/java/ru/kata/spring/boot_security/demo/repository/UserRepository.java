package ru.kata.spring.boot_security.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u JOIN FETCH u.roles where u.username = :username")
    User findByUsername(String username);

    @Query("select u from User u JOIN FETCH u.roles where u.email = :email")
    User findByEmail(String email);

    Optional<User> findById(Long id);


}
