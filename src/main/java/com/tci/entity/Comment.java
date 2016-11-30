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
}
