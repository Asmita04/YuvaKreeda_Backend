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

import com.yuva.kreeda.vikasa.entities.Sports;
import com.yuva.kreeda.vikasa.service.SportService;

@RestController
@RequestMapping("/sports")
@CrossOrigin(origins = "http://localhost:3000")
public class SportController {

  @Autowired
  private SportService service;

  @GetMapping
  public List<Sports> getAllSports() {
    return service.getAllSports();
  }

  @GetMapping("/{id}")
  public Sports getSportById(@PathVariable Long id) {
    return service.getSportById(id);
  }

  @PostMapping
  public Sports addSport(@RequestBody Sports sport) {
    return service.addSport(sport);
  }

  @PutMapping("/{id}")
  public Sports updateSport(@PathVariable Long id, @RequestBody Sports sport) {
    return service.updateSport(id, sport);
  }

  @DeleteMapping("/{id}")
  public String deleteSport(@PathVariable Long id) {
    return service.deleteSport(id);
  }
}
