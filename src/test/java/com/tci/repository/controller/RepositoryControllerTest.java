package com.tci.repository.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tci.entity.Comment;
import com.tci.entity.Document;
import com.tci.util.TestUtil;
import com.tci.repository.service.RepositoryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.tci.util.TestUtil.DOCUMENT_ID_1;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/repository-test-context.xml")
@WebAppConfiguration
@EnableWebMvc
public class RepositoryControllerTest {
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Document> expectedDocuments;

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private RepositoryService repositoryService;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
        expectedDocuments = TestUtil.getAllDocuments();
    }

    @Test
    public void testGetAllDocuments() throws Exception {
        when(repositoryService.getAllDocuments()).thenReturn(expectedDocuments);

        byte[] bytesResponse = mockMvc.perform(get("/repository/documents")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        List<Document> response = objectMapper.readValue(bytesResponse, new TypeReference<List<Document>>() {
        });
        assertEquals(expectedDocuments, response);

        when(repositoryService.getAllDocuments()).thenThrow(new IOException());
        mockMvc.perform(get("/repository/documents"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testGetDocumentById() throws Exception {
        when(repositoryService.getDocumentById(DOCUMENT_ID_1)).thenReturn(expectedDocuments.get(0));

        byte[] bytesResponse = mockMvc.perform(get("/repository/documents/" + DOCUMENT_ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        Document response = objectMapper.readValue(bytesResponse, Document.class);
        assertEquals(expectedDocuments.get(0), response);

        when(repositoryService.getDocumentById(DOCUMENT_ID_1)).thenReturn(null);
        mockMvc.perform(get("/repository/documents/" + DOCUMENT_ID_1))
                .andExpect(status().isNotFound());

        when(repositoryService.getDocumentById(DOCUMENT_ID_1)).thenThrow(new IOException());
        mockMvc.perform(get("/repository/documents/" + DOCUMENT_ID_1))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testUpdateDocument() throws Exception {
        Document updatedDocument = expectedDocuments.get(0);
        updatedDocument.getIndexes().put("another", "another");
        updatedDocument.getComments().add(new Comment(UUID.randomUUID(), 1, "content"));

        updatedDocument.setDocumentName("another name");
        updatedDocument.setDocumentContent("another content");
        updatedDocument.setDocumentTitle("another title");

        when(repositoryService.updateDocument(updatedDocument)).thenReturn(updatedDocument);
        byte[] bytesResponse = mockMvc.perform(put("/repository/documents/" + DOCUMENT_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(updatedDocument))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        Document response = objectMapper.readValue(bytesResponse, Document.class);
        assertEquals(updatedDocument, response);

        when(repositoryService.updateDocument(updatedDocument)).thenReturn(null);
        mockMvc.perform(put("/repository/documents/" + DOCUMENT_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(updatedDocument)))
                .andExpect(status().isNotFound());

        when(repositoryService.updateDocument(updatedDocument)).thenThrow(new IOException());
        mockMvc.perform(put("/repository/documents/" + DOCUMENT_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(updatedDocument)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testSaveDocument() throws Exception {
        Document newDocumentWithoutIds = expectedDocuments.get(0);
        newDocumentWithoutIds.setDocumentId(null);
        newDocumentWithoutIds.getComments().forEach((comment) -> comment.setCommentId(null));

        Document newDocumentWithIds = new Document(newDocumentWithoutIds);
        newDocumentWithIds.setDocumentId(UUID.randomUUID());
        newDocumentWithIds.getComments().forEach((comment) -> comment.setCommentId(UUID.randomUUID()));

        when(repositoryService.saveDocument(newDocumentWithoutIds)).thenReturn(newDocumentWithIds);
        byte[] bytesResponse = mockMvc.perform(post("/repository/documents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(newDocumentWithoutIds))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        Document response = objectMapper.readValue(bytesResponse, Document.class);
        assertEquals(newDocumentWithIds, response);

        when(repositoryService.saveDocument(newDocumentWithoutIds)).thenReturn(null);
        mockMvc.perform(post("/repository/documents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(newDocumentWithoutIds)))
                .andExpect(status().isBadRequest());

        when(repositoryService.saveDocument(newDocumentWithoutIds)).thenThrow(new IOException());
        mockMvc.perform(post("/repository/documents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(newDocumentWithoutIds)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testDeleteDocument() throws Exception {
        mockMvc.perform(delete("/repository/documents/" + DOCUMENT_ID_1))
                .andExpect(status().isNoContent());
        verify(repositoryService, times(1)).deleteDocument(DOCUMENT_ID_1);

        doThrow(new IOException()).when(repositoryService).deleteDocument(DOCUMENT_ID_1);
        mockMvc.perform(delete("/repository/documents/" + DOCUMENT_ID_1))
                .andExpect(status().isInternalServerError());

    }
}