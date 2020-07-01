package com.jorge.springit.repository;

import com.jorge.springit.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<Link,Long> {

}
