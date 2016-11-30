package com.tci.repository.service;

import com.tci.entity.Document;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface RepositoryService {
    List<Document> getAllDocuments() throws IOException;
    Document getDocumentById(UUID documentId) throws IOException;
    Document updateDocument(Document document) throws IOException;
    Document saveDocument(Document document) throws IOException;
    void deleteDocument(UUID documentId) throws IOException;
}
