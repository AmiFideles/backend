package com.example.demo.rest;

import com.example.demo.dto.UserDto;
import com.example.demo.repository.HitRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.userDetails.CustomUserDetails;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.config.SwaggerConfiguration.BEARER_KEY_SECURITY_SCHEME;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;
    private final HitRepository hitRepository;
    private final UserService userService;

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @GetMapping("/info/{username}")
    @PreAuthorize("#user.username == #username")
    public UserDto getUser(@AuthenticationPrincipal CustomUserDetails user, @PathVariable String username){
        return userService.findByUsername(username);
    }



    /*
    * TODO
    *  get user info
    *  get all user
    *
    *
    *
    * */

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @GetMapping("/me")
    public String me(Authentication authentication) {
        System.out.println();
        return "test";
    }


    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @GetMapping("/test")
    public String test(){
        return "admin";
    }
}
