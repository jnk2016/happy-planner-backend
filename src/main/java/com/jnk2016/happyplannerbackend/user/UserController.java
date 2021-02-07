package com.jnk2016.happyplannerbackend.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/users")
public class UserController {

    private ApplicationUserRepository applicationUserRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(ApplicationUserRepository applicationUserRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public ApplicationUser getCurrentUser(@AuthenticationPrincipal ApplicationUser user){
        return user;
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody ApplicationUser user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        if(applicationUserRepository.findByUsername(user.getUsername()) == null){
        applicationUserRepository.save(user);}
    }

//    @GetMapping("/{id}")
//    public int waterIntake(@PathVariable long id) throws Exception {
//        ApplicationUser user = applicationUserRepository.findById(id).orElseThrow(() -> new Exception("User not found"));
//        return user.getDailyWater();
//    }

    @GetMapping("/water")
    public int waterIntake(Authentication auth) {
        ApplicationUser user = applicationUserRepository.findByUsername((auth.getName()));  /** Obtains the current user */
        return user.getDailyWater();
    }
}