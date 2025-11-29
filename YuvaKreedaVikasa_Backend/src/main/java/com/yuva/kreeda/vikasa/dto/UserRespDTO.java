package com.yuva.kreeda.vikasa.dto;

import java.util.List;

import com.yuva.kreeda.vikasa.entities.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRespDTO {
  private Long id;
  private String name;
  private String email;
  private Role role;
  private List<String> sports;
}
