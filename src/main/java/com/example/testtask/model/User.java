package com.example.testtask.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Table(name = "user")
@Entity
@NoArgsConstructor
@Setter
@Getter
public class User implements Serializable {

    private static final long serialVersionUID = 6041044664742026387L;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "created_on")
    private String createdOn;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @JsonIgnore
    @Column(name = "account_enable")
    private boolean enabled;

    @JsonIgnore
    @Column(name = "credentials_expired")
    private boolean credentialsNonExpired;

    @JsonIgnore
    @Column(name = "account_expired")
    private boolean accountNonExpired;

    @JsonIgnore
    @Column(name = "account_locked")
    private boolean locked;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return isEnabled() == user.isEnabled()
                && isCredentialsNonExpired() == user.isCredentialsNonExpired()
                && isAccountNonExpired() == user.isAccountNonExpired()
                && isLocked() == user.isLocked()
                && Objects.equals(getCreatedOn(), user.getCreatedOn())
                && Objects.equals(getUserId(), user.getUserId())
                && Objects.equals(getEmail(), user.getEmail())
                && Objects.equals(getPassword(), user.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getUserId(),
                getEmail(),
                getCreatedOn(),
                getPassword(),
                isEnabled(),
                isCredentialsNonExpired(),
                isAccountNonExpired(),
                isLocked()
        );
    }

}
