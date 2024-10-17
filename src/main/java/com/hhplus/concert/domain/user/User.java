package com.hhplus.concert.domain.user;

import com.hhplus.concert.domain.reservation.Reservation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.*;

@Getter
@Entity
@Table(name = "users")
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private String name;

    private String email;

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations = new ArrayList<>();

    protected User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
