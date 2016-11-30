package com.tci.collector.service;

import com.tci.entity.Document;

import java.util.List;
import java.util.UUID;


public interface CollectorService {
    List<Document> getAllDocuments();
    Document getDocumentById(UUID documentId);
    Document saveDocument(String repositoryName, Document document);
    Document updateDocument(UUID documentId, Document document);
    void deleteDocument(UUID documentId);
}
