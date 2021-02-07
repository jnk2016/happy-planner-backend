package com.jnk2016.happyplannerbackend.mood;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import com.jnk2016.happyplannerbackend.user.ApplicationUserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/moods")
public class MoodController {
    private ApplicationUserRepository applicationUserRepository;
    private MoodRepository moodRepository;

    public MoodController(ApplicationUserRepository applicationUserRepository,
                          MoodRepository moodRepository){
        this.applicationUserRepository = applicationUserRepository;
        this.moodRepository = moodRepository;
    }

    @PostMapping("/new")
    public void newMood(Authentication auth, @RequestBody Mood mood){
        ApplicationUser user = applicationUserRepository.findByUsername((auth.getName()));
        mood.setUser(user);
        moodRepository.save(mood);
    }

    @GetMapping("/user")
    public List<MoodResponse> getMoods(Authentication auth){
        ApplicationUser user = applicationUserRepository.findByUsername(auth.getName());
        List<Mood>userMoodData = moodRepository.findByUser(user);
        List<MoodResponse> moodResponse = new ArrayList<>();
        for(Mood userMood : userMoodData){
            MoodResponse mood = new MoodResponse(userMood.getMoodId(), userMood.getLabel(), userMood.getNote(), userMood.getTimestamp());
            moodResponse.add(mood);
        }
        return moodResponse;
    }
}
