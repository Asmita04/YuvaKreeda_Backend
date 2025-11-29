package com.yuva.kreeda.vikasa.service;

import java.util.List;

import com.yuva.kreeda.vikasa.entities.Role;
import com.yuva.kreeda.vikasa.entities.User;

public interface UserService {
  List<User> getAllUsers();

  String addUser(com.yuva.kreeda.vikasa.dto.UserDTO user);

  User getDetailsById(Long id);

  String updateDetails(Long id, User user);

  String deleteDetails(Long id);

  List<User> getUsersByRole(Role role);
}
