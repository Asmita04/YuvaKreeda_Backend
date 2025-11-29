package com.yuva.kreeda.vikasa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuva.kreeda.vikasa.custom_exceptions.ResourceNotFoundException;
import com.yuva.kreeda.vikasa.entities.Sports;
import com.yuva.kreeda.vikasa.repository.SportRepository;

@Service
@Transactional
public class SportServiceImpl implements SportService {

  @Autowired
  private SportRepository repo;

  @Override
  public List<Sports> getAllSports() {
    return repo.findAll();
  }

  @Override
  public Sports addSport(Sports sport) {
    return repo.save(sport);
  }

  @Override
  public Sports updateSport(Long id, Sports newSport) {
    Sports sport = repo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Sport not found with id " + id));
    sport.setSportName(newSport.getSportName());
    sport.setDescription(newSport.getDescription());
    return repo.save(sport);
  }

  @Override
  public String deleteSport(Long id) {
    if (!repo.existsById(id)) {
      throw new ResourceNotFoundException("Sport not found with id " + id);
    }
    repo.deleteById(id);
    return "Deleted Successfully";
  }

  @Override
  public Sports getSportById(Long id) {
    return repo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Sport not found with id " + id));
  }
}
