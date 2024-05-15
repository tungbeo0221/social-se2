package org.se2.authentication.model;

    import jakarta.persistence.*;

    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Set;

    @Entity
    @Table(name = "posts")
    public class Post {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer postId;

        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;

        @Column(length = 1000)
        private String status;

        @Column(length = 150)
        private String imageUrl;

        @Column
        private LocalDateTime createAt;

        @Column(columnDefinition = "TEXT")
        private String content;

        @Column
        private String title;


        @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
        private List<Comment> comments = new ArrayList<>();

        public Post() {
        }


        public Post(Integer postId, User user, String status, String imageUrl) {
            this.postId = postId;
            this.user = user;
            this.status = status;
            this.imageUrl = imageUrl;
        }


    // getters

        public Integer getPostId() {
            return postId;
        }

        public void setPostId(Integer postId) {
            this.postId = postId;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public LocalDateTime getCreateAt() {
            return createAt;
        }

        public void setCreateAt(LocalDateTime createAt) {
            this.createAt = createAt;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<Comment> getComments() {
            return comments;
        }

        public void setComments(List<Comment> comments) {
            this.comments = comments;
        }
    }
