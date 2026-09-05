package com.govtech.bff.security.auth.mapper;

import com.govtech.bff.security.auth.dto.CreateUserRequest;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public CreateUserRequest toCreateUserRequest(OidcUser oidcUser) {

    return new CreateUserRequest(
        oidcUser.getSubject(),
        oidcUser.getGivenName(),
        oidcUser.getFamilyName(),
        oidcUser.getEmail());
  }
}
