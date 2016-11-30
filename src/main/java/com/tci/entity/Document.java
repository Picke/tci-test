package com.tci.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;


@Entity
@Table(name = "document")
public class Document implements Serializable {

    private Integer documentId;
    private String documentName;
    private String documentTitle;
    private Map<String, String> indexes;
    private String documentContent;
    private List<Comment> comments;

    @Id
    @Column(name = "document_id", unique = true, nullable = false)
    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }

    @Column(name = "document_name", nullable = false)
    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    @Column(name = "document_title", nullable = false)
    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    @ElementCollection
    @MapKeyColumn(name="index_name")
    @Column(name="index_value")
    @CollectionTable(name="document_indexes", joinColumns=@JoinColumn(name="document_id"))
    public Map<String, String> getIndexes() {
        return indexes;
    }

    public void setIndexes(Map<String, String> indexes) {
        this.indexes = indexes;
    }

    @Column(name = "document_content", nullable = false)
    public String getDocumentContent() {
        return documentContent;
    }

    public void setDocumentContent(String documentContent) {
        this.documentContent = documentContent;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
    @JoinTable(name = "document_comment_mapping", joinColumns = @JoinColumn(name = "document_id"), inverseJoinColumns = @JoinColumn(name = "comment_id"))
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Document{" +
                "documentId=" + documentId +
                ", documentName='" + documentName + '\'' +
                ", documentTitle='" + documentTitle + '\'' +
                ", indexes=" + indexes +
                ", documentContent='" + documentContent + '\'' +
                ", comments=" + comments +
                '}';
    }
}
