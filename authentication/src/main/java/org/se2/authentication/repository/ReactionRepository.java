package org.se2.authentication.repository;

import org.se2.authentication.model.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionRepository extends JpaRepository<Reaction, Integer> {

}
