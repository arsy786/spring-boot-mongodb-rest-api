package dev.arsalaan.springbootmongodbrestapi.config;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MongoDBConnectionInitializr implements CommandLineRunner {

  private MongoTemplate mongoTemplate;

  public MongoDBConnectionInitializr(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public void run(String... args) throws Exception {
    try {
      String db = mongoTemplate.getDb().getName();
      Set<String> collections = mongoTemplate.getCollectionNames();
      log.info("MongoDB connected to database: {}", db);
      log.info("MongoDB database has collections: {}", collections);
    } catch (Exception e) {
      log.info("MongoDB connection error: " + e.getMessage());
    }
  }

}
