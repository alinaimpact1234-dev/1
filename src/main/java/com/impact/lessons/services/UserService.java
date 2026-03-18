package com.impact.lessons.services;

import com.impact.lessons.dto.CreateUserRequest;
import com.impact.lessons.dto.UserResponse;
import com.impact.lessons.entity.User;
import com.impact.lessons.entity.UserCredentials;
import com.impact.lessons.entity.UserPersonalData;
import com.impact.lessons.repository.CredentialsRepository;
import com.impact.lessons.repository.UserPersonalDataRepository;
import com.impact.lessons.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CredentialsRepository credentialsRepository;
    private final UserPersonalDataRepository personalDataRepository;

    public UserService(UserRepository userRepository,
                       CredentialsRepository credentialsRepository,
                       UserPersonalDataRepository personalDataRepository) {
        this.userRepository = userRepository;
        this.credentialsRepository = credentialsRepository;
        this.personalDataRepository = personalDataRepository;
    }

    @Transactional
    public void createUser(CreateUserRequest request) {
        User user = new User();
        userRepository.save(user);

        UserCredentials credentials = new UserCredentials();
        credentials.setUser(user);
        credentials.setUsername(request.getUsername());
        credentials.setPasswordHash(request.getPassword());
        credentialsRepository.save(credentials);

        UserPersonalData personalData = new UserPersonalData();
        personalData.setUser(user);
        personalData.setFirstName(request.getFirstName());
        personalData.setLastName(request.getLastName());
        personalData.setBirthDate(request.getBirthDate());
        personalDataRepository.save(personalData);
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream().map(user -> {
            UserPersonalData personal = personalDataRepository.findById(user.getId()).orElse(null);
            UserCredentials credentials = credentialsRepository.findById(user.getId()).orElse(null);

            UserResponse response = new UserResponse();
            response.setId(user.getId());

            if (personal != null) {
                response.setFirstName(personal.getFirstName());
                response.setLastName(personal.getLastName());
            }

            if (credentials != null) {
                response.setUsername(credentials.getUsername());
            }

            return response;
        }).toList();
    }
}