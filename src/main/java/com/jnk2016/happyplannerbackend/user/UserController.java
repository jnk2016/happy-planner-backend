package com.jnk2016.happyplannerbackend.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

    private ApplicationUserRepository applicationUserRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(ApplicationUserRepository applicationUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /** Register a new account */
    @PostMapping("/sign-up")
    public void signUp(@RequestBody ApplicationUser user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        if(applicationUserRepository.findByUsername(user.getUsername()) == null){
        applicationUserRepository.save(user);}
    }

    /** Get the water intake for the user */
    @GetMapping("/water")
    public double waterIntake(Authentication auth) {
        ApplicationUser user = applicationUserRepository.findByUsername((auth.getName()));  // Obtains the current user
        return user.getDailyWater();
    }
}