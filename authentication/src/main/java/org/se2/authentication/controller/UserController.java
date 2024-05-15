package org.se2.authentication.controller;

import org.se2.authentication.dto.UserDto;
import org.se2.authentication.model.User;
import org.se2.authentication.model.Post;
import org.se2.authentication.repository.PostRepository;
import org.se2.authentication.repository.UserRepository;
import jakarta.validation.Valid;
import org.se2.authentication.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.se2.authentication.service.UserService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    private final int PAGINATIONSIZE = 999;

    @Autowired
    private FollowService followService;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private org.se2.authentication.repository.PostRepository PostRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String getRegistrationPage(@ModelAttribute("user") UserDto userDto) {
        return "register";
    }

    @PostMapping("/registration")
    public String saveUser(@ModelAttribute("user") UserDto userDto, Model model) {
        userService.save(userDto);
        model.addAttribute("message", "User registered successfully");
        return "register";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
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

    @GetMapping("/admin-page")
    public String getAdminPage(Model model, Principal principal, @RequestParam(required = false) String keyword,
                               @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "14") int size) {
        try {
            Pageable paging = PageRequest.of(page - 1, size);
            Page<User> pageUser;

            if (keyword == null || keyword.isEmpty()) {
                pageUser = userRepository.findAll(paging);
            } else {
                pageUser = userRepository.findByEmailContainingIgnoreCase(keyword, paging);
                model.addAttribute("keyword", keyword);
            }

            List<User> users = pageUser.getContent();
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());

            model.addAttribute("user", userDetails);
            model.addAttribute("users", users);
            model.addAttribute("currentPage", pageUser.getNumber() + 1);
            model.addAttribute("totalItems", pageUser.getTotalElements());
            model.addAttribute("totalPages", pageUser.getTotalPages());
            model.addAttribute("pageSize", size);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "AdminUser";
    }

    @GetMapping("/admin-page/posts")
    public String getAll(Model model, Principal principal, @RequestParam(required = false) String keyword,
                         @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "8") int size) {
        try {
            List<Post> posts = new ArrayList<Post>();
            Pageable paging = PageRequest.of(page - 1, size);

            Page<Post> pagePost;
            if (keyword == null) {
                pagePost = PostRepository.findAll(paging);
            } else {
                pagePost = PostRepository.findByPostIdContainingIgnoreCase(keyword, paging);
                model.addAttribute("keyword", keyword);
            }

            posts = pagePost.getContent();

            model.addAttribute("posts", posts);
            model.addAttribute("currentPage", pagePost.getNumber() + 1);
            model.addAttribute("totalItems", pagePost.getTotalElements());
            model.addAttribute("totalPages", pagePost.getTotalPages());
            model.addAttribute("pageSize", size);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        return "AdminPost";
    }


    @GetMapping("/admin-page/delete/{id}")
    public String deletePost(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            PostRepository.deleteById(id);

            redirectAttributes.addFlashAttribute("message", "The Post with id=" + id + " has been deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/admin-page/posts";
    }

    @GetMapping("/user/profile/{id}")
    public String getUserProfile(@PathVariable Long id, Model model) {
        // Fetch the user with the given ID from the database
       User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            // Handle the case where no user with the given ID exists
            model.addAttribute("message", "User not found");
            return "error";
        }

        // Calculate the number of followers and followings
        int followingCount = followService.getFollowingCount(id);
        int followerCount = followService.getFollowerCount(id);

        // Add the user and counts to the model
        model.addAttribute("user", user);
        model.addAttribute("followingCount", followingCount);
        model.addAttribute("followerCount", followerCount);

        // Return the name of the view to be rendered
        return "userProfile";
    }

    @GetMapping("user/update/{id}")
    public String getUpdateUserPage(@PathVariable(value = "id") Long id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            model.addAttribute("user", user);
            return "profileEdit"; // Thymeleaf template name
        } else {
            return "UserNotFound"; // Thymeleaf template for handling user not found
        }
    }

    @PostMapping("/user/update")
        public String saveUpdate(@Valid User user, BindingResult bindingResult){
            if(bindingResult.hasErrors()){
                System.out.println(bindingResult.getAllErrors());
                return "profileEdit";
            } else {
                userRepository.save(user);
                return "redirect:/user/profile/" + user.getUserId();
            }
        }

}
