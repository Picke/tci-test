package com.tci.repository.service;

import com.tci.entity.Comment;
import com.tci.entity.Document;
import com.tci.util.TestUtil;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static com.tci.util.TestUtil.DOCUMENT_ID_1;
import static com.tci.util.TestUtil.DOCUMENT_ID_2;
import static org.junit.Assert.*;

public class RepositoryServiceImplTest {

    private static RepositoryServiceImpl repositoryService;
    private static List<Document> expectedDocuments = new ArrayList<>();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        String resourcesDirPath = RepositoryServiceImplTest.class.getClassLoader().getResource("").getPath();
        repositoryService = new RepositoryServiceImpl("Test", resourcesDirPath);

        expectedDocuments = TestUtil.getAllDocuments();
    }



    @Test
    public void testGetAllDocuments() throws IOException {
        assertEquals(expectedDocuments, repositoryService.getAllDocuments());
    }

    @Test
    public void testGetDocumentById() throws IOException {
        assertNull(repositoryService.getDocumentById(null));
        assertEquals(expectedDocuments.get(0), repositoryService.getDocumentById(DOCUMENT_ID_1));
        assertEquals(expectedDocuments.get(1), repositoryService.getDocumentById(DOCUMENT_ID_2));
    }

    @Test
    public void testUpdateDocument() throws IOException {
        List<Document> documents = new ArrayList<>();
        for (Document document : expectedDocuments) {
            documents.add(new Document(document));
        }

        assertNull(repositoryService.updateDocument(null));
        assertNull(repositoryService.updateDocument(new Document()));

        documents.get(0).getIndexes().put("another", "another");
        documents.get(0).getComments().add(new Comment(UUID.randomUUID(), 1, "content"));

        documents.get(0).setDocumentName("another name");
        documents.get(0).setDocumentContent("another content");
        documents.get(0).setDocumentTitle("another title");

        repositoryService.updateDocument(documents.get(0));
        assertEquals(documents.get(0), repositoryService.getDocumentById(DOCUMENT_ID_1));
        assertEquals(documents.get(1), repositoryService.getDocumentById(DOCUMENT_ID_2));

        documents.get(0).setIndexes(null);
        documents.get(0).setComments(null);

        repositoryService.updateDocument(documents.get(0));
        assertEquals(documents.get(0), repositoryService.getDocumentById(DOCUMENT_ID_1));
        assertEquals(documents.get(1), repositoryService.getDocumentById(DOCUMENT_ID_2));

        repositoryService.writeAllDocuments(expectedDocuments);
    }

    @Test
    public void testDeleteDocument() throws IOException {
        repositoryService.deleteDocument(null);
        repositoryService.deleteDocument(UUID.fromString("11111111-1111-1111-1111-111111111111"));

        repositoryService.deleteDocument(expectedDocuments.get(0).getDocumentId());
        assertEquals(Collections.singletonList(expectedDocuments.get(1)), repositoryService.getAllDocuments());

        repositoryService.deleteDocument(expectedDocuments.get(1).getDocumentId());
        assertEquals(0, repositoryService.getAllDocuments().size());

        repositoryService.writeAllDocuments(expectedDocuments);
    }

    @Test
//    @Ignore
    public void testSaveDocument() throws IOException {
        List<Document> documents = new ArrayList<>();
        for (Document document : expectedDocuments) {
            documents.add(new Document(document));
        }

        assertNull(repositoryService.saveDocument(null));

        Document newDocument = new Document(documents.get(0));
        newDocument.setDocumentId(null);
        newDocument.getComments().forEach((comment -> comment.setCommentId(null)));

        Document savedDocument = repositoryService.saveDocument(newDocument);
        assertNotNull(savedDocument.getDocumentId());
        assertTrue(savedDocument.getComments().stream().noneMatch(comment -> comment.getCommentId() == null));

        newDocument.setDocumentId(savedDocument.getDocumentId());
        for (int i = 0; i < newDocument.getComments().size(); i++) {
            newDocument.getComments().get(i).setCommentId(
                    newDocument.getComments().get(i).getCommentId()
            );
        }

        assertEquals(savedDocument, newDocument);
        documents.add(savedDocument);
        assertEquals(documents, repositoryService.getAllDocuments());

        repositoryService.writeAllDocuments(expectedDocuments);
    }

}