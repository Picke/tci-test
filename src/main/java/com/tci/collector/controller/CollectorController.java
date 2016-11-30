package com.tci.collector.controller;

import com.tci.collector.service.CollectorService;
import com.tci.entity.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/documents")
public class CollectorController {

    private CollectorService service;

    @Autowired
    public CollectorController(CollectorService collectorService) {
        this.service = collectorService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Document>> getAllDocuments() throws ExecutionException, InterruptedException {
        List<Document> documents = service.getAllDocuments();
        return !documents.isEmpty() ?
                new ResponseEntity<>(documents, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/{documentId}", method = RequestMethod.GET)
    public
    ResponseEntity<Document> getDocumentById(@PathVariable UUID documentId) {
        Document document = service.getDocumentById(documentId);
        return document != null ?
                new ResponseEntity<>(document, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public
    ResponseEntity<Document> saveDocument(
            @RequestBody Document document,
            @RequestParam(value = "repositoryName", defaultValue = "A") String repositoryName) {
        Document doc = service.saveDocument(repositoryName, document);
        return (doc != null) ?
                new ResponseEntity<>(doc, HttpStatus.CREATED) :
                new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @RequestMapping(value = "/{documentId}", method = RequestMethod.PUT, consumes = "application/json")
    public
    ResponseEntity<Document> updateDocument(@PathVariable UUID documentId, @RequestBody Document document) {
        Document result = service.updateDocument(documentId, document);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/{documentId}", method = RequestMethod.DELETE, consumes = "application/json")
    public
    ResponseEntity<Void> deleteDocument(@PathVariable UUID documentId) {
        service.deleteDocument(documentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
