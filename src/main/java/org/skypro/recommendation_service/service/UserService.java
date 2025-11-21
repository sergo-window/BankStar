package org.skypro.recommendation_service.service;

import org.skypro.recommendation_service.repository.UserDataRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserDataRepository userDataRepository;

    public UserService(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    public UUID findUserIdByName(String username) {

        return userDataRepository.findUserIdByName(username);
    }

    public String getUserNameById(UUID userId) {

        return userDataRepository.getUserNameById(userId);
    }
}
