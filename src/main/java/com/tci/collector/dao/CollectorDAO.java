package com.tci.collector.dao;

import com.tci.entity.Document;

import javax.print.Doc;

public interface CollectorDAO {
    void merge(Document document);
    Document findById(Integer documentId);
    void delete(Integer documentId);
}
