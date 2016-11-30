package com.tci.collector.dao;

import com.tci.entity.Document;

import javax.print.Doc;
import java.util.UUID;

public interface CollectorDAO {
    void merge(Document document);
    Document findById(UUID documentId);
    void delete(UUID documentId);
}
