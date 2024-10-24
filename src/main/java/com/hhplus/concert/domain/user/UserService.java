package com.hhplus.concert.domain.user;

import com.hhplus.concert.domain.support.error.CoreException;
import com.hhplus.concert.domain.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUser(Long id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new CoreException(ErrorType.USER_NOT_FOUND, id);
        }
        return user;
    }
}
