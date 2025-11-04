package com.example.threatprediction.repository;

import com.example.threatprediction.model.LogEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface LogEntryRepository extends MongoRepository<LogEntry, ObjectId> {
    List<LogEntry> findBySourceIPAndTimestampBetween(String sourceIP, Instant start, Instant end);
}
