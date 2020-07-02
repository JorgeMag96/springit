package com.jorge.springit;

import com.jorge.springit.model.Comment;
import com.jorge.springit.model.Link;
import com.jorge.springit.repository.CommentRepository;
import com.jorge.springit.repository.LinkRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringitApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringitApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(LinkRepository linkRepository, CommentRepository commentRepository){
        return args -> {
            Link link = new Link("Getting started with Spring Boot 2", "https://www.testlinkpage.com/spring-boot-2");
            linkRepository.save(link);

            Comment comment = new Comment("This Spring Boot 2 is awesome!", link);
            commentRepository.save(comment);
            link.addComment(comment);

            System.out.println("We just inserted a link and a comment");
            System.out.println("=====================================");
        };
    }
}
