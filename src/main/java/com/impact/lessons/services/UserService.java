package com.impact.lessons.services;
import com.impact.lessons.config.JwtService;
import com.impact.lessons.dto.*;
import com.impact.lessons.entity.*;
import com.impact.lessons.exception.ApiException;
import com.impact.lessons.exception.ErrorCode;
import com.impact.lessons.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class UserService {
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserEmailRepository userEmailRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final CredentialsRepository credentialsRepository;
    private final UserPersonalDataRepository personalDataRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public UserService(UserRepository userRepository,
                       CredentialsRepository credentialsRepository,
                       UserPersonalDataRepository personalDataRepository,
                       UserEmailRepository userEmailRepository,PasswordEncoder passwordEncoder,
                       JwtService jwtService, RefreshTokenRepository refreshTokenRepository,
                       RoleRepository roleRepository,
                       UserRoleRepository userRoleRepository

    ) {
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.credentialsRepository = credentialsRepository;
        this.personalDataRepository = personalDataRepository;
        this.userEmailRepository = userEmailRepository;this.refreshTokenRepository = refreshTokenRepository;



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
    public LoginResponse login(LoginRequest request) {

        UserCredentials credentials = credentialsRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new ApiException(
                        ErrorCode.USER_NOT_FOUND.getMessage(),
                        ErrorCode.USER_NOT_FOUND.getCode()
                ));

        // 2. Verificăm parola
        boolean matches = passwordEncoder.matches(
                request.getPassword(),
                credentials.getPasswordHash()
        );

        if (!matches) {
            throw new RuntimeException("Invalid credentials");
        }

        // 3. Luăm user
        User user = credentials.getUser();

        // 4. Luăm personal data
        UserPersonalData personalData = personalDataRepository
                .findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Personal data not found"));

        // 5. Luăm role
        UserRole userRole = userRoleRepository
                .findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Role data not found"));

        Role role = roleRepository
                .findById(userRole.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // 6. Generăm token CU DATE COMPLETE
        String accessToken = jwtService.generateAccessToken(
                credentials.getUsername(),
                role.getName(),
                personalData
        );

        // refresh token rămâne la fel
        String refreshToken = jwtService.generateRefreshToken(
                credentials.getUsername()
        );

        RefreshToken RTK = new RefreshToken();
        RTK.setUser(user);
        RTK.setToken(refreshToken);
        RTK.setExpiresAt(LocalDateTime.now().plusDays(7));
        refreshTokenRepository.save(RTK);

        return new LoginResponse(accessToken, refreshToken);
    }
    public LoginResponse refresh(RefreshRequest request) {

        boolean accessValid = jwtService.isTokenValid(request.getAccessToken());
        if (accessValid) {
            throw new RuntimeException("Access token not expired yet");
        }

        RefreshToken storedToken = refreshTokenRepository
                .findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
        if (storedToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(storedToken);
            throw new RuntimeException("Refresh token expired");
        }

        String username = jwtService.extractUsername(request.getRefreshToken());
        refreshTokenRepository.delete(storedToken);

        UserCredentials credentials = credentialsRepository
                .findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User user = credentials.getUser();

        UserPersonalData personalData = personalDataRepository
                .findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Personal data not found"));

        UserRole userRole = userRoleRepository
                .findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Role data not found"));

        Role role = roleRepository
                .findById(userRole.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        String newAccessToken = jwtService.generateAccessToken(
                username,
                role.getName(),
                personalData
        );

        String newRefreshToken = jwtService.generateRefreshToken(username);
        RefreshToken newToken = new RefreshToken();
        newToken.setUser(storedToken.getUser());
        newToken.setToken(newRefreshToken);
        newToken.setExpiresAt(LocalDateTime.now().plusDays(7));
        refreshTokenRepository.save(newToken);

        return new LoginResponse(newAccessToken, newRefreshToken);
    }




}
