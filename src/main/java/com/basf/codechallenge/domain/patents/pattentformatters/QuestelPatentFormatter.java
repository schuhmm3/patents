package com.basf.codechallenge.domain.patents.pattentformatters;

import com.basf.codechallenge.domain.patents.Patent;
import com.vdurmont.semver4j.Semver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class QuestelPatentFormatter implements PatentFormatter {
  private static final Pattern DATE_REGEX = Pattern.compile("(\\d{4})\\d{4}");
  private static final String NAME = "questel-patent-document";
  private static final Semver MAX_VERSION = new Semver("10.0.0");

  private final XPathExpression abstractPath;
  private final XPathExpression descriptionPath;

  @Autowired
  public QuestelPatentFormatter() {
    try {
      abstractPath = XPathFactory.newInstance()
          .newXPath().compile("/questel-patent-document/abstract/*");
      descriptionPath = XPathFactory.newInstance().newXPath().compile(
          "/questel-patent-document/description/*");
    } catch (XPathExpressionException xpex) {
      throw new IllegalStateException("Invalid QuestelPatentFormatter " +
          "configuration. Cause={}", xpex.getCause());
    }
  }

  @Override
  public boolean accept(PatentFormat format) {
    Semver vs;
    if (format == null) {
      return false;
    }
    try {
      final String version = format.attribute("schema-version");
      vs = new Semver(version, Semver.SemverType.LOOSE);
    } catch (Exception ex) {
      vs = new Semver("0.0.1");
    }
    return NAME.equals(format.name()) &&
        MAX_VERSION.compareTo(vs) > 0;
  }

  @Override
  public Patent patent(Document document) {
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
    final String abs = nodeContent(document, abstractPath);
    builder.withAbs(abs);
    final String descriptions = nodeContent(document, descriptionPath);
    builder.withContents(descriptions);
    return builder.build();
  }

  private String extractYear(final String date) {
    final Matcher matcher = DATE_REGEX.matcher(date);
    if(matcher.matches()) {
      return matcher.group(1);
    }
    return null;
  }

  private String nodeContent(final Document document, XPathExpression xpath) {
    final StringWriter sw = new StringWriter();
    try {
      final Node node = (Node) xpath.evaluate(document, XPathConstants.NODE);
      final TransformerFactory tf = TransformerFactory.newInstance();
      final Transformer transformer = tf.newTransformer();
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
      transformer.setOutputProperty(OutputKeys.INDENT, "no");
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      transformer.transform(new DOMSource(node), new StreamResult(sw));
      return sw.toString();
    } catch (Exception ex) {
      return null;
    }
  }
}
