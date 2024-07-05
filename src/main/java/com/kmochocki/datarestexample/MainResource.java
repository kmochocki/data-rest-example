package com.kmochocki.datarestexample;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;
import java.util.UUID;

@Entity
@Getter
@Setter
class MainResource {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false)
    @EqualsAndHashCode.Include
    private UUID id;

    private String description;

    @OneToMany
    @JoinColumn(name = "main_resource_id")
    @ToString.Exclude
    private Collection<ChildResource> childResources;
}
