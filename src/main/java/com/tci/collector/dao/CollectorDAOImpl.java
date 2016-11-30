package com.tci.collector.dao;

import com.tci.entity.Document;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

public class CollectorDAOImpl extends HibernateDaoSupport implements CollectorDAO {

    @Override
    public Document findById(Integer documentId) {
        return getHibernateTemplate().get(Document.class, documentId);
    }

    @Override
    public void merge(Document document) {
        getHibernateTemplate().merge(document);
    }

    @Override
    public void delete(Integer documentId) {
        Document document = findById(documentId);
        if (document != null) {
            getHibernateTemplate().delete(document);
        }
    }

}
