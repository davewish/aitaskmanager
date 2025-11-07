package com.dvwish.aitaskmanager.taskmanager.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {

  private Long id;
  private String name;
  private String username;
  private List<String> roles;
}
