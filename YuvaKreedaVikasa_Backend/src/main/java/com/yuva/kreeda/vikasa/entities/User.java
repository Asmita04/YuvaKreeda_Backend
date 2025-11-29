package com.yuva.kreeda.vikasa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class User extends BaseEntity {
  @Column(length = 20, nullable = false)
  private String name;

  @Column(length = 30, unique = true, nullable = false)
  private String email;

  @Column(length = 300, nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(length = 20, nullable = false)
  private Role role;

  public User(String name, String email, String password, Role role) {
    super();
    this.name = name;
    this.email = email;
    this.password = password;
    this.role = role;
  }

  @jakarta.persistence.ManyToMany(fetch = jakarta.persistence.FetchType.EAGER)
  @jakarta.persistence.JoinTable(name = "user_sports", joinColumns = @jakarta.persistence.JoinColumn(name = "user_id"), inverseJoinColumns = @jakarta.persistence.JoinColumn(name = "sport_id"))
  private java.util.Set<Sports> sports = new java.util.HashSet<>();
}