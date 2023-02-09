package com.laingard.FrelloManager.model;

import com.laingard.FrelloManager.enumeration.ERole;
import com.laingard.FrelloManager.enumeration.EState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "states")
public class State {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EState name;

    public State() {
    }

    public State(EState name) {
        this.name = name;
    }
}
