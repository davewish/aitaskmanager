package com.dvwish.aitaskmanager.taskmanager.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserDTO {
    private Long id;
    private String name;
    private String username;
    private List<String> roles;
}
