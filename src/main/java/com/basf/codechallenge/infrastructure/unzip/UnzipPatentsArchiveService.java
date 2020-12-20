package com.basf.codechallenge.infrastructure.unzip;

import com.basf.codechallenge.application.patents.ExtractPatentsArchiveService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class UnzipPatentsArchiveService implements ExtractPatentsArchiveService {
  private static final String FILENAME_PATTERN = "^.+\\.xml";
  private static final Logger LOG =
      LoggerFactory.getLogger(UnzipPatentsArchiveService.class);

  public Stream<Pair<String, String>> extract(final InputStream zippedIs) {
    final Stream.Builder<Pair<String,String>> builder = Stream.builder();
    try (BufferedInputStream bis = new BufferedInputStream(zippedIs);
         final ZipInputStream zis = new ZipInputStream(bis)) {
      ZipEntry entry;
      while ((entry = zis.getNextEntry()) != null) {
        if (!entry.isDirectory() && entry.getName().matches(FILENAME_PATTERN)) {
          builder.add(new ImmutablePair<>(entry.getName(), reader(zis)));
        }
      }
    } catch (IOException ioex) {
      LOG.error("Something failed while reading pattents from zip file. Cause={}", ioex.getMessage());
    }
    return builder.build();
  }

  public static String reader(final ZipInputStream zipInputStream) {
    return new BufferedReader(
        new InputStreamReader(zipInputStream, StandardCharsets.UTF_8))
        .lines()
        .collect(Collectors.joining(""));
  }
}
