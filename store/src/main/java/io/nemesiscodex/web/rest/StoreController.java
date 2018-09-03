package io.nemesiscodex.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.nemesiscodex.domain.Message;
import io.nemesiscodex.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/store")
public class StoreController {

    private final MessageRepository messageRepository;

    @Autowired
    public StoreController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @PostMapping(value = "", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @Timed
    public Message createMessage(@RequestParam("message") String message,
                                 @RequestParam(value = "shouldFail", required = false) boolean shouldFail) {

        Message newMessage = new Message(message, Instant.now());

        if(shouldFail) {
            throw new RuntimeException("Error. shouldFail = true");
        }

        newMessage = messageRepository.save(newMessage);

        return newMessage;
    }
}
