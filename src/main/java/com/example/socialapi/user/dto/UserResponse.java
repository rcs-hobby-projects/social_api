package com.example.socialapi.user.dto;

public record UserResponse(Long id, String name, String email) {
  public static UserResponse from(com.example.socialapi.user.User user) {
    return new UserResponse(user.getId(), user.getName(), user.getEmail());
  }
}
