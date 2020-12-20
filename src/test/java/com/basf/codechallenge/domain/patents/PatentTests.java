package com.basf.codechallenge.domain.patents;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class PatentTests {

  private Patent subject;

  static Stream<Arguments> patentArgsProvider() {
    return Stream.of(
        Arguments.of(null, "title", "app", "2020", "Patent id can't be null or empty."),
        Arguments.of("  ", "title", "app", "2020", "Patent id can't be null or empty."),
        Arguments.of("1", null, "app", "2020", "Patent title can't be null or empty."),
        Arguments.of("1", "  ", "app", "2020", "Patent title can't be null or empty."),
        Arguments.of("1", "title", null, "2020", "Patent application can't be null or empty."),
        Arguments.of("1", "title", "  ", "2020", "Patent application can't be null or empty."),
        Arguments.of("1", "title", "app", null, "Patent year can't be null or empty."),
        Arguments.of("1", "title", "app", "  ", "Patent year can't be null or empty.")
    );
  }

  @DisplayName("it validates required args on creation")
  @ParameterizedTest
  @MethodSource("patentArgsProvider")
  public void itValidatesArgsOnCreation(String i,
      String t,
      String a,
      String y,
      String e) {

    final Exception exception =
        assertThrows(IllegalArgumentException.class, () ->
            Patent.of(i,
                t,
                a,
                y,
                "abstract",
                new HashSet<>())
        );
    assertEquals(e,exception.getMessage());

  }

  @DisplayName("it can build a valid patent")
  @Test
  public void itCanBuildAValidPatent() {
    subject = new Patent.Builder()
        .withTitle("Patent title")
        .withYear("2008")
        .withApplication("Q")
        .withAbs("simple abstract")
        .withContents("any contents")
        .build();

    assertEquals("Patent title", subject.getTitle());
    assertEquals("2008", subject.getYear());
    assertEquals("Q", subject.getApplication());
    assertEquals("simple abstract", subject.getAbs());
    assertEquals("any contents", subject.getContents());
    assertNotNull(subject.getId());
    assertEquals(0, subject.getChemicals().size());
  }

  @DisplayName("it can build a valid patent")
  @Test
  public void itCanBuildACompletPatent() {
    subject = Patent.of("1",
        "Patent title",
        "Q",
        "2008",
        "simple abstract",
        new HashSet<String>() {{ add("isobutanol"); }});

    assertEquals("Patent title", subject.getTitle());
    assertEquals("2008", subject.getYear());
    assertEquals("Q", subject.getApplication());
    assertEquals("simple abstract", subject.getAbs());
    assertEquals("1", subject.getId());
    assertEquals(1, subject.getChemicals().size());
  }

  @DisplayName("it contains all data to get chemicals")
  @Test
  public void itContainsAllDataToGetChemicals() {
    subject = new Patent.Builder()
        .withTitle("Patent title")
        .withYear("2008")
        .withApplication("Q")
        .withAbs("simple abstract")
        .withContents("any contents")
        .build();

    assertEquals("simple abstract\nany contents", subject.contentsToAnalyze());
  }

  @DisplayName("it stores added chemicals")
  @Test
  public void itStoresAddedChemicals() {
    subject = new Patent.Builder()
        .withTitle("Patent title")
        .withYear("2008")
        .withApplication("Q")
        .withAbs("simple abstract")
        .withContents("any contents")
        .build();

    subject.addChemicals(new HashSet<String>() {{ add("isobutanol"); }});
    assertEquals(1, subject.getChemicals().size());
    subject.addChemicals(new HashSet<String>() {{ add("tetrachloromethane"); }});
    assertEquals(2, subject.getChemicals().size());
    assertEquals(0, subject.getChemicals().stream()
        .filter(c -> !c.equals("isobutanol") && !c.equals("tetrachloromethane")).count());
  }

  @DisplayName("it doesn't store duplicated chemicals")
  @Test
  public void itDoesntStoreDuplicatedAChemicals() {
    subject = new Patent.Builder()
        .withTitle("Patent title")
        .withYear("2008")
        .withApplication("Q")
        .withAbs("simple abstract")
        .withContents("any contents")
        .build();

    subject.addChemicals(new HashSet<String>() {{ add("isobutanol"); }});
    assertEquals(1, subject.getChemicals().size());
    subject.addChemicals(new HashSet<String>() {{ add("isobutanol"); }});
    assertEquals(1, subject.getChemicals().size());
  }

  @DisplayName("it checks equality by id")
  @Test
  public void itChecksEqualityById() {
    final Patent subjectA = new Patent.Builder()
        .withTitle("Patent title")
        .withYear("2008")
        .withApplication("Q")
        .withAbs("simple abstract")
        .withContents("any contents")
        .build();

    final Patent subjectB = new Patent.Builder()
        .withTitle("Patent title")
        .withYear("2008")
        .withApplication("Q")
        .withAbs("simple abstract")
        .withContents("any contents")
        .build();

    assertNotEquals(subjectA, subjectB);

    final Patent subjectC = Patent.of(subjectA.getId(),
        "another title",
        "A",
        "2020",
        "weird abstract",
        new HashSet<>());

    assertEquals(subjectA, subjectC);
  }
}
