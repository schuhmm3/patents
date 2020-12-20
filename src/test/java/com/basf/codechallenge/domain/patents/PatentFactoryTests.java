package com.basf.codechallenge.domain.patents;

import com.basf.codechallenge.domain.patents.pattentformatters.PatentFormat;
import com.basf.codechallenge.domain.patents.pattentformatters.PatentFormatter;
import com.basf.codechallenge.domain.patents.pattentformatters.PatentFormatterFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PatentFactoryTests {
  private PatentFormatterFactory formatterFactory;
  private PatentFactory subject;

  @BeforeEach
  public void setup() {
    formatterFactory = mock(PatentFormatterFactory.class);
    subject = new PatentFactory(formatterFactory);
  }

  @DisplayName("it uses patent formatter for contents parsing")
  @Test
  public void itUsesPatentFormatterForContentsParsing() {
    final Patent patent = new Patent.Builder()
        .withTitle("doc-title")
        .withYear("2008")
        .withApplication("Q")
        .withAbs("abstract")
        .withContents("any description")
        .build();

    final String xml = "<?xml version=\"1.0\"?>\n" +
        "<doc-name title=\"doc-title\" year=\"2008\">\n" +
        " <abstract>\n" +
        "  abstract" +
        " </abstract>\n" +
        " <description>\n" +
        "  any description" +
        " </description>\n" +
        "</doc-name>";
    when(formatterFactory.build(argThat(a -> "doc-name".equals(a.name()))))
        .thenReturn(new PatentFormatter() {
          @Override
          public Patent patent(Document document) {
            return patent;
          }

          @Override
          public boolean accept(PatentFormat format) {
            return true;
          }
        });
    final Patent results = subject.build("A012.xml",xml);

    verify(formatterFactory).build(argThat(a -> "doc-name".equals(a.name())));
    assertEquals(patent, results);
  }

  @DisplayName("it returns null on any error")
  @Test
  public void itReturnsNullOnError() {
    final String xml = "<?xml version=\"1.0\"?>\n" +
        "<doc-name title=\"doc-title\" year=\"2008\">\n" +
        " <abstract>\n" +
        "  abstract" +
        " </abstract>\n" +
        " <description>\n" +
        "  any description" +
        " </description>\n" +
        "</doc-name>";
    when(formatterFactory.build(argThat(a -> "doc-name".equals(a.name()))))
        .thenThrow(new RuntimeException("Failed"));
    final Patent results = subject.build("A012.xml",xml);

    verify(formatterFactory).build(argThat(a -> "doc-name".equals(a.name())));
    assertNull(results);
  }
}
