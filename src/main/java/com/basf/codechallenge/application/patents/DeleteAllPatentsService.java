package com.basf.codechallenge.application.patents;

import com.basf.codechallenge.domain.patents.PatentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteAllPatentsService {
  private final PatentRepository patentRepository;

  @Autowired
  public DeleteAllPatentsService(final PatentRepository patentRepository) {
    this.patentRepository = patentRepository;
  }

  public void deleteAllPatents() {
    patentRepository.clear();
  }
}
