package com.basf.codechallenge.infrastructure.ner;

import com.aliasi.dict.DictionaryEntry;
import com.aliasi.dict.MapDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;


@Service
public class CsvDictionaryService implements ChemicalsDictionaryService {
  private static final double CHUNK_SCORE = 1.0;

  private final MapDictionary<String> dictionary;

  @Autowired
  public CsvDictionaryService(@Value("classpath:chemicals.csv") Resource resourceFile) {
    this.dictionary = dictionary(resourceFile);
  }

  public MapDictionary<String> loadDictionary() {
    return dictionary;
  }

  private MapDictionary<String> dictionary(final Resource resource) {
    final MapDictionary<String> dictionary = new MapDictionary<>();
    try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8);
         BufferedReader buff = new BufferedReader(reader)) {
      String line;
      while((line = buff.readLine()) != null) {
        Arrays.stream(line.split(","))
            .forEach(c -> dictionary
                .addEntry(new DictionaryEntry<>(c, "CHEMICAL", CHUNK_SCORE)));
      }
    } catch (IOException e) {
      return new MapDictionary<>();
    }
    return dictionary;
  }
}
