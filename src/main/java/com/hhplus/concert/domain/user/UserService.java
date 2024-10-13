package com.hhplus.concert.domain.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User user(Long id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new NoUserException();
        }
        return user;
    }
}
