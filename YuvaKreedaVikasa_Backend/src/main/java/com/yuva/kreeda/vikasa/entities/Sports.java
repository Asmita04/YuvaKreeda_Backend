package com.yuva.kreeda.vikasa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "sports")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Sports extends BaseEntity {

  @Column(length = 100, unique = true, nullable = false)
  private String sportName;

  @Column(length = 200)
  private String description;

  public Sports(String sportName, String description) {
    this.sportName = sportName;
    this.description = description;
  }
}
