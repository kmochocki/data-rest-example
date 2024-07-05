package com.kmochocki.datarestexample;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource
interface MainResourceRepository extends CrudRepository<MainResource, UUID> {
}
