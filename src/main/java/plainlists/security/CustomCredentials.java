package com.semantyca.admin.security;

public class CustomCredentials {
  private String token;
  private Long userId;

 public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getUserId() {
    return userId;
  }
}
