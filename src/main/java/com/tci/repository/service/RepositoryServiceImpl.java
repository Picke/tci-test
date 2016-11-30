package com.tci.repository.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tci.entity.Document;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class RepositoryServiceImpl implements RepositoryService {
    private static final Logger LOGGER = Logger.getLogger(RepositoryServiceImpl.class);
    private static final String FILE_NAME = "documents.json";

    private String repositoryName;
    private String resourcesDirPath;

    @Autowired
    public RepositoryServiceImpl(String repositoryName, String resourcesDirPath) {
        this.repositoryName = repositoryName;
        this.resourcesDirPath = resourcesDirPath;
    }

    public Document getDocumentById(UUID documentId) throws IOException {
        List<Document> documents;
        try {
            documents = getAllDocuments();
        } catch (IOException e) {
            LOGGER.error(e);
            throw e;
        }
        return documents
                .stream()
                .filter(document -> document.getDocumentId().equals(documentId))
                .findFirst()
                .orElse(null);
    }


    @Override
    public Document updateDocument(Document document) throws IOException {
        if (document.getDocumentId() == null) {
            return null;
        }
        List<Document> documents;
        try {
            documents = getAllDocuments();
        } catch (IOException e) {
            LOGGER.error(e);
            throw e;
        }
        if (documents.stream().anyMatch(doc -> doc.getDocumentId().equals(document.getDocumentId()))) {
            for (Document doc : documents) {
                if (doc.getDocumentId().equals(document.getDocumentId())) {
                    doc.setDocumentName(document.getDocumentName());
                    doc.setDocumentTitle(document.getDocumentTitle());
                    doc.setIndexes(document.getIndexes());
                    doc.setDocumentContent(document.getDocumentContent());
                    doc.setComments(document.getComments());

                    try {
                        writeAllDocuments(documents);
                        return doc;
                    } catch (IOException e) {
                        LOGGER.error(e);
                        throw e;
                    }
                }
            }
        }
        return null;

    }

    @Override
    public Document saveDocument(Document document) throws IOException{
        if (document.getDocumentId() == null) {
            return null;
        }
        try {
            List<Document> documents = getAllDocuments();
            documents.add(document);
            writeAllDocuments(documents);
            return document;
        } catch (IOException e) {
            LOGGER.error(e);
            throw e;
        }
    }

    @Override
    public void deleteDocument(UUID documentId) throws IOException {
        try {
            List<Document> documents = getAllDocuments();
            if (documents.stream().anyMatch(doc -> doc.getDocumentId().equals(documentId))) {
                documents.removeIf((doc) -> doc.getDocumentId().equals(documentId));
                writeAllDocuments(documents);
            }
        } catch (IOException e) {
            LOGGER.error(e);
            throw e;
        }
    }

    @Override
    public List<Document> getAllDocuments() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(resourcesDirPath + "/repository" + repositoryName + "/" + FILE_NAME),
                new TypeReference<List<Document>>() {
                });
    }

    private void writeAllDocuments(List<Document> documents) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(resourcesDirPath + "/repository" + repositoryName + "/" + FILE_NAME),
                documents);
    }
}