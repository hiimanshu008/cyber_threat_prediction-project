package com.example.threatprediction.repository;

import com.example.threatprediction.model.SystemConfig;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemConfigRepository extends MongoRepository<SystemConfig, ObjectId> {
    Optional<SystemConfig> findByConfigNameAndActiveTrue(String configName);
}
