package com.tci.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


@Entity
@Table(name = "document")
public class Document implements Serializable {

    private UUID documentId;
    private String documentName;
    private String documentTitle;
    private Map<String, String> indexes;
    private String documentContent;
    private List<Comment> comments;

    public Document() {
    }

    public Document(UUID documentId, String documentName, String documentTitle, Map<String, String> indexes, String documentContent, List<Comment> comments) {
        this.documentId = documentId;
        this.documentName = documentName;
        this.documentTitle = documentTitle;
        this.indexes = indexes;
        this.documentContent = documentContent;
        this.comments = comments;
    }

    public Document(Document document) {
        this.documentId = document.getDocumentId();
        this.documentName = document.getDocumentName();
        this.documentTitle = document.getDocumentTitle();
        this.indexes = document.getIndexes().entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue));
        this.documentContent = document.getDocumentContent();
        if (document.getComments() != null && !document.getComments().isEmpty()) {
            this.comments = new ArrayList<>();
            for (Comment comment : document.getComments()) {
                this.comments.add(new Comment(comment));
            }
        }
    }

    @Id
    @Column(name = "document_id", unique = true, nullable = false)
    public UUID getDocumentId() {
        return documentId;
    }

    public void setDocumentId(UUID documentId) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Document document = (Document) o;

        if (documentId != null ? !documentId.equals(document.documentId) : document.documentId != null) return false;
        if (documentName != null ? !documentName.equals(document.documentName) : document.documentName != null)
            return false;
        if (documentTitle != null ? !documentTitle.equals(document.documentTitle) : document.documentTitle != null)
            return false;
        if (indexes != null ? !indexes.equals(document.indexes) : document.indexes != null) return false;
        if (documentContent != null ? !documentContent.equals(document.documentContent) : document.documentContent != null)
            return false;
        return comments != null ? comments.equals(document.comments) : document.comments == null;
    }

    @Override
    public int hashCode() {
        int result = documentId != null ? documentId.hashCode() : 0;
        result = 31 * result + (documentName != null ? documentName.hashCode() : 0);
        result = 31 * result + (documentTitle != null ? documentTitle.hashCode() : 0);
        result = 31 * result + (indexes != null ? indexes.hashCode() : 0);
        result = 31 * result + (documentContent != null ? documentContent.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        return result;
    }
}
