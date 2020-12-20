package com.basf.codechallenge.infrastructure.ner;

import com.aliasi.dict.MapDictionary;

public interface ChemicalsDictionaryService {
  MapDictionary<String> loadDictionary();
}
