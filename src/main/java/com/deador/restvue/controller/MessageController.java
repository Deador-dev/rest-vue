package com.deador.restvue.controller;

import com.deador.restvue.exception.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("message")
public class MessageController {
    private int counter = 4;

    private List<Map<String, String>> messages = new ArrayList<>() {{
        add(new HashMap<>() {{
            put("id", "1");
            put("text", "First Message");
        }});
        add(new HashMap<>() {{
            put("id", "2");
            put("text", "Second Message");
        }});
        add(new HashMap<>() {{
            put("id", "3");
            put("text", "Third Message");
        }});
    }};

    @GetMapping
    public List<Map<String, String>> getMessages() {
        return messages;
    }

    @GetMapping("/{id}")
    public Map<String, String> getMessage(@PathVariable(name = "id") String id) {
        return getMessageById(id);
    }


    @PostMapping()
    public Map<String, String> createMessage(@RequestBody Map<String, String> message) {
        message.put("id", String.valueOf(counter++));
        messages.add(message);

        return message;
    }

    @PutMapping("/{id}")
    public Map<String, String> updateMessage(@PathVariable(name = "id") String id,
                                             @RequestBody Map<String, String> message) {
        Map<String, String> messageFromDB = getMessage(id);

        messageFromDB.putAll(message);
        messageFromDB.put("id", id);

        return messageFromDB;
    }

    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable(name = "id") String id) {
        Map<String, String> message = getMessageById(id);

        messages.remove(message);
    }


    private Map<String, String> getMessageById(String id) {
        return messages.stream()
                .filter(message -> message.get("id").equals(id))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }
}
