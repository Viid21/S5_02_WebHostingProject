package com.TuWebYa.horno.user.infra.repository;

import com.TuWebYa.horno.user.infra.entity.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserR2dbcRepository extends ReactiveCrudRepository<UserEntity, UUID> {
}
