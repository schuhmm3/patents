package com.basf.codechallenge.infrastructure.ner;

import com.aliasi.dict.MapDictionary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import static org.junit.jupiter.api.Assertions.*;

public class CsvDictionaryServiceTests {
  private ChemicalsDictionaryService subject;

  @BeforeEach
  public void setup() {
    final Resource res = new ClassPathResource("test-chemicals.csv");
    subject = new CsvDictionaryService(res);
  }

  @DisplayName("it loads all chemicals")
  @Test
  public void itLoadsAllChemicals() {
    final MapDictionary<String> results = subject.loadDictionary();
    assertNotNull(results);
    assertEquals(4, results.size());
  }
}
