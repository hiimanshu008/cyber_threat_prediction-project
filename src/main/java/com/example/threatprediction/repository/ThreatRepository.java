package com.example.threatprediction.repository;

import com.example.threatprediction.model.Threat;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThreatRepository extends MongoRepository<Threat, ObjectId> {
}
