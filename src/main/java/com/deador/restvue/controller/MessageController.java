package com.deador.restvue.controller;

import com.deador.restvue.entity.Message;
import com.deador.restvue.entity.Views;
import com.deador.restvue.repository.MessageRepository;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("message")
public class MessageController {
    private final MessageRepository messageRepository;

    @Autowired
    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Message> getMessages() {
        return messageRepository.findAll();
    }

    @GetMapping("/{id}")
    @JsonView(Views.FullMessage.class)
    public Message getMessage(@PathVariable(name = "id") Message message) {
        return message;
    }


    @PostMapping()
    public Message createMessage(@RequestBody Message message) {
        return messageRepository.save(message);
    }

    @PutMapping("/{id}")
    public Message updateMessage(@PathVariable(name = "id") Message messageFromDB,
                                 @RequestBody Message message) {
        BeanUtils.copyProperties(message, messageFromDB, "id");
        return messageRepository.save(messageFromDB);
    }

    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable(name = "id") Message message) {
        messageRepository.delete(message);
    }

}
