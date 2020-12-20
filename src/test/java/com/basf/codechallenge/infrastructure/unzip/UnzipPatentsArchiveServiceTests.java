package com.basf.codechallenge.infrastructure.unzip;

import com.basf.codechallenge.application.patents.ExtractPatentsArchiveService;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class UnzipPatentsArchiveServiceTests {

  private static final String CONTENTS_A =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?><questel-patent" +
      "-document lang=\"en\" date-produced=\"20180805\" " +
      "produced-by=\"Questel\" schema-version=\"3.23\" file=\"US06177006B1" +
      ".xml\">  <bibliographic-data lang=\"en\">    <invention-title " +
      "format=\"original\" lang=\"en\" id=\"title_en\">Filtering " +
      "device</invention-title>  </bibliographic-data>  <abstract " +
      "format=\"original\" lang=\"en\" id=\"abstr_en\">    <p><br/>Abstract" +
      ".</p>  </abstract>  <description format=\"original\" lang=\"en\" " +
      "id=\"desc_en\">    <heading>BACKGROUND OF THE INVENTION</heading>  " +
      "</description></questel-patent-document>";


  final String B = "";

  private static final String CONTENTS_B =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?><questel-patent-document " +
      "lang=\"en\" date-produced=\"20180805\" produced-by=\"Questel\" " +
      "schema-version=\"3.23\" file=\"US06178393B1.xml\">  " +
      "<bibliographic-data lang=\"en\">    <invention-title " +
      "format=\"original\" lang=\"en\" id=\"title_en\">Pump station " +
      "control system and method</invention-title>  </bibliographic-data>" +
      "  <abstract format=\"original\" lang=\"en\" id=\"abstr_en\">    <p" +
      " ><br/>Another abstract.</p>  </abstract>  <description " +
      "format=\"original\" lang=\"en\" id=\"desc_en\">Description" +
      ".</description></questel-patent-document>";

  private ExtractPatentsArchiveService subject;

  @BeforeEach
  public void setup() {
    subject = new UnzipPatentsArchiveService();
  }

  @DisplayName("it can unzip input params")
  @Test
  public void itCanUnzipInputParams() {
    final InputStream is = Thread.currentThread()
        .getContextClassLoader()
        .getResourceAsStream("patents.zip");
    final Stream<Pair<String, String>> results = subject.extract(is);
    assertNotNull(results);
    assertEquals(2, (int) results.count());
  }

  @DisplayName("it can unzip input file names")
  @Test
  public void itCanUnzipFileNames() {
    final InputStream is = Thread.currentThread()
        .getContextClassLoader()
        .getResourceAsStream("patents.zip");
    final Stream<Pair<String, String>> results = subject.extract(is);
    assertNotNull(results);
    assertEquals(0,
        results.filter(p ->
            !p.getLeft().contains("US06177006B1.xml") && !p.getLeft().contains("US06178393B1.xml")
        ).count());
  }

  @DisplayName("it can unzip input file contents")
  @Test
  public void itCanUnzipFileContents() {
    final InputStream is = Thread.currentThread()
        .getContextClassLoader()
        .getResourceAsStream("patents.zip");
    final Stream<Pair<String, String>> results = subject.extract(is);
    assertNotNull(results);
    assertEquals(0,
        results.filter(p ->
            !p.getRight().equals(CONTENTS_A) && !p.getRight().equals(CONTENTS_B)
        ).count());
  }
}
