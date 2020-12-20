package com.basf.codechallenge.infrastructure.configuration;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class MongoConfig {

  @Value("${mongo.host}")
  private String mongoHost;

  @Value("${mongo.port}")
  private int mongoPort;

  @Value("${mongo.database}")
  private String mongoDatabase;

  @Bean
  public MongoClientFactoryBean mongo() {
    MongoClientFactoryBean mongo = new MongoClientFactoryBean();
    mongo.setHost(mongoHost);
    mongo.setPort(mongoPort);
    return mongo;
  }

  @Bean
  public MongoDatabaseFactory mongoDatabaseFactory(final MongoClient mongo) {
    return new SimpleMongoClientDatabaseFactory(mongo, mongoDatabase);
  }

  @Bean
  public MongoTemplate mongoTemplate(final MongoDatabaseFactory mongoDatabaseFactory) {
    return new MongoTemplate(mongoDatabaseFactory);
  }
}
