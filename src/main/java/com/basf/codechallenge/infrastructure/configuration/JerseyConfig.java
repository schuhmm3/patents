package com.basf.codechallenge.infrastructure.configuration;

import com.basf.codechallenge.infrastructure.resources.PatentsResource;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {

  public JerseyConfig() {
    packages("org.glassfish.jersey.examples.multipart");
    register(PatentsResource.class);
    register(MultiPartFeature.class);
  }
}
