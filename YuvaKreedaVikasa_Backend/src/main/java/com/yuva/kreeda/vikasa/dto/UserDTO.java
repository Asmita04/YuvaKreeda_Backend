package com.yuva.kreeda.vikasa.dto;

import java.util.List;

import com.yuva.kreeda.vikasa.entities.Role;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDTO {
  private String name;
  private String email;
  private String password;
  private Role role;
  private List<String> sportNames;
}

