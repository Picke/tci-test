package com.tci.repository.controller;

import com.tci.entity.Document;
import com.tci.repository.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/repository/documents")
public class RepositoryController {

    private RepositoryService service;

    @Autowired
    public RepositoryController(RepositoryService repositoryService) {
        service = repositoryService;
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public
    ResponseEntity<List<Document>> getAllDocuments() {
        try {
            return new ResponseEntity<>(service.getAllDocuments(), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{documentId}", method = RequestMethod.GET, produces = "application/json")
    public
    ResponseEntity<Document> getDocumentById(@PathVariable UUID documentId) {
        try {
            Document document = service.getDocumentById(documentId);
            return document != null ?
                    new ResponseEntity<>(document, HttpStatus.OK) :
                    new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{documentId}", method = RequestMethod.PUT, consumes = "application/json")
    public
    ResponseEntity<Document> updateDocument(@PathVariable UUID documentId, @RequestBody Document document) {
        try {
            document.setDocumentId(documentId);
            Document result = service.updateDocument(document);
            return (result != null) ?
                    new ResponseEntity<>(result, HttpStatus.OK) :
                    new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public
    ResponseEntity<Document> saveDocument(@RequestBody Document document) {
        try {
            Document savedDocument = service.saveDocument(document);
            return (savedDocument != null) ?
                    new ResponseEntity<>(savedDocument, HttpStatus.CREATED) :
                    new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{documentId}", method = RequestMethod.DELETE)
    public
    ResponseEntity<Void> deleteDocument(@PathVariable UUID documentId) {
        try {
            service.deleteDocument(documentId);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
