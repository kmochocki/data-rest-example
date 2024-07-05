package com.kmochocki.datarestexample;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
class ChildResource {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false)
    @EqualsAndHashCode.Include
    private UUID id;

    private String name;

    public ChildResource(String name) {
        this.name = name;
    }
}
