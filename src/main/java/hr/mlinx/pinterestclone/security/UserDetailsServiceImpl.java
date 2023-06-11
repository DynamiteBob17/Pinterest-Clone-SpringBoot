package hr.mlinx.pinterestclone.security;

import hr.mlinx.pinterestclone.exception.ResourceNotFoundException;
import hr.mlinx.pinterestclone.model.User;
import hr.mlinx.pinterestclone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("user", "username", username));

        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setId(user.getId());
        customUserDetails.setUsername(user.getUsername());
        customUserDetails.setEmail(user.getEmail());
        customUserDetails.setPassword(user.getPassword());
        customUserDetails.setRoles(userService.getRolesOfUser(user));
        return customUserDetails;
    }

}
