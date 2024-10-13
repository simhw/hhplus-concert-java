package com.hhplus.concert.infra.user;

import com.hhplus.concert.domain.user.User;
import com.hhplus.concert.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User findById(Long id) {
        return userJpaRepository.findById(id).orElse(null);
    }
}
