package authservice.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import authservice.entities.UserInfo;
import authservice.model.UserInfoDto;
import authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

@Component
@AllArgsConstructor
@Data
public class UserDetailsImplementation implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserInfo user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(user);
    }

    public UserInfo checkIfUserAlreadyExists(UserInfoDto userInfoDto){
        return userRepository.findByUsername(userInfoDto.getUsername());
    }

    public Boolean signupUser(UserInfoDto userInfoDto){

        //a function to check if userEmail, password is valid

//        if (!isValidUserInfo(userInfoDto)) {
//            return false;
//        }


        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
        if(Objects.nonNull(checkIfUserAlreadyExists(userInfoDto))){
            return false;
        }
        String userId = UUID.randomUUID().toString();
        userRepository.save(
                new UserInfo(userId, userInfoDto.getUsername(), userInfoDto.getPassword(), new HashSet<>()));
        return true;
    }

    private boolean isValidUserInfo(UserInfoDto userInfoDto) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

        if (userInfoDto.getUsername() == null || userInfoDto.getPassword() == null) {
            return false;
        }

        if (!Pattern.matches(emailRegex, userInfoDto.getUsername())) {
            return false;
        }

        if (!Pattern.matches(passwordRegex, userInfoDto.getPassword())) {
            return false;
        }

        return true;
    }

}
