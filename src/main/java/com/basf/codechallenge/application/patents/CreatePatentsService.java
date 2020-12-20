package com.basf.codechallenge.application.patents;

import com.basf.codechallenge.domain.chemicals.ExtractChemicalsService;
import com.basf.codechallenge.domain.patents.PatentFactory;
import com.basf.codechallenge.domain.patents.PatentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Objects;
import java.util.Set;

@Service
public class CreatePatentsService {
  private final ExtractChemicalsService extractChemicalsService;
  private final ExtractPatentsArchiveService extractPatentsArchiveService;
  private final PatentFactory patentFactory;
  private final PatentRepository patentRepository;

  @Autowired
  public CreatePatentsService(
      final ExtractChemicalsService extractChemicalsService,
      final PatentRepository patentRepository,
      final ExtractPatentsArchiveService extractPatentsArchiveService,
      final PatentFactory patentFactory) {
    this.extractChemicalsService = extractChemicalsService;
    this.patentRepository = patentRepository;
    this.extractPatentsArchiveService = extractPatentsArchiveService;
    this.patentFactory = patentFactory;
  }

  public void createPatentsFromArchive(final InputStream archive) {
    extractPatentsArchiveService.extract(archive)
        .parallel()
        .map(p -> patentFactory.build(p.getLeft(), p.getRight()))
        .filter(Objects::nonNull)
        .forEach(p -> {
          final Set<String> chemicals = extractChemicalsService
              .runNamedEntityRecognitionOver(p.contentsToAnalyze());
          p.addChemicals(chemicals);
          patentRepository.save(p);
        });
  }
}
