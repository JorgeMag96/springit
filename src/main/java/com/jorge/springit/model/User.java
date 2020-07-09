package com.jorge.springit.model;

import com.jorge.springit.security.validator.PasswordsMatch;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Data
@RequiredArgsConstructor
@Getter @Setter
@ToString
@NoArgsConstructor
@PasswordsMatch
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    @NotEmpty(message = "You must enter a First Name.")
    private String firstName;

    @NonNull
    @NotEmpty(message = "You must enter a Last Name.")
    private String lastName;

    @NonNull
    @NotEmpty(message = "Please enter alias.")
    @Column(nullable = false, unique = true)
    private String alias;

    @NonNull
    @Size(min = 8, max =  20)
    @Column(nullable = false, unique = true)
    private String email;

    @NonNull
    @Column(length = 100)
    private String password;

    @Transient
    @NotEmpty(message = "Please enter Password Confirmation.")
    private String confirmPassword;

    @NonNull
    @Column(nullable = false)
    private boolean enabled;

    private String activationCode;

    @Transient
    @Setter(AccessLevel.NONE)
    private String fullName;

    public String getFullName(){
        return firstName + " " + lastName;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id")
    )
    private Set<Role> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(
                role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return alias;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public void addRole(Role userRole) {
        roles.add(userRole);
    }

    public void addRoles(HashSet<Role> roles){
        roles.forEach(this::addRole);
    }
}
