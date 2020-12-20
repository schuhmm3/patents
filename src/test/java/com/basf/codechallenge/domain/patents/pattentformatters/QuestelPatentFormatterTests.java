package com.basf.codechallenge.domain.patents.pattentformatters;

import com.basf.codechallenge.domain.patents.Patent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static org.junit.jupiter.api.Assertions.*;

public class QuestelPatentFormatterTests {
  public QuestelPatentFormatter subject;

  private Document doc;

  @BeforeEach
  public void setup() throws Exception {
    final DocumentBuilder builder = DocumentBuilderFactory
        .newInstance()
        .newDocumentBuilder();
    doc = builder
        .parse(Thread.currentThread().getContextClassLoader().getResourceAsStream("patent.xml"));
  }

  /*
    final Patent.Builder builder = new Patent.Builder();
    final Element questelPatent = document.getDocumentElement();
    builder.withApplication(questelPatent.getAttribute("produced-by"));
    builder.withYear(
            extractYear(questelPatent.getAttribute("date-produced"))
    );
    final NodeList titles = document.getElementsByTagName("invention-title");
    if (titles.getLength() > 0) {
      builder.withTitle(titles.item(0).getTextContent());
    }
    final NodeList abstracts = document.getElementsByTagName("abstract");
    if (abstracts.getLength() > 0) {
      builder.withAbs(abstracts.item(0).getTextContent());
    }
    final NodeList descriptions = document.getElementsByTagName("description");
    if (descriptions.getLength() > 0) {
      builder.withContents(descriptions.item(0).getTextContent());
    }
    return builder.build();
     */

  @DisplayName("it parses a valid xml document")
  @Test
  public void itParsesAValidXmlDocument() {
    subject = new QuestelPatentFormatter();
    final Patent results = subject.patent(doc);
    assertEquals("Cleaning member for ink jet head and ink jet apparatus provided with said cleaning member", results.getTitle());
    assertEquals("Questel", results.getApplication());
    assertEquals("2018", results.getYear());
    assertEquals("<p><br/>abstract.</p>", results.getAbs());
    assertEquals("<heading>FIELD OF THE INVENTION</heading>", results.getContents());
  }
}
