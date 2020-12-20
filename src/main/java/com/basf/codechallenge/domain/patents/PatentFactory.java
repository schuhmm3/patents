package com.basf.codechallenge.domain.patents;

import com.basf.codechallenge.domain.patents.pattentformatters.PatentFormat;
import com.basf.codechallenge.domain.patents.pattentformatters.PatentFormatter;
import com.basf.codechallenge.domain.patents.pattentformatters.PatentFormatterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;


@Service
public class PatentFactory {
  private static final Logger LOG =
      LoggerFactory.getLogger(PatentFactory.class);

  private final PatentFormatterFactory factory;

  @Autowired
  public PatentFactory(final PatentFormatterFactory factory) {
    this.factory = factory;
  }

  public Patent build(final String fileName, final String contents) {
    LOG.debug("Parsing file with name={}", fileName);
    try {
      final DocumentBuilder builder = DocumentBuilderFactory
          .newInstance()
          .newDocumentBuilder();
      final InputSource is = new InputSource(new StringReader(contents));
      final Document doc = builder.parse(is);
      doc.getDocumentElement().normalize();

      final String docName = doc.getDocumentElement().getNodeName();
      final PatentFormat format = new PatentFormat(docName);
      format.addFormatInfo(doc.getDocumentElement().getAttributes());
      final PatentFormatter formatter = factory.build(format);
      return formatter.patent(doc);
    } catch (Exception ex) {
      LOG.error("Something failed while parsing file={}. Cause={}",
          fileName,
          ex.getMessage());
    }
    return null;
  }
}
