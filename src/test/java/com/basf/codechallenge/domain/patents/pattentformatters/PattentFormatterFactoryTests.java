package com.basf.codechallenge.domain.patents.pattentformatters;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PattentFormatterFactoryTests {
  private PatentFormatterFactory subject;
  private Set<PatentFormatter> formatters;

  @BeforeEach
  public void setup() {
    formatters = new HashSet<>();
    formatters.add(new QuestelPatentFormatter());
  }

  @DisplayName("it returns a valid patent formatter")
  @Test
  public void itReturnsAValidPatentFormatter() {
    subject = new PatentFormatterFactory(formatters);
    final PatentFormat format = new PatentFormat("questel-patent-document");
    final PatentFormatter results = subject.build(format);
    assertNotNull(results);
  }

  @DisplayName("it fails on Invalid format")
  @Test
  public void itFailsOnInvalidFormat() {
    subject = new PatentFormatterFactory(formatters);
    final PatentFormat format = new PatentFormat("wrong format");
    final Exception exception =
        assertThrows(IllegalArgumentException.class, () ->
            subject.build(format));
    assertEquals("Patent document type not found.",exception.getMessage());
  }

  @DisplayName("it fails on null format")
  @Test
  public void itFailsOnNullFormat() {
    subject = new PatentFormatterFactory(formatters);
    final Exception exception =
        assertThrows(IllegalArgumentException.class, () ->
            subject.build(null));
    assertEquals("Patent document type not found.",exception.getMessage());
  }
}
