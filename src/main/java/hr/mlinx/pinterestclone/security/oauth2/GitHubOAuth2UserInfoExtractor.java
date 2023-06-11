package hr.mlinx.pinterestclone.security.oauth2;

import hr.mlinx.pinterestclone.model.AuthProvider;
import hr.mlinx.pinterestclone.model.Role;
import hr.mlinx.pinterestclone.model.RoleName;
import hr.mlinx.pinterestclone.security.CustomUserDetails;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GitHubOAuth2UserInfoExtractor implements OAuth2UserInfoExtractor {

    @Override
    public CustomUserDetails extractUserInfo(OAuth2User oAuth2User) {
        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setUsername(retrieveAttr("login", oAuth2User));
        customUserDetails.setEmail(retrieveAttr("email", oAuth2User));
        customUserDetails.setImageUrl(retrieveAttr("avatar_url", oAuth2User));
        customUserDetails.setAuthProvider(AuthProvider.GITHUB);
        customUserDetails.setAttributes(oAuth2User.getAttributes());
        Role role = new Role();
        role.setName(RoleName.ROLE_USER);
        customUserDetails.setRoles(Set.of(role));
        return customUserDetails;
    }

    @Override
    public boolean accepts(OAuth2UserRequest userRequest) {
        return AuthProvider.GITHUB.name().equalsIgnoreCase(userRequest.getClientRegistration().getRegistrationId());
    }

}
