package com.basf.codechallenge.infrastructure.ner;

import com.aliasi.dict.DictionaryEntry;
import com.aliasi.dict.MapDictionary;
import com.basf.codechallenge.domain.chemicals.ExtractChemicalsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class LinqpipeExtractChemicalsServiceTests {

  private ChemicalsDictionaryService dictionaryService;
  private ExtractChemicalsService subject;

  private MapDictionary<String> dictionary;

  @BeforeEach
  public void setup() {
    dictionaryService = mock(ChemicalsDictionaryService.class);
    subject = new LingpipeExtractChemicalsService(dictionaryService);
    dictionary = new MapDictionary<>();
    dictionary.addEntry(new DictionaryEntry<>("isobutanol", "CHEMICALS", 1));
    dictionary.addEntry(new DictionaryEntry<>("tetrachloromethane", "CHEMICALS", 1));
    dictionary.addEntry(new DictionaryEntry<>("calcium carbonate", "CHEMICALS", 1));
  }

  @DisplayName("it can extract chemicals")
  @Test
  public void itCanExtractChemicals() {
    final String      contents = "isobutanol is simply dummy text of " +
        "the printing and typesetting industry. " +
        "Lorem Ipsum has been the industry's standard dummy text ever " +
        "since the 1500s, when an unknown printer took a galley " +
        "of type and scrambled it to make a type specimen book. " +
        "It has survived not only five centuries, but also the leap " +
        "into electronic typesetting, remaining essentially unchanged. " +
        "calcium carbonate It was popularised in the 1960s with the release " +
        "of Letraset sheets containing Lorem Ipsum passages, and more recently with " +
        "desktop publishing software like Aldus PageMaker including " +
        "versions of Lorem Ipsum";

    when(dictionaryService.loadDictionary()).thenReturn(dictionary);
    final Set<String> results = subject.runNamedEntityRecognitionOver(contents);
    assertNotNull(results);
    assertEquals(2, results.size());
    assertTrue(results.contains("calcium carbonate"));
    assertTrue(results.contains("isobutanol"));
  }

  @DisplayName("it can extract chemicals on different case")
  @Test
  public void itCanExtractChemicalsOnDifferentCase() {
    final String      contents = "isoButanol is simply dummy text of " +
        "the printing and typesetting industry. " +
        "Lorem Ipsum has been the industry's standard dummy text ever " +
        "since the 1500s, when an unknown printer took a galley " +
        "of type and scrambled it to make a type specimen book. " +
        "It has survived not only five centuries, but also the leap " +
        "into electronic typesetting, remaining essentially unchanged. " +
        "Calcium carbonate It was popularised in the 1960s with the release " +
        "of Letraset sheets containing Lorem Ipsum passages, and more recently with " +
        "desktop publishing software like Aldus PageMaker including " +
        "versions of Lorem Ipsum";

    when(dictionaryService.loadDictionary()).thenReturn(dictionary);
    final Set<String> results = subject.runNamedEntityRecognitionOver(contents);
    assertNotNull(results);
    assertEquals(2, results.size());
    assertTrue(results.contains("Calcium carbonate"));
    assertTrue(results.contains("isoButanol"));
  }

  @DisplayName("it returns empty when there is no dictionary")
  @Test
  public void itCanExtractChemicalsWithoutWhiteSpaces() {
    final String      contents = "isobutanol is simply dummy text of " +
        "the printing and typesetting industry. " +
        "Lorem Ipsum has been the industry's standard dummy text ever " +
        "since the 1500s, when an unknown printer took a galley " +
        "of type and scrambled it to make a type specimen book. " +
        "It has survived not only five centuries, but also the leap " +
        "into electronic typesetting, remaining essentially unchanged. " +
        "Calciumcarbonate It was popularised in the 1960s with the release " +
        "of Letraset sheets containing Lorem Ipsum passages, and more recently with " +
        "desktop publishing software like Aldus PageMaker including " +
        "versions of Lorem Ipsum";

    when(dictionaryService.loadDictionary()).thenReturn(null);
    final Set<String> results = subject.runNamedEntityRecognitionOver(contents);
    assertNotNull(results);
    assertEquals(0, results.size());
  }
}

