package com.jnk2016.happyplannerbackend.mood;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import com.jnk2016.happyplannerbackend.user.ApplicationUserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/moods")
public class MoodController {
    private ApplicationUserRepository applicationUserRepository;
    private MoodRepository moodRepository;

    public MoodController(ApplicationUserRepository applicationUserRepository, MoodRepository moodRepository){
        this.applicationUserRepository = applicationUserRepository;
        this.moodRepository = moodRepository;
    }

    /** Create a new mood for the logged in user */
    @PostMapping
    public void newMood(Authentication auth, @RequestBody Mood mood){
        ApplicationUser user = applicationUserRepository.findByUsername((auth.getName()));
        mood.setUser(user);
        LocalDateTime currentTime = LocalDateTime.now();
        mood.setTimestamp(currentTime);
        moodRepository.save(mood);
    }

    /** Get all moods pertaining to the logged in user */
    @GetMapping
    public List<MoodResponse> getMoods(Authentication auth){
        ApplicationUser user = applicationUserRepository.findByUsername(auth.getName());
        List<Mood>userMoodData = moodRepository.findByUser(user);
        List<MoodResponse> moodResponse = new ArrayList<>();
        for(Mood userMood : userMoodData){
            MoodResponse mood = new MoodResponse(userMood);
            moodResponse.add(mood);
        }
        return moodResponse;
    }

    /** Update attributes of a mood */
    @PutMapping("/{id}")
    public void updateMood(@PathVariable long id, @RequestBody Mood requestBody) throws Exception {
        Mood mood = moodRepository.findById(id).orElseThrow(() -> new Exception("This mood does not exist!"));
        mood.setLabel(requestBody.getLabel());
        mood.setNote(requestBody.getNote());
        moodRepository.save(mood);
    }

    /** Delete a mood */
    @DeleteMapping("/{id}")
    public void deleteMood(@PathVariable long id) throws Exception {
        Mood mood = moodRepository.findById(id).orElseThrow(() -> new Exception("This mood does not exist!"));
        moodRepository.delete(mood);
    }
}
