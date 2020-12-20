package com.basf.codechallenge.domain.patents.pattentformatters;

import org.w3c.dom.NamedNodeMap;

import java.util.HashMap;
import java.util.Map;

public class PatentFormat {
  private final String name;
  private final Map<String, String> additionalInfo;

  public PatentFormat(final String name) {
    this.name = name;
    this.additionalInfo = new HashMap<>();
  }

  public void addFormatInfo(final NamedNodeMap attributes) {
    for(int i = 0; i < attributes.getLength(); i++) {
      additionalInfo.put(
          attributes.item(i).getNodeName(),
          attributes.item(i).getNodeValue()
      );
    }
  }

  public String name() {return name; }

  public String attribute(final String name) {
    return additionalInfo.get(name);
  }
}
