package com.jorge.springit.repository;

import com.jorge.springit.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LinkRepository extends JpaRepository<Link,Long> {

    Link findByTitle(String title);
    List<Link> findByTitleLikeOrderByCreationDateDesc(String title);

}
