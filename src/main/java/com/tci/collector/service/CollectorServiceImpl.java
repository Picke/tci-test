package com.tci.collector.service;

import com.tci.collector.dao.CollectorDAO;
import com.tci.entity.Document;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Transactional
public class CollectorServiceImpl implements CollectorService {

    private static final Logger LOGGER = Logger.getLogger(CollectorServiceImpl.class);

    private RestTemplate restTemplate;

    private final CollectorDAO collectorDAO;
    private HashMap<String, String> repositoryNameUrlMap = new HashMap<>();

    private ExecutorService executorService;

    @Required
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    public CollectorServiceImpl(CollectorDAO collectorDAO, String repositoryNames, String repositoryUrls) {
        this.collectorDAO = collectorDAO;

        List<String> repositoryNameList = Arrays.asList(repositoryNames.split(","));
        List<String> repositoryUrlList = Arrays.asList(repositoryUrls.split(","));
        for (int i = 0; i < repositoryNameList.size(); i++) {
            repositoryNameUrlMap.put(repositoryNameList.get(i), repositoryUrlList.get(i));
        }

        this.executorService = Executors.newFixedThreadPool(repositoryUrlList.size());
    }

    public List<Document> getAllDocuments() {
        LOGGER.info("Getting all documents");
        List<Document> documents = new ArrayList<>();
        List<Future<List<Document>>> futures = new ArrayList<>();
        for (String repositoryUrl : repositoryNameUrlMap.values()) {
            futures.add(executorService.submit(() -> Arrays.asList(
                    restTemplate.getForEntity(repositoryUrl, Document[].class).getBody())));
        }
        for (Future<List<Document>> future : futures) {
            try {
                documents.addAll(future.get());
            } catch (Exception e) {
                LOGGER.warn(e);
            }
        }
        return documents;
    }

    @Transactional
    @Override
    public Document getDocumentById(UUID documentId) {
        LOGGER.info(String.format("Getting document by id: [%s]", documentId));

        Document document = collectorDAO.findById(documentId);
        if (document != null) {
            LOGGER.info(String.format("Getting from cache: [%s]", document));
            return document;
        }

        List<Future<ResponseEntity<Document>>> futures = new ArrayList<>();
        for (String repositoryUrl : repositoryNameUrlMap.values()) {
            futures.add(executorService.submit(() ->
                    restTemplate.getForEntity(repositoryUrl + "/" + documentId, Document.class)));
        }
        for (Future<ResponseEntity<Document>> future : futures) {
            try {
                ResponseEntity<Document> responseEntity = future.get();
                if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                    collectorDAO.merge(responseEntity.getBody());
                    LOGGER.info(String.format("Document [%s] saved to cache", responseEntity.getBody()));

                    return responseEntity.getBody();
                }
            } catch (Exception e) {
                LOGGER.warn(e);
            }
        }
        return null;
    }

    @Override
    public Document saveDocument(String repositoryName, Document document) {
        LOGGER.info(String.format("Saving document [%s] to repository [%s]", document, repositoryName));
        try {
            ResponseEntity<Document> responseEntity = restTemplate.postForEntity(repositoryNameUrlMap.get(repositoryName), document, Document.class);
            if (responseEntity.getStatusCode().equals(HttpStatus.CREATED)) {
                collectorDAO.merge(responseEntity.getBody());
                LOGGER.info(String.format("Document [%s] saved to cache", responseEntity.getBody()));
                return responseEntity.getBody();
            }
        } catch (RestClientException ex) {
            LOGGER.error(ex);
        }
        return null;
    }

    @Override
    public Document updateDocument(UUID documentId, Document document) {
        LOGGER.info(String.format("Updating document with document id: [%s]", documentId));

        document.setDocumentId(documentId);

        List<Future<ResponseEntity>> futures = new ArrayList<>();

        for (String repositoryUrl : repositoryNameUrlMap.values()) {
            futures.add(executorService.submit(() -> {
                HttpEntity<Document> entity = new HttpEntity<>(document);
                return restTemplate.exchange(repositoryUrl + "/" + documentId, HttpMethod.PUT, entity, Document.class);
            }));
        }

        for (Future<ResponseEntity> future : futures) {
            try {
                ResponseEntity responseEntity = future.get();
                if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                    collectorDAO.merge(document);
                    LOGGER.info(String.format("Document [%s] saved to cache", document));
                    return document;
                }
            } catch (RestClientException | InterruptedException | ExecutionException e) {
                LOGGER.warn(e);
            }
        }

        return null;
    }

    @Override
    public void deleteDocument(UUID documentId) {
        LOGGER.info(String.format("Deleting document with documentId: [%s]", documentId));
        collectorDAO.delete(documentId);

        List<Future> futures = new ArrayList<>();
        for (String repositoryUrl : repositoryNameUrlMap.values()) {
            futures.add(executorService.submit(() ->
                    restTemplate.delete(repositoryUrl + "/" + documentId)));
        }
        for (Future future : futures) {
            try {
                future.get();
            } catch (Exception ex) {
                LOGGER.warn(ex);
            }
        }

    }
}

