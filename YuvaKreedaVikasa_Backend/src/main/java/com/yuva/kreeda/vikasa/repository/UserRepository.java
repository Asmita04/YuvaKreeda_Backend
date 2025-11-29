package com.yuva.kreeda.vikasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yuva.kreeda.vikasa.entities.Role;
import com.yuva.kreeda.vikasa.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
  List<User> findByRole(Role role);
}
