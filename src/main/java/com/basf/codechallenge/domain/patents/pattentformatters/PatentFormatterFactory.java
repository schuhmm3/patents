package com.basf.codechallenge.domain.patents.pattentformatters;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Set;

@Service
public class PatentFormatterFactory {

  private final Set<PatentFormatter> patentFormatters;

  @Autowired
  public PatentFormatterFactory(final Set<PatentFormatter> patentFormatters) {
    this.patentFormatters = patentFormatters;
  }

  public PatentFormatter build(final PatentFormat format) {
    return patentFormatters.stream()
        .filter(f -> f.accept(format))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Patent document type not found."));
  }
}
