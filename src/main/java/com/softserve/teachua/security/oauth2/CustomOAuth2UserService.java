package com.softserve.teachua.security.oauth2;

import com.softserve.teachua.exception.oauth2.OAuth2AuthenticationProcessingException;
import com.softserve.teachua.model.AuthProvider;
import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.UserRepository;
import com.softserve.teachua.security.UserPrincipal;
import com.softserve.teachua.security.oauth2.user.OAuth2UserInfo;
import com.softserve.teachua.security.oauth2.user.OAuth2UserInfoFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final String NO_EMAIL = "Email not found from OAuth2 provider";
    private static final String USE_PASSWORD = "You are already sign up. Use your password to log in";
    private static final String USE_ANOTHER_PROVIDER = "You are already sign up. Use your %s to log in";
    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * The method returns object OAuth2User as result of successful authentication.
     *
     * @return OAut2User
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * The method checks if user already has account, if not create new.
     *
     * @return OAut2User
     */
    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException(NO_EMAIL);
        }

        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            Optional<AuthProvider> userProvider = Optional.ofNullable(user.getProvider());
            if (!userProvider.isPresent()) {
                throw new OAuth2AuthenticationProcessingException(USE_PASSWORD);
            }
            if (!userProvider.get().equals(AuthProvider.valueOf(
                oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException(
                    String.format(USE_ANOTHER_PROVIDER, user.getProvider()));
            }
            user = updateExistingUser(user, oAuth2UserInfo);
            log.debug("Successfully log in user - " + user);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }
        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    /**
     * The method create new user.
     *
     * @return User
     */
    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = new User();
        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setFirstName(oAuth2UserInfo.getFirstName());
        user.setLastName(oAuth2UserInfo.getLastName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setUrlLogo(oAuth2UserInfo.getImageUrl());
        user.setStatus(true);
        log.info("Successfully registered new user - " + user);
        return userRepository.save(user);
    }

    /**
     * The method update user.
     *
     * @return User
     */
    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setFirstName(oAuth2UserInfo.getFirstName());
        existingUser.setLastName(oAuth2UserInfo.getLastName());
        existingUser.setUrlLogo(oAuth2UserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }
}
