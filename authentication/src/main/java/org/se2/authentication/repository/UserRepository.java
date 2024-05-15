package org.se2.authentication.repository;

import org.se2.authentication.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Query(value = "SELECT c FROM User c WHERE " +
            "c.fullname LIKE %?1% OR " +
            "c.phoneNo LIKE %?1% OR " +
            "c.email LIKE %?1%")
    Page<User> findByUserNameContainingIgnoreCase(String keyword, Pageable pageable);

    List<User> findAll();
    Optional<User> findById(Long id);
    Page<User> findByEmailContainingIgnoreCase(String keyword, Pageable paging);
}
