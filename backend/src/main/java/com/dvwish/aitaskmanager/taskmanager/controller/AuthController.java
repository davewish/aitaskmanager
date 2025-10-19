package com.dvwish.aitaskmanager.taskmanager.controller;

import com.dvwish.aitaskmanager.taskmanager.dto.AssignRoleRequestDTO;
import com.dvwish.aitaskmanager.taskmanager.dto.LoginRequestDTO;
import com.dvwish.aitaskmanager.taskmanager.service.CognitoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final CognitoService cognitoService;
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO request) {
        String token = cognitoService.authenticateUser(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/assign-role")
    public ResponseEntity<String> assignRole(@RequestBody AssignRoleRequestDTO request) {
        cognitoService.assignRole(request.getEmail(), request.getRole());
        return ResponseEntity.ok("Role assigned successfully");
    }



}
