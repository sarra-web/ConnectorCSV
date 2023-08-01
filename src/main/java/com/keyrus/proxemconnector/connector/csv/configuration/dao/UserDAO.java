package com.keyrus.proxemconnector.connector.csv.configuration.dao;

import com.keyrus.proxemconnector.connector.csv.configuration.model.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class UserDAO implements Serializable {

        @Id
        @Column
       // @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

     //   @NotBlank
     //   @Size(max = 20)
     @Column
        private String username;

     //   @NotBlank
    //    @Size(max = 50)
    //    @Email
     @Column
        private String email;

  //      @NotBlank
  //      @Size(max = 120)
  @Column
    private String password;

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "user_roles",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id"))
        private Collection<RoleDAO> roles ;



    public UserDAO(Long id, String username, String email, String password, Collection<RoleDAO> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }
    public UserDAO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.roles = user.getRoles().stream().map(role -> new RoleDAO(role.getId(),role.getName())).collect(Collectors.toSet());
    }

    public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Collection<RoleDAO> getRoles() {
            return roles;
        }

        public void setRoles(List<RoleDAO> roles) {
            this.roles = roles;
        }
    }

