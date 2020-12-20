package com.basf.codechallenge.domain.patents;

public interface PatentRepository {
  String save(final Patent patent);
  void clear();
}
