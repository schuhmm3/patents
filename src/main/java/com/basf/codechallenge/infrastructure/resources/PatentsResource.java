package com.basf.codechallenge.infrastructure.resources;

import com.basf.codechallenge.application.patents.CreatePatentsService;
import com.basf.codechallenge.application.patents.DeleteAllPatentsService;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static javax.ws.rs.core.Response.Status.SERVICE_UNAVAILABLE;

@Path("/patents")
public class PatentsResource {
  private static final Logger LOG = LoggerFactory.getLogger(PatentsResource.class);

  @Autowired
  private CreatePatentsService createPatentsService;
  @Autowired
  private DeleteAllPatentsService deleteAllPatentsService;
  @Autowired
  private TaskExecutor executor;

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public void create(@Suspended final AsyncResponse asyncResponse,
      @FormDataParam("data") InputStream zipped) {
    LOG.info("Incoming request to /archives POST method.");
    CompletableFuture
        .runAsync(() -> createPatentsService.createPatentsFromArchive(zipped), executor)
        .thenApply((result) -> asyncResponse.resume(Response.ok().build()));
    asyncResponse.setTimeout(3, TimeUnit.HOURS);
    asyncResponse.setTimeoutHandler(ar ->
        ar.resume(Response.status(SERVICE_UNAVAILABLE).entity("Operation timed out").build())
    );
  }

  @DELETE
  public void delete() {
    deleteAllPatentsService.deleteAllPatents();
  }
}
