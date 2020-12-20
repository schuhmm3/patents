package com.basf.codechallenge.infrastructure.repositories;

import com.basf.codechallenge.domain.patents.Patent;
import com.basf.codechallenge.domain.patents.PatentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MongoDBPatentRepository implements PatentRepository {

  private final MongoTemplate mongoTemplate;

  @Autowired
  public MongoDBPatentRepository(final MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public String save(Patent patent) {
    final Patent saved = mongoTemplate.save(patent);
    return saved.getId();
  }

  @Override
  public void clear() {
    mongoTemplate.dropCollection(Patent.class);
  }
}
