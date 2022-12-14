package com.example.demo.security.userDetails;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> byUsername = userRepository.findByUsername(username);
        if (byUsername.isPresent()){
            UserDetails build = CustomUserDetails.build(byUsername.get());
            return build;
        }else{
            throw new UsernameNotFoundException("Not found user");
        }
    }
    public UserDetails loadById(Long id){
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()){
            return CustomUserDetails.build(byId.get());
        }else {
            throw new UsernameNotFoundException("Not found user");
        }
    }


}
