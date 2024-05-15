package org.se2.authentication.controller;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.se2.authentication.model.Comment;
import org.se2.authentication.model.Post;
import org.se2.authentication.repository.CommentRepository;
import org.se2.authentication.repository.PostRepository;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/user-page")
public class PostController {
  @Autowired
  private final PostRepository postRepository;

  @Autowired
    private UserDetailsService userDetailsService;
  private final CommentRepository commentRepository;

  private final int PAGINATIONSIZE = 999;

  public PostController(PostRepository postRepository, CommentRepository commentRepository) {
    this.postRepository = postRepository;
    this.commentRepository = commentRepository;
  }

  @GetMapping("user-page")
  public String getUserPage (Model model, Principal principal, @RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "size", defaultValue = "" + PAGINATIONSIZE) int size) {
    PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "postId"));
    Page<Post> postsPage = postRepository.findAll(pageRequest);
    List<Post> posts = postsPage.toList();

    long postCount = postRepository.count();
    int numOfPages = (int) Math.ceil((postCount * 1.0) / PAGINATIONSIZE);
    model.addAttribute("posts", posts);
    model.addAttribute("postCount", postCount);
    model.addAttribute("pageRequested", page);
    model.addAttribute("paginationSize", PAGINATIONSIZE);
    model.addAttribute("numOfPages", numOfPages);


    UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
    model.addAttribute("user", userDetails);
    return "posts";
  }


  @GetMapping("/posts/{id}")
  public String getPostById(@PathVariable long id, Model model, Principal principal) {
    Optional<Post> postOptional = postRepository.findById(id);
    UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
    model.addAttribute("user", userDetails);
    if (postOptional.isPresent()) {
      model.addAttribute("post", postOptional.get());

      List<Comment> commentsOptional = commentRepository.findCommentsByPostId(id);
      model.addAttribute("comments", commentsOptional);

    } else {
      model.addAttribute("error", "No post");
    }
    return "post";
  }

  @GetMapping("/user-page/posts/add")
  public String add(Model model) {
    model.addAttribute("newPost", new Post());
    return "addPost";
  }

  @PostMapping("/add")
  public String addPost(@RequestBody Post newPost) {
    postRepository.save(newPost);
    return "redirect:/posts";
  }

  @PutMapping("/{id}")
  public String editPost(@RequestBody Post newPost, @PathVariable Long id, Model model) {
    return postRepository.findById(id).map(post -> {
      model.addAttribute("editedPost", post);
      return "editPost";
    }).orElse("redirect:/");
  }

  @DeleteMapping("/{id}")
  public String deletePost(@PathVariable Long id) {
    postRepository.deleteById(id);
    return "redirect:/";
  }
}
