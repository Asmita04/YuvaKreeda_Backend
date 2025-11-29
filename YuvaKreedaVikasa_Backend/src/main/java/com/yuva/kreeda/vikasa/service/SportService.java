package com.yuva.kreeda.vikasa.service;

import java.util.List;

import com.yuva.kreeda.vikasa.entities.Sports;

public interface SportService {
  List<Sports> getAllSports();

  Sports addSport(Sports sport);

  Sports updateSport(Long id, Sports sport);

  String deleteSport(Long id);

  Sports getSportById(Long id);
}
