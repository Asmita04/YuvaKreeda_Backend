package com.yuva.kreeda.vikasa.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

import lombok.RequiredArgsConstructor;

@Service
@Transactional
//@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;
  
  @Autowired
  private OtpService otpService;

  @Autowired
  private EmailService emailService;

  @Autowired // Added SportRepository injection
  private SportRepository sportRepo;

  @Override
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  //Register user 
  @Override
  public String addUser(UserDTO userDto) {

      // 1. Check email verification FIRST
      if (!otpService.isEmailVerified(userDto.getEmail())) {
          return "Email not verified. Please verify OTP before registering.";
      }
      // 2. Create User entity
      User user = new User();
      user.setName(userDto.getName());
      user.setEmail(userDto.getEmail());
      user.setPassword(userDto.getPassword());
      user.setRole(userDto.getRole());
      // 3. Fetch or create sports
      if (userDto.getSportNames() != null && !userDto.getSportNames().isEmpty()) {

          List<Sports> existingSports = sportRepo.findBySportNameIn(userDto.getSportNames());
          Set<Sports> finalSports = new HashSet<>(existingSports);

          Set<String> existingSportNames = existingSports.stream()
                  .map(Sports::getSportName)
                  .collect(Collectors.toSet());

          for (String sportName : userDto.getSportNames()) {
              if (!existingSportNames.contains(sportName)) {
                  Sports newSport = new Sports(sportName, "Auto-created during sign-up");
                  finalSports.add(sportRepo.save(newSport));
              }
          }
          user.setSports(finalSports);
      }
      // 4. Save user AFTER OTP verification
      userRepository.save(user);

      // 5. Remove email from verified list
      otpService.clearVerifiedEmail(user.getEmail());

      // 6. Send welcome email
      emailService.sendEmail(user.getEmail(), "Welcome to YuvaKreedaVikasa!",
              "Hi " + user.getName() + ",\n\nThanks for registering.\n\nTeam YKV");

      return "User registered successfully!";
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

