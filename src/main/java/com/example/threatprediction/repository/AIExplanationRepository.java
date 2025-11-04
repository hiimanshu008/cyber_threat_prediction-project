package com.example.threatprediction.repository;

import com.example.threatprediction.model.AIExplanation;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AIExplanationRepository extends MongoRepository<AIExplanation, ObjectId> {
    Optional<AIExplanation> findTopByThreatIdOrderByCreatedAtDesc(ObjectId threatId);
}
