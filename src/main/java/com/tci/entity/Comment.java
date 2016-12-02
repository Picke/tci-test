package com.tci.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "comment")
public class Comment implements Serializable {

    private UUID commentId;
    private Integer userId;
    private String commentContent;

    public Comment() {
    }

    public Comment(UUID commentId, Integer userId, String commentContent) {
        this.commentId = commentId;
        this.userId = userId;
        this.commentContent = commentContent;
    }

    public Comment(Comment comment) {
        this.commentId = comment.getCommentId();
        this.userId = comment.getUserId();
        this.commentContent = comment.getCommentContent();
    }

    @Id
    @Column(name = "comment_id", unique = true, nullable = false)
    public UUID getCommentId() {
        return commentId;
    }

    public void setCommentId(UUID commentId) {
        this.commentId = commentId;
    }

    @Column(name = "user_id")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Column(name = "comment_content")
    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", userId=" + userId +
                ", commentContent='" + commentContent + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        if (commentId != null ? !commentId.equals(comment.commentId) : comment.commentId != null) return false;
        if (userId != null ? !userId.equals(comment.userId) : comment.userId != null) return false;
        return commentContent != null ? commentContent.equals(comment.commentContent) : comment.commentContent == null;
    }

    @Override
    public int hashCode() {
        int result = commentId != null ? commentId.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (commentContent != null ? commentContent.hashCode() : 0);
        return result;
    }
}
