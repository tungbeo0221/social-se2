package org.se2.authentication.model;

import jakarta.persistence.*;

@Entity
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reactionId", nullable = false)
    private int reactionId;
    private int userId;
    private int postId;
    private String type;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getReactionId() {
        return reactionId;
    }

    public void setReactionId(int reactionId) {
        this.reactionId = reactionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}

