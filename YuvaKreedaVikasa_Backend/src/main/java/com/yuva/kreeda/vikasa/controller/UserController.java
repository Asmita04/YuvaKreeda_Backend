package com.yuva.kreeda.vikasa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yuva.kreeda.vikasa.entities.Role;
import com.yuva.kreeda.vikasa.entities.User;
import com.yuva.kreeda.vikasa.service.UserService;

@RestController // @Controller - cls level annotation + @ResponseBody - req handling
                // method(@GetMapping | @PostMapping| .......) ret type
@RequestMapping("/users") // optional BUT recommended to specify base url pattern
@CrossOrigin(origins = "http://localhost:3000") // required only for Browser running app - public facing apps
public class UserController {

  // depcy
  @Autowired
  private UserService userService;

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
  @PostMapping
  public String addNewUser(@RequestBody com.yuva.kreeda.vikasa.dto.UserDTO userDto) {
    System.out.println("in add " + userDto);
    // invoke service layer method
    return userService.addUser(userDto);
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

