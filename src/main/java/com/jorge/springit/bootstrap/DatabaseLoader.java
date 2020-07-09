package com.jorge.springit.bootstrap;

import com.jorge.springit.model.Comment;
import com.jorge.springit.model.Link;
import com.jorge.springit.model.Role;
import com.jorge.springit.model.User;
import com.jorge.springit.repository.CommentRepository;
import com.jorge.springit.repository.LinkRepository;
import com.jorge.springit.repository.RoleRepository;
import com.jorge.springit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DatabaseLoader implements CommandLineRunner {

    private final LinkRepository linkRepository;
    private final CommentRepository commentRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private HashMap<String, User> users = new HashMap<>();

    @Override
    public void run(String... args) {

        //add users and roles
        addUsersAndRoles();

        Map<String,String> links = new HashMap<>();
        links.put("Securing Spring Boot APIs and SPAs with OAuth 2.0","https://auth0.com/blog/securing-spring-boot-apis-and-spas-with-oauth2/?utm_source=reddit&utm_medium=sc&utm_campaign=springboot_spa_securing");
        links.put("Easy way to detect Device in Java Web Application using Spring Mobile - Source code to download from GitHub","https://www.opencodez.com/java/device-detection-using-spring-mobile.htm");
        links.put("Tutorial series about building microservices with SpringBoot (with Netflix OSS)","https://medium.com/@marcus.eisele/implementing-a-microservice-architecture-with-spring-boot-intro-cdb6ad16806c");
        links.put("Detailed steps to send encrypted email using Java / Spring Boot - Source code to download from GitHub","https://www.opencodez.com/java/send-encrypted-email-using-java.htm");
        links.put("Build a Secure Progressive Web App With Spring Boot and React","https://dzone.com/articles/build-a-secure-progressive-web-app-with-spring-boo");
        links.put("Building Your First Spring Boot Web Application - DZone Java","https://dzone.com/articles/building-your-first-spring-boot-web-application-ex");
        links.put("Building Microservices with Spring Boot Fat (Uber) Jar","https://jelastic.com/blog/building-microservices-with-spring-boot-fat-uber-jar/");
        links.put("Spring Cloud GCP 1.0 Released","https://cloud.google.com/blog/products/gcp/calling-java-developers-spring-cloud-gcp-1-0-is-now-generally-available");
        links.put("Simplest way to Upload and Download Files in Java with Spring Boot - Code to download from Github","https://www.opencodez.com/uncategorized/file-upload-and-download-in-java-spring-boot.htm");
        links.put("Add Social Login to Your Spring Boot 2.0 app","https://developer.okta.com/blog/2018/07/24/social-spring-boot");
        links.put("File download example using Spring REST Controller","https://www.jeejava.com/file-download-example-using-spring-rest-controller/");

        links.forEach((k,v) -> {

            Link link = new Link(k,v);

            // add the user to the link
            if(k.startsWith("Build")){
                link.setUser(users.get("admin"));
            } else {
                link.setUser(users.get("user"));
            }

            linkRepository.save(link);

            // we will do something with comments later
            Comment spring = new Comment("Thank you for this link related to Spring Boot. I love it, great post!",link);
            Comment security = new Comment("I love that you're talking about Spring Security",link);
            Comment pwa = new Comment("What is this Progressive Web App thing all about? PWAs sound really cool.",link);
            Comment[] comments = {spring,security,pwa};
            for(Comment comment : comments) {
                commentRepository.save(comment);
                link.addComment(comment);
            }
        });

        long linkCount = linkRepository.count();
        System.out.println("Number of links in the database: " + linkCount );
    }

    private void addUsersAndRoles() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String secret = "{bcrypt}" + encoder.encode("password");

        Role userRole = new Role("ROLE_USER");
        roleRepository.save(userRole);
        Role adminRole = new Role("ROLE_ADMIN");
        roleRepository.save(adminRole);

        User user = new User("Jorge_user", "Villarreal_user","user","user@gmail.com",secret,true);
        user.addRole(userRole);
        user.setConfirmPassword(secret);
        userRepository.save(user);
        users.put("user", user);

        User admin = new User("Jorge_admin", "Villarreal_admin","admin", "admin@gmail.com",secret,true);
        admin.addRole(adminRole);
        admin.setConfirmPassword(secret);
        userRepository.save(admin);
        users.put("admin", admin);

        User master = new User("Jorge_master", "Villarreal_master","master","super@gmail.com",secret,true);
        master.addRoles(new HashSet<>(Arrays.asList(userRole,adminRole)));
        master.setConfirmPassword(secret);
        userRepository.save(master);
        users.put("master", master);

    }
}
