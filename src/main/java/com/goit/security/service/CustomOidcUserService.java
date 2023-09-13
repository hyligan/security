package com.goit.security.service;

import com.goit.security.dto.GoogleUserInfo;
import com.goit.security.entities.User;
import com.goit.security.repo.UserRepo;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOidcUserService extends OidcUserService {
  
  private final UserRepo userRepository;

  public CustomOidcUserService(UserRepo userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
    OidcUser oidcUser = super.loadUser(userRequest);
    try {
      return processOidcUser(oidcUser);
    } catch (Exception ex) {
      throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
    }
  }

  private OidcUser processOidcUser(OidcUser oidcUser) {
    GoogleUserInfo googleUserInfo = new GoogleUserInfo(oidcUser.getAttributes());
    Optional<User> userOptional = userRepository.findByGoogleId(googleUserInfo.getId());
    if (!userOptional.isPresent()) {
      User user = new User();
      user.setEmail(googleUserInfo.getEmail());
      user.setName(googleUserInfo.getName());
      user.setRole("ROLE_USER");
      user.setEnabled(true);
      user.setGoogleId(googleUserInfo.getId());
      userRepository.save(user);
    }

    if(userOptional.isPresent())
      if(userOptional.get().getEnabled())
        return oidcUser;
    throw new RuntimeException("Unknown user");
  }
}
