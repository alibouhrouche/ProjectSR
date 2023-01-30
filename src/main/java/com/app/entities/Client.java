package com.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "client")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JacksonXmlRootElement
public class Client implements UserDetails {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code_postal", nullable = false)
    private Integer codePostal;

    @Column(name = "rue", nullable = false)
    private String rue;

    @Column(name = "ville", nullable = false, length = 45)
    private String ville;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "mot_de_passe", nullable = false, length = 100)
    private String password;

    @Column(name = "nom", nullable = false, length = 45)
    private String nom;

    @Column(name = "prenom", nullable = false, length = 45)
    private String prenom;

    @Enumerated(EnumType.STRING)
    private Role role;

    public Client(Integer codePostal, String rue, String ville, String email, String password, String nom, String prenom) {
        this.codePostal = codePostal;
        this.rue = rue;
        this.ville = ville;
        this.email = email;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
    }

    @Override
    @Schema(accessMode = AccessMode.READ_ONLY)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String mdp) {
        password = mdp;
    }

    @Override
    @Schema(accessMode = AccessMode.READ_ONLY)
    public String getUsername() {
        return email;
    }

    @Override
    @Schema(accessMode = AccessMode.READ_ONLY)
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @Schema(accessMode = AccessMode.READ_ONLY)
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @Schema(accessMode = AccessMode.READ_ONLY)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @Schema(accessMode = AccessMode.READ_ONLY)
    public boolean isEnabled() {
        return true;
    }

    @JsonProperty("isAdmin")
    @Schema(accessMode = AccessMode.READ_ONLY)
    public boolean isAdmin() {
        return role.equals(Role.ADMIN);
    }
}