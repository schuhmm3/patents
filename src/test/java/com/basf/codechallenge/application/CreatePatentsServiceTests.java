package com.basf.codechallenge.application;

import com.basf.codechallenge.application.patents.CreatePatentsService;
import com.basf.codechallenge.application.patents.ExtractPatentsArchiveService;
import com.basf.codechallenge.domain.chemicals.ExtractChemicalsService;
import com.basf.codechallenge.domain.patents.Patent;
import com.basf.codechallenge.domain.patents.PatentFactory;
import com.basf.codechallenge.domain.patents.PatentRepository;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.HashSet;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

public class CreatePatentsServiceTests {
  private static final String FILE_A_NAME = "US06177006B1.xml";
  private static final String FILE_B_NAME = "US06178393B1.xml";
  private static final String FILE_A =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
      "<questel-patent-document lang=\"en\" date-produced=\"20180805\" " +
      "produced-by=\"Questel\" schema-version=\"3.23\" file=\"US06177006B1.xml\">" +
      "<bibliographic-data lang=\"en\">" +
      "<invention-title format=\"original\" lang=\"en\" id=\"title_en\">" +
      "Filtering device</invention-title>" +
      "</bibliographic-data>" +
      "<abstract format=\"original\" lang=\"en\" id=\"abstr_en\">" +
      "<p><br/>Abstract.</p>" +
      "</abstract>" +
      "<description format=\"original\" lang=\"en\" id=\"desc_en\">" +
      "<heading>BACKGROUND OF THE INVENTION</heading>" +
      "</description>" +
      "</questel-patent-document>";
  private static final String FILE_B =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
      "<questel-patent-document lang=\"en\" date-produced=\"20180805\" " +
      "produced-by=\"Questel\" schema-version=\"3.23\" file=\"US06178393B1.xml\">" +
      "<bibliographic-data lang=\"en\">" +
      "<invention-title format=\"original\" lang=\"en\" id=\"title_en\">" +
      "Pump station control system and method</invention-title>" +
      "</bibliographic-data>" +
      "<abstract format=\"original\" lang=\"en\" id=\"abstr_en\">" +
      "<p><br/>Another abstract.</p>" +
      "</abstract>" +
      "<description format=\"original\" lang=\"en\" id=\"desc_en\">" +
      "Description." +
      "</description>" +
      "</questel-patent-document>";
  private ExtractChemicalsService extractChemicalsService;
  private ExtractPatentsArchiveService extractPatentsArchiveService;
  private PatentFactory patentFactory;
  private PatentRepository patentRepository;
  private CreatePatentsService subject;

  private Stream<Pair<String, String>> files;
  private InputStream input;
  private Patent patentA;
  private Patent patentB;

  @BeforeEach
  public void setup() {
    extractChemicalsService = mock(ExtractChemicalsService.class);
    extractPatentsArchiveService = mock(ExtractPatentsArchiveService.class);
    patentFactory = mock(PatentFactory.class);
    patentRepository = mock(PatentRepository.class);
    subject = new CreatePatentsService(extractChemicalsService,
        patentRepository,
        extractPatentsArchiveService,
        patentFactory);

    final Stream.Builder<Pair<String,String>> builder = Stream.builder();
    builder.add(new ImmutablePair<>(FILE_A_NAME, FILE_A));
    builder.add(new ImmutablePair<>(FILE_B_NAME, FILE_B));
    files = builder.build();
    input = Thread.currentThread()
        .getContextClassLoader()
        .getResourceAsStream("patents.zip");

    patentA = new Patent.Builder()
        .withTitle("Filtering device")
        .withApplication("Questel")
        .withYear("2018")
        .withAbs("<p><br/>Abstract.</p>")
        .withContents("<heading>BACKGROUND OF THE INVENTION</heading>")
        .build();

    patentB = new Patent.Builder()
        .withTitle("Pump station control system and method")
        .withApplication("Questel")
        .withYear("2018")
        .withAbs("<p><br/>Another abstract.</p>")
        .withContents("Description.")
        .build();
  }

  @DisplayName("it stores patents with chemicals")
  @Test
  public void itStoresPatentsWithChemicals() {
    when(extractPatentsArchiveService.extract(input)).thenReturn(files);
    when(patentFactory.build(FILE_A_NAME,FILE_A)).thenReturn(patentA);
    when(patentFactory.build(FILE_B_NAME,FILE_B)).thenReturn(patentB);
    when(extractChemicalsService.runNamedEntityRecognitionOver(patentA.contentsToAnalyze()))
        .thenReturn(new HashSet<>());
    when(extractChemicalsService.runNamedEntityRecognitionOver(patentB.contentsToAnalyze()))
        .thenReturn(new HashSet<String>() {{ add("isobutanol");}});

    subject.createPatentsFromArchive(input);

    verify(extractPatentsArchiveService).extract(input);
    verify(patentFactory).build(FILE_A_NAME,FILE_A);
    verify(patentFactory).build(FILE_B_NAME,FILE_B);
    verify(extractChemicalsService).runNamedEntityRecognitionOver(patentA.contentsToAnalyze());
    verify(extractChemicalsService).runNamedEntityRecognitionOver(patentB.contentsToAnalyze());
    verify(patentRepository).save(argThat(p -> p.equals(patentA) && p.getChemicals().size() == 0));
    verify(patentRepository).save(argThat(p -> p.equals(patentB) && p.getChemicals().size() == 1));
  }

  @DisplayName("it doesnt fail on null patents")
  @Test
  public void itDoesntFailOnNullPatents() {
    when(extractPatentsArchiveService.extract(input)).thenReturn(files);
    when(patentFactory.build(FILE_A_NAME,FILE_A)).thenReturn(patentA);
    when(patentFactory.build(FILE_B_NAME,FILE_B)).thenReturn(null);
    when(extractChemicalsService.runNamedEntityRecognitionOver(patentA.contentsToAnalyze()))
        .thenReturn(new HashSet<>());

    subject.createPatentsFromArchive(input);

    verify(extractPatentsArchiveService).extract(input);
    verify(patentFactory).build(FILE_A_NAME,FILE_A);
    verify(patentFactory).build(FILE_B_NAME,FILE_B);
    verify(extractChemicalsService, times(1)).runNamedEntityRecognitionOver(any());
    verify(patentRepository, times(1)).save(any());
  }
}
