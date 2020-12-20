package com.basf.codechallenge.application.patents;

import org.apache.commons.lang3.tuple.Pair;

import java.io.InputStream;
import java.util.stream.Stream;

public interface ExtractPatentsArchiveService {
  Stream<Pair<String, String>> extract(final InputStream zippedIs);
}
