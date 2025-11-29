package com.yuva.kreeda.vikasa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yuva.kreeda.vikasa.entities.Sports;

public interface SportRepository extends JpaRepository<Sports, Long> {
  java.util.List<Sports> findBySportNameIn(java.util.List<String> names);
}
