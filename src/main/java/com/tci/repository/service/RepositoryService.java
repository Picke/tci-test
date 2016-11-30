package com.tci.repository.service;

import com.tci.entity.Document;

import java.io.IOException;
import java.util.List;

public interface RepositoryService {
    List<Document> getAllDocuments() throws IOException;
    Document getDocumentById(Integer documentId) throws IOException;
    Document updateDocument(Document document) throws IOException;
    Document saveDocument(Document document) throws IOException;
    void deleteDocument(Integer documentId) throws IOException;
}
