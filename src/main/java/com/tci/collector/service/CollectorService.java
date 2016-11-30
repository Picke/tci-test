package com.tci.collector.service;

import com.tci.entity.Document;

import java.util.List;


public interface CollectorService {
    List<Document> getAllDocuments();
    Document getDocumentById(Integer documentId);
    Document saveDocument(String repositoryName, Document document);
    Document updateDocument(Integer documentId, Document document);
    void deleteDocument(Integer documentId);
}
