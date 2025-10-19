package com.dvwish.aitaskmanager.taskmanager.service;


import com.dvwish.aitaskmanager.taskmanager.model.User;
import com.dvwish.aitaskmanager.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CognitoService {

    private final CognitoIdentityProviderClient cognitoClient;
    private final UserRepository userRepository;

    @Value("${aws.cognito.userPoolId}")
    private String userPoolId;

    @Value("${aws.cognito.clientId}")
    private String clientId;

    public void registerUser(String email, String username, String password) {
        // Register user in Cognito
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .clientId(clientId)
                .username(email)
                .password(password)
                .userAttributes(
                        AttributeType.builder().name("email").value(email).build(),
                        AttributeType.builder().name("preferred_username").value(username).build()
                )
                .build();
        cognitoClient.signUp(signUpRequest);

        // Save user in database
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setCognitoId(getCognitoSub(email));
        user.setRoles(List.of("User")); // Default role
        userRepository.save(user);
    }

    public String authenticateUser(String email, String password) {
        Map<String, String> authParameters = new HashMap<>();
        authParameters.put("USERNAME", email);
        authParameters.put("PASSWORD", password);

        AdminInitiateAuthRequest authRequest = AdminInitiateAuthRequest.builder()
                .authFlow(AuthFlowType.ADMIN_USER_PASSWORD_AUTH)
                .userPoolId(userPoolId)
                .clientId(clientId)
                .authParameters(authParameters
                )
                .build();
        AdminInitiateAuthResponse response = cognitoClient.adminInitiateAuth(authRequest);
        return response.authenticationResult().idToken(); // JWT token
    }

    public void assignRole(String email, String role) {
        AdminAddUserToGroupRequest request = AdminAddUserToGroupRequest.builder()
                .userPoolId(userPoolId)
                .username(email)
                .groupName(role)
                .build();
        cognitoClient.adminAddUserToGroup(request);

        // Update roles in database
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<String> roles = user.getRoles();
        if (!roles.contains(role)) {
            roles.add(role);
            user.setRoles(roles);
            userRepository.save(user);
        }
    }

    private String getCognitoSub(String email) {
        AdminGetUserRequest request = AdminGetUserRequest.builder()
                .userPoolId(userPoolId)
                .username(email)
                .build();
        AdminGetUserResponse response = cognitoClient.adminGetUser(request);
        return response.userAttributes().stream()
                .filter(attr -> "sub".equals(attr.name()))
                .findFirst()
                .map(AttributeType::value)
                .orElseThrow(() -> new RuntimeException("Cognito sub not found"));
    }
}