package com.basf.codechallenge.domain.patents;

import org.springframework.data.annotation.Transient;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Patent {
  private final String id;
  private final String title;
  private final String application;
  private final String year;
  private final String abs;
  private final Set<String> chemicals;
  @Transient
  private final String contents;

  private Patent(final String id,
      final String title,
      final String application,
      final String year,
      final String abs,
      final String contents,
      final Set<String> chemicals) {
    if (id == null || "".equals(id.trim())) {
      throw new IllegalArgumentException("Patent id can't be null or empty.");
    }
    if (title == null || "".equals(title.trim())) {
      throw new IllegalArgumentException("Patent title can't be null or empty.");
    }
    if (application == null || "".equals(application.trim())) {
      throw new IllegalArgumentException("Patent application can't be null or empty.");
    }
    if (year == null || "".equals(year.trim())) {
      throw new IllegalArgumentException("Patent year can't be null or empty.");
    }
    this.id = id;
    this.title = title.trim();
    this.application = application.trim();
    this.year = year.trim();
    this.abs = abs != null ? abs.trim() : null;
    this.contents = contents!= null ? contents.trim() : null;
    this.chemicals = chemicals;
  }

  public static Patent of(String id,
      String title,
      String application,
      String year,
      String abs,
      Set<String> chemicals) {
    return new Patent(id, title, application, year, abs, null, chemicals);
  }

  public final String contentsToAnalyze() {
    return abs + "\n" + contents;
  }

  public final void addChemicals(final Set<String> chemicals) {
    this.chemicals.addAll(chemicals);
  }

  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getApplication() {
    return application;
  }

  public String getYear() {
    return year;
  }

  public String getAbs() {
    return abs;
  }

  public Set<String> getChemicals() {
    return chemicals;
  }

  public String getContents() {
    return contents;
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o == null) {
      return false;
    }
    if (!(o instanceof Patent)) {
      return false;
    }
    final Patent other = (Patent)o;
    return  id.equals(other.id);
  }

  public static class Builder {
    private String title;
    private String application;
    private String year;
    private String abs;
    private String contents;

    public Builder() {}

    public Builder withTitle(final String title) {
      this.title = title;
      return this;
    }

    public Builder withApplication(final String application) {
      this.application = application;
      return this;
    }

    public Builder withYear(final String year) {
      this.year = year;
      return this;
    }

    public Builder withAbs(final String abs) {
      this.abs = abs;
      return this;
    }

    public Builder withContents(final String contents) {
      this.contents = contents;
      return this;
    }

    public Patent build() {
      final String id = UUID.randomUUID().toString();
      return new Patent(id, title, application, year, abs, contents, new HashSet<>());
    }
  }
}


