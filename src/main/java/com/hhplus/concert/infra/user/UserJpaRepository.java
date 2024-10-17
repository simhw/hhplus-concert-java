package com.hhplus.concert.infra.user;

import com.hhplus.concert.domain.user.User;
import org.springframework.data.repository.CrudRepository;

public interface UserJpaRepository extends CrudRepository<User, Long> {
}
