package com.tci.util;

import com.tci.entity.Comment;
import com.tci.entity.Document;

import java.util.*;

public class TestUtil {
    public static final UUID DOCUMENT_ID_1 = UUID.fromString("c0ae21f2-b70a-11e6-80f5-76304dec7eb7");
    public static final UUID DOCUMENT_ID_2 = UUID.fromString("e104bb46-b70a-11e6-80f5-76304dec7eb7");

    public static List<Document> getAllDocuments() {
        List<Document> documents = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment(UUID.fromString("cb6348d4-b70a-11e6-80f5-76304dec7eb7"), 1, "Comment content 1"));
        comments.add(new Comment(UUID.fromString("d1e6053e-b70a-11e6-80f5-76304dec7eb7"), 2, "Comment content 2"));
        Map<String, String> indexes = new HashMap<>();
        indexes.put("first", "1-100");
        indexes.put("second", "101-200");
        indexes.put("third", "201-300");
        Document document = new Document(
                DOCUMENT_ID_1,
                "Document 1",
                "Title 1",
                indexes,
                "Content 1",
                comments
        );
        documents.add(document);

        comments = new ArrayList<>();
        comments.add(new Comment(UUID.fromString("e5870534-b70a-11e6-80f5-76304dec7eb7"), 2, "Comment content 3"));
        comments.add(new Comment(UUID.fromString("ea7188e4-b70a-11e6-80f5-76304dec7eb7"), 3, "Comment content 4"));
        indexes = new HashMap<>();
        indexes.put("first", "1-100");
        indexes.put("second", "101-200");
        document = new Document(
                DOCUMENT_ID_2,
                "Document 2",
                "Title 2",
                indexes,
                "Content 2",
                comments
        );
        documents.add(document);

        return documents;
    }
}
