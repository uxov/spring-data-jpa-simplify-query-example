package xyz.defe.springDataJpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import xyz.defe.springDataJpa.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}