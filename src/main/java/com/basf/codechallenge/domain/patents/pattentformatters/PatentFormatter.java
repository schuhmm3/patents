package com.basf.codechallenge.domain.patents.pattentformatters;

import com.basf.codechallenge.domain.patents.Patent;
import org.w3c.dom.Document;

public interface PatentFormatter {

  Patent patent(final Document document);

  boolean accept(final PatentFormat format);
}
