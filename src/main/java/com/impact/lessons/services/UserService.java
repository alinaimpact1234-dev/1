package com.impact.lessons.services;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.impact.lessons.dto.UpdateUserPersonalData;
import com.impact.lessons.repository.UserEmailRepository;
import com.impact.lessons.dto.CreateUserRequest;
import com.impact.lessons.dto.SetEmailRequest;
import com.impact.lessons.dto.UserResponse;
import com.impact.lessons.entity.User;
import com.impact.lessons.entity.UserCredentials;
import com.impact.lessons.entity.UserEmail;
import com.impact.lessons.entity.UserPersonalData;
import com.impact.lessons.repository.CredentialsRepository;
import com.impact.lessons.repository.UserPersonalDataRepository;
import com.impact.lessons.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;


@Service
public class UserService {
    private final UserEmailRepository userEmailRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final CredentialsRepository credentialsRepository;
    private final UserPersonalDataRepository personalDataRepository;


    public UserService(UserRepository userRepository,
                       CredentialsRepository credentialsRepository,
                       UserPersonalDataRepository personalDataRepository,
                       UserEmailRepository userEmailRepository,PasswordEncoder passwordEncoder
    ) {

        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.credentialsRepository = credentialsRepository;
        this.personalDataRepository = personalDataRepository;
        this.userEmailRepository = userEmailRepository;
    }
    @Transactional
    public void setPersonalData( Long id, UpdateUserPersonalData data){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));


        UserPersonalData personalData = personalDataRepository.findById(user.getId())
                .orElse(new UserPersonalData());


        personalData.setUser(user);
        personalData.setFirstName(data.getFirstName());
        personalData.setLastName(data.getLastName());
        personalData.setBirthDate(data.getBirthDate());


        personalDataRepository.save(personalData);
    }

    @Transactional
    public void setBirthDate(Long id, LocalDate birthDate){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserPersonalData personalData = personalDataRepository.findById(user.getId())
                .orElse(new UserPersonalData());

        personalData.setUser(user);
        personalData.setBirthDate(birthDate);

        personalDataRepository.save(personalData);
    }


    @Transactional
    public void createUser(CreateUserRequest request) {
        User user = new User();
        userRepository.save(user);


        UserCredentials credentials = new UserCredentials();
        credentials.setUser(user);
        credentials.setUsername(request.getUsername());
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        credentials.setPasswordHash(hashedPassword);

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
                response.setBirthDate(personal.getBirthDate());

            }


            if (credentials != null) {
                response.setUsername(credentials.getUsername());
            }


            return response;
        }).toList();
    }
    public void setEmailUser(SetEmailRequest emailRequest){
        User user = userRepository.findById(emailRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));


        UserEmail email = new UserEmail();
        email.setUser(user);
        email.setEmail(emailRequest.getEmail());
        email.setIsPrimary(emailRequest.getIsPrimary() != null ? emailRequest.getIsPrimary() : false);


        userEmailRepository.save(email);
    }

    public UserResponse getUserById(Long id) {


        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));


        UserPersonalData personal = personalDataRepository.findById(user.getId()).orElse(null);


        UserCredentials credentials = credentialsRepository.findById(user.getId()).orElse(null);


        UserResponse response = new UserResponse();


        response.setId(user.getId());


        if (personal != null) {
            response.setFirstName(personal.getFirstName());
            response.setLastName(personal.getLastName());
            response.setBirthDate(personal.getBirthDate());
        }
        if (credentials != null) {
            response.setUsername(credentials.getUsername());
        }
        return response;
    }



}
