package com.yuva.kreeda.vikasa.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuva.kreeda.vikasa.custom_exceptions.ResourceNotFoundException;
import com.yuva.kreeda.vikasa.dto.UserDTO;
import com.yuva.kreeda.vikasa.entities.Role;
import com.yuva.kreeda.vikasa.entities.Sports;
import com.yuva.kreeda.vikasa.entities.User;
import com.yuva.kreeda.vikasa.repository.SportRepository;
import com.yuva.kreeda.vikasa.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EmailService emailService;

  @Autowired // Added SportRepository injection
  private SportRepository sportRepo;

  @Override
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  @Override
  public String addUser(UserDTO userDto) { // Changed parameter type to UserDTO
    // 1. Create User entity from DTO
    User user = new User();
    user.setName(userDto.getName());
    user.setEmail(userDto.getEmail());
    user.setPassword(userDto.getPassword());
    user.setRole(userDto.getRole());

    // 2. Fetch Sports entities using sportNames
    if (userDto.getSportNames() != null && !userDto.getSportNames().isEmpty()) {
      java.util.List<Sports> existingSports = sportRepo.findBySportNameIn(userDto.getSportNames());
      java.util.Set<Sports> finalSports = new HashSet<>(existingSports);

      // Extract names of existing sports
      java.util.Set<String> existingSportNames = new HashSet<>();
      for (Sports s : existingSports) {
        existingSportNames.add(s.getSportName());
      }

      // Find missing sports and create them
      for (String sportName : userDto.getSportNames()) {
        if (!existingSportNames.contains(sportName)) {
          Sports newSport = new Sports(sportName, "Will be generated during user registration");
          finalSports.add(sportRepo.save(newSport));
        }
      }
      user.setSports(finalSports);
    }

    // 3. Save user
    userRepository.save(user);

    // 4. Send Welcome Email (Async) without slowing the process
    emailService.sendEmail(user.getEmail(), "Welcome to YuvaKreedaVikasa!",
        "Hi " + user.getName() + ",\n\nThanks for registering on our platform.\n\nRegards,\nTeam YuvaKreedaVikasa");

    return "User added successfully with sports!";
  }

  @Override
  public User getDetailsById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
  }

  @Override
  public String updateDetails(Long id, User user) {
    if (userRepository.existsById(id)) {
      // In a real app, you'd fetch, update fields, and save.
      // For now, assuming the passed user object has the updates but we need to set
      // the ID.
      user.setId(id);
      userRepository.save(user);
      return "User updated successfully";
    }
    return "User update failed: User not found";
  }

  @Override
  public String deleteDetails(Long id) {
    if (userRepository.existsById(id)) {
      userRepository.deleteById(id);
      return "User deleted successfully";
    }
    return "User deletion failed: User not found";
  }

  @Override
  public List<User> getUsersByRole(Role role) {
    return userRepository.findByRole(role);
  }
}

