package io.nemesiscodex.repository;

import io.nemesiscodex.domain.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {

}
