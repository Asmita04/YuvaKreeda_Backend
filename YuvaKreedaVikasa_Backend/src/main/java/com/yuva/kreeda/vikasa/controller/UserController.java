package com.yuva.kreeda.vikasa.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yuva.kreeda.vikasa.YuvaKreedaVikasaApplication;
import com.yuva.kreeda.vikasa.dto.OtpVerifyDTO;
import com.yuva.kreeda.vikasa.dto.UserDTO;
import com.yuva.kreeda.vikasa.entities.Role;
import com.yuva.kreeda.vikasa.entities.User;
import com.yuva.kreeda.vikasa.service.EmailService;
import com.yuva.kreeda.vikasa.service.OtpService;
import com.yuva.kreeda.vikasa.service.UserService;

@RestController // @Controller - cls level annotation + @ResponseBody - req handling
                // method(@GetMapping | @PostMapping| .......) ret type
@RequestMapping("/users") // optional BUT recommended to specify base url pattern
@CrossOrigin(origins = "http://localhost:5173") // required only for Browser running app - public facing apps
public class UserController {

    private final YuvaKreedaVikasaApplication yuvaKreedaVikasaApplication;

  // depcy
  @Autowired
  private UserService userService;
  
  @Autowired
  private org.springframework.security.authentication.AuthenticationManager authenticationManager;

  @Autowired
  private com.yuva.kreeda.vikasa.security.JwtUtils jwtUtils;
  @Autowired
  private EmailService emailService;
  
  @Autowired
  private OtpService otpService;

    UserController(YuvaKreedaVikasaApplication yuvaKreedaVikasaApplication) {
        this.yuvaKreedaVikasaApplication = yuvaKreedaVikasaApplication;
    }
    //com.yuva.kreeda.vikasa.dto.LoginDTO
    @PostMapping("/login")
    public org.springframework.http.ResponseEntity<?> loginUser(
        @RequestBody com.yuva.kreeda.vikasa.dto.LoginDTO loginDto) {
      System.out.println("in login " + loginDto);
      try {
        // 1. Create auth token
        org.springframework.security.authentication.UsernamePasswordAuthenticationToken authToken = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
            loginDto.getEmail(), loginDto.getPassword());

        // 2. Authenticate
        org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(authToken);

        // 3. Generate JWT
        // Get User entity from CustomUserDetails
        com.yuva.kreeda.vikasa.security.CustomUserDetails userDetails = (com.yuva.kreeda.vikasa.security.CustomUserDetails) authentication
            .getPrincipal();
        User user = userDetails.getUser();

        String token = jwtUtils.generateToken(user);

        // 4. Return response with token and user details
        // We can wrap this in a LoginResponseDTO if we want, or just return a
        // Map/UserRespDTO with token
        // For now, let's add token to UserRespDTO or return a Map.
        // Let's return a Map for flexibility or modify UserRespDTO.
        // Let's modify UserRespDTO to include token or just return a Map.
        // To keep it simple and consistent with frontend expectation (which might
        // expect user details),
        // let's return a Map containing token and user details.

        List<String> sportNames = user.getSports().stream()
            .map(s -> s.getSportName())
            .collect(java.util.stream.Collectors.toList());

        com.yuva.kreeda.vikasa.dto.UserRespDTO userResp = new com.yuva.kreeda.vikasa.dto.UserRespDTO(
            user.getId(), user.getName(), user.getEmail(), user.getRole(), sportNames);

        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("token", token);
        response.put("user", userResp);

        return org.springframework.http.ResponseEntity.ok(response);

      } catch (Exception e) {
        return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED)
            .body("Invalid email or password");
      }
    }

  /*
   * Desc - Get all users (List)
   * URL - http://host:port/users
   * Method - GET
   * NO inputs !
   * Resp - List<User> -> JSON []
   */
  @GetMapping
  public List<User> getAllUsers() {
    System.out.println("in get all ");
    // rest controller -> service layer method
    return userService.getAllUsers();
  }

  /*
   * Desc - Add new user
   * URL - http://host:port/users
   * Method - POST
   * I/P - payload - json -> Java object (de-serial | un marshal ) : User entity
   * Resp - Message (success | failure)
   */
  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody UserDTO dto) {
	  System.out.println("in register : " + dto.getEmail() );
      String result = userService.addUser(dto);

      if (result.contains("not verified")) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
      }

      return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  
  @PostMapping("/otp/send")
  public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> req) {
	  System.out.println("in user controller of sendOTP!");
      String email = req.get("email");
      String otp = otpService.generateOtp(email); // store OTP in DB or cache
      emailService.sendOtpEmail(email, otp);

      return ResponseEntity.ok(Map.of("message", "OTP sent successfully"));
  }

  @PostMapping("/otp/verify")
  public ResponseEntity<?> verifyOtp(@RequestBody OtpVerifyDTO req) {
      String email = req.getEmail();
      String otp = req.getOtp();
      System.out.println("verifying otp request got : " + email + " " + otp );
      if (email == null || otp == null) {
          return ResponseEntity.badRequest().body(Map.of("message", "Email and OTP required"));
      }
      boolean valid = otpService.validateOtp(email, otp);
      if (!valid) {
          return ResponseEntity.status(400).body(Map.of("message", "Invalid or expired OTP"));
      }
      return ResponseEntity.ok(Map.of("message", "OTP verified"));
  }

  /*
   * Desc - Get user details by its id
   * URL - http://host:port/users/{userId}
   * Method - GET
   * I/P - path variable
   * Resp - User details -> ser -> json representation
   */
  @GetMapping("/{userId}")
  public User getUserDetailsById(@PathVariable Long userId) {
    System.out.println("in get details " + userId);
    // invoke service layer method
    return userService.getDetailsById(userId);
  }

  /*
   * Desc - Update user details
   * URL - http://host:port/users/{userId}
   * Method - PUT
   * I/P - path variable - userId
   * Payload - json -> Java object (entity - User)
   * Resp - string mesg(success | failed)
   */
  @PutMapping("/{userId}")
  public String updateUserDetails(@PathVariable Long userId, @RequestBody User user) {
    System.out.println("in update " + userId + " " + user);
    // invoke service layer
    return userService.updateDetails(userId, user);
  }

  /*
   * Desc - Delete user details
   * URL - http://host:port/users/{userId}
   * Method - DELETE
   * I/P - path variable - userId *
   * Resp - string mesg(success | failed)
   */
  @DeleteMapping("/{userId}")
  public String deleteUserDetails(@PathVariable Long userId) {
    System.out.println("in delete " + userId);
    // invoke service layer method
    return userService.deleteDetails(userId);
  }

  /*
   * Desc - Get users by role
   * URL - http://host:port/users/role/{role}
   * Method - GET
   * I/P - path variable - role (ADMIN | COACH | PLAYER)
   * Resp - List<User> -> JSON []
   */
  @GetMapping("/role/{role}")
  public List<User> getUsersByRole(@PathVariable Role role) {
    System.out.println("in get by role " + role);
    return userService.getUsersByRole(role);
  }

}

