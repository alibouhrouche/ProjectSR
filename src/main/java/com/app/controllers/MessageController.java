package com.app.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.entities.Client;
import com.app.entities.Message;
import com.app.services.MessageService;
import com.app.services.SortService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/client/messages")
public class MessageController {
    private final SortService sService;
    private final MessageService messageService;
    @GetMapping(produces = { "application/json", "application/xml" })
    public ResponseEntity<List<Message>> getMessages(
        @RequestParam Optional<Integer> id,
        @RequestParam(defaultValue = "1") int _page,
        @RequestParam(defaultValue = "10") int _size,
        @RequestParam(required = false) String[] _sort,
        @RequestParam(required = false) String[] _order
    ) {
        try {
            HttpHeaders responseHeaders = new HttpHeaders();
            if(_sort == null){
                _sort = new String[]{"id"};
                _order = new String[]{"desc"};
            }
            Pageable paging = sService.getSorter(_page, _size, _sort, _order);
            if(paging == null)
                return ResponseEntity.ok(messageService.getMessages(id));
            Page<Message> pageCarnets = messageService.getMessages(id,paging);
            List<Message> carnets = pageCarnets.getContent();
            responseHeaders.add("x-total-count",String.valueOf(pageCarnets.getTotalElements()));
            return ResponseEntity.ok().headers(responseHeaders).body(carnets);
        }catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(path="/my",produces = { "application/json", "application/xml" })
    public ResponseEntity<List<Message>> getMyMessages(
        Authentication authentication,
        @RequestParam(defaultValue = "1") int _page,
        @RequestParam(defaultValue = "10") int _size,
        @RequestParam(required = false) String[] _sort,
        @RequestParam(required = false) String[] _order
    ) {
        Client client = (Client)authentication.getPrincipal();
        if(client == null)
            return ResponseEntity.ok(new ArrayList<Message>());
        try {
            Optional<Integer> id = Optional.of(client.getId());
            HttpHeaders responseHeaders = new HttpHeaders();
            if(_sort == null){
                _sort = new String[]{"id"};
                _order = new String[]{"desc"};
            }
            Pageable paging = sService.getSorter(_page, _size, _sort, _order);
            if(paging == null)
                return ResponseEntity.ok(messageService.getMessages(id));
            Page<Message> pageCarnets = messageService.getMessages(id,paging);
            List<Message> carnets = pageCarnets.getContent();
            responseHeaders.add("x-total-count",String.valueOf(pageCarnets.getTotalElements()));
            return ResponseEntity.ok().headers(responseHeaders).body(carnets);
        }catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(path={"/my/{id}","/{id}"},produces = { "application/json", "application/xml" })
    public ResponseEntity<Optional<Message>> getMessage(@PathVariable int id) {
        return ResponseEntity.ok(messageService.getMessage(id));
    }
    @PostMapping(path={"","/my"},consumes = {MediaType.TEXT_PLAIN_VALUE},produces = {"application/json", "application/xml"})
    public ResponseEntity<Message> postMessage(Authentication authentication,@RequestBody String message) {
        Client client = (Client)authentication.getPrincipal();
        if(client == null){
            return ResponseEntity.badRequest().build();
        }
        Message msg = new Message(message, client);
        return ResponseEntity.ok(messageService.sendMessage(msg));
    }
    @DeleteMapping(path={"/my/{id}","/{id}"},produces = {"application/json", "application/xml"})
    public ResponseEntity<Boolean> deleteMessage(Authentication authentication,@PathVariable int id) {
        return ResponseEntity.ok(messageService.deleteMessage((Client)authentication.getPrincipal(), id));
    }
}
