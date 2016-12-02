package com.tci.collector.dao;

import com.tci.util.TestUtil;
import com.tci.entity.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.tci.util.TestUtil.DOCUMENT_ID_1;
import static com.tci.util.TestUtil.DOCUMENT_ID_2;
import static org.junit.Assert.*;

@ContextConfiguration(locations = "classpath:collector-dao-test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class CollectorDAOImplTest {
    @Autowired
    private CollectorDAOImpl collectorDAO;

    private List<Document> expectedDocuments;

    @Before
    public void setUp() throws Exception {
        expectedDocuments = TestUtil.getAllDocuments();
        TestUtil.getAllDocuments().forEach(document -> collectorDAO.merge(document));

    }

    @Test
    @Transactional
    @Rollback
    public void testFindById() throws Exception {
        assertEquals(expectedDocuments.get(0), collectorDAO.findById(DOCUMENT_ID_1));
        assertEquals(expectedDocuments.get(1), collectorDAO.findById(DOCUMENT_ID_2));
        assertNull(collectorDAO.findById(UUID.randomUUID()));
    }

    @Test
    @Transactional
    @Rollback
    public void testDelete() throws Exception {
        assertEquals(expectedDocuments.get(0), collectorDAO.findById(DOCUMENT_ID_1));
        collectorDAO.delete(DOCUMENT_ID_2);
        assertEquals(expectedDocuments.get(0), collectorDAO.findById(DOCUMENT_ID_1));
        collectorDAO.delete(DOCUMENT_ID_1);
        assertNull(collectorDAO.findById(DOCUMENT_ID_1));

    }

}