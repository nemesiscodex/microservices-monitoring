package io.nemesiscodex.web.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/echo")
public class EchoController {

    @GetMapping(produces = "application/json")
    public EchoResponse echo(@RequestParam(value = "message") String message,
                       @RequestParam(value = "error", required = false) Boolean error) {
        if (error != null && error) {
            throw new RuntimeException("There was an error. (parameter error=true)");
        }
        return new EchoResponse(message);
    }

    class EchoResponse {
        private String message;

        EchoResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public EchoResponse setMessage(String message) {
            this.message = message;
            return this;
        }
    }

}
