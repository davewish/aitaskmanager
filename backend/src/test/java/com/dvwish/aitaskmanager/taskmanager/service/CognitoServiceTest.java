package com.dvwish.aitaskmanager.taskmanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dvwish.aitaskmanager.taskmanager.model.User;
import com.dvwish.aitaskmanager.taskmanager.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminAddUserToGroupRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminGetUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminGetUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthenticationResultType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpResponse;

@ExtendWith(MockitoExtension.class)
public class CognitoServiceTest {

  @Mock
  private CognitoIdentityProviderClient cognitoClient;
  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private CognitoService cognitoService;
  private final String userPoolId = "us-east-1_abcdef123";
  private  final String clientId = "1a2b3c4d5e6f7g8h9i0j";

  @BeforeEach()
  void setUp() {

    lenient().when(cognitoClient.signUp(any(SignUpRequest.class)))
        .thenReturn(SignUpResponse.builder().build());
    lenient().when(cognitoClient.adminInitiateAuth(any(AdminInitiateAuthRequest.class)))
        .thenReturn(AdminInitiateAuthResponse.builder()
            .authenticationResult(AuthenticationResultType.builder().idToken("jwt-token").build())
            .build());
    lenient().when(cognitoClient.adminGetUser(any(AdminGetUserRequest.class)))
        .thenReturn(AdminGetUserResponse.builder()
            .userAttributes(AttributeType.builder().name("sub").value("cognito-sub").build())
            .build());


  }

  @Test
  void testRegisterUser() {
    //arrange
    String email = "test@example.com";
    String username = "testuser";
    String password = "SecurePass123";
    User savedUser = new User();

    savedUser.setEmail(email);
    savedUser.setUsername(username);
    savedUser.setCognitoId("Cognito-sub");
    savedUser.getRoles().add("User");
    when(userRepository.save(any(User.class))).thenReturn(savedUser);
    // Act
    cognitoService.registerUser(email, username, password);

    // Assert
    verify(cognitoClient).signUp(any(SignUpRequest.class));
    verify(userRepository).save(any(User.class));

  }

  @Test
  void testAuthenticateUser() {
    // Arrange
    String email = "test@example.com";
    String password = "SecurePass123!";

    // Act
    String token = cognitoService.authenticateUser(email, password);

    // Assert
    assertEquals("jwt-token", token);
    verify(cognitoClient).adminInitiateAuth(any(AdminInitiateAuthRequest.class));
  }

  @Test
  void testAssignRole() {
    // Arrange
    String email = "test@example.com";
    String role = "Admin";
    User user = new User();
    user.setEmail(email);
    user.getRoles().add("User");
    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
    when(userRepository.save(any(User.class))).thenReturn(user);

    // Act
    cognitoService.assignRole(email, role);

    // Assert
    verify(cognitoClient).adminAddUserToGroup(any(AdminAddUserToGroupRequest.class));
    verify(userRepository).save(any(User.class));
    assertTrue(user.getRoles().contains(role));
  }


}
