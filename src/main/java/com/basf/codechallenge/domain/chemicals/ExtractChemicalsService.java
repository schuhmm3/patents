package com.basf.codechallenge.domain.chemicals;

import java.util.Set;

public interface ExtractChemicalsService {
  Set<String> runNamedEntityRecognitionOver(final String contents);
}
