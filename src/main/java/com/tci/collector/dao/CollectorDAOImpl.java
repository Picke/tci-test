package com.tci.collector.dao;

import com.tci.entity.Document;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import java.util.UUID;

public class CollectorDAOImpl extends HibernateDaoSupport implements CollectorDAO {

    @Override
    public Document findById(UUID documentId) {
        return getHibernateTemplate().get(Document.class, documentId);
    }

    @Override
    public void merge(Document document) {
        getHibernateTemplate().merge(document);
    }

    @Override
    public void delete(UUID documentId) {
        Document document = findById(documentId);
        if (document != null) {
            getHibernateTemplate().delete(document);
        }
    }

}
