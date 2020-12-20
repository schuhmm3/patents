package com.basf.codechallenge.infrastructure.ner;



import com.aliasi.dict.ExactDictionaryChunker;
import com.aliasi.dict.MapDictionary;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.basf.codechallenge.domain.chemicals.ExtractChemicalsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LingpipeExtractChemicalsService implements ExtractChemicalsService {

  private final ChemicalsDictionaryService dictionaryService;

  @Autowired
  public LingpipeExtractChemicalsService(final ChemicalsDictionaryService dictionaryService) {
    this.dictionaryService = dictionaryService;
  }

  public Set<String> runNamedEntityRecognitionOver(final String contents) {
    final MapDictionary<String> dictionary = dictionaryService.loadDictionary();
    if (dictionary == null) {
      return new HashSet<>();
    }
    final ExactDictionaryChunker dictionaryChunkerTT
        = new ExactDictionaryChunker(dictionary,
        IndoEuropeanTokenizerFactory.INSTANCE,
        true,false);

    return dictionaryChunkerTT.chunk(contents).chunkSet()
        .parallelStream()
        .map(c -> {
          final int start = c.start();
          final int end = c.end();
          return contents.substring(start, end);
        })
        .collect(Collectors.toSet());
  }
}
