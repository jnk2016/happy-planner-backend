package com.jnk2016.happyplannerbackend.braindumpnote;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import com.jnk2016.happyplannerbackend.user.ApplicationUserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dumps")
public class BrainDumpNoteController {
    private ApplicationUserRepository applicationUserRepository;
    private BrainDumpNoteRepository brainDumpNoteRepository;

    public BrainDumpNoteController(ApplicationUserRepository applicationUserRepository, BrainDumpNoteRepository brainDumpNoteRepository){
        this.applicationUserRepository = applicationUserRepository;
        this.brainDumpNoteRepository = brainDumpNoteRepository;
    }

    /** Create a new dump note for the logged in user */
    @PostMapping
    public void newNote(Authentication auth, @RequestBody BrainDumpNote brainDumpNote){
        ApplicationUser user = applicationUserRepository.findByUsername((auth.getName()));
        brainDumpNote.setUser(user);
        brainDumpNoteRepository.save(brainDumpNote);
    }

    /** Get all dump notes pertaining to the logged in user */
    @GetMapping
    public List<BrainDumpNoteResponse> getNote(Authentication auth){
        ApplicationUser user = applicationUserRepository.findByUsername(auth.getName());
        List<BrainDumpNote>userDumpNoteData = brainDumpNoteRepository.findByUser(user);
        List<BrainDumpNoteResponse> brainDumpNoteResponse = new ArrayList<>();
        for(BrainDumpNote userDumpNote : userDumpNoteData){
            BrainDumpNoteResponse brainDumpNote = new BrainDumpNoteResponse(userDumpNote);
            brainDumpNoteResponse.add(brainDumpNote);
        }
        return brainDumpNoteResponse;
    }

    /** Update attributes of a dump note */
    @PutMapping("/{id}")
    public void updateNote(@PathVariable long id, @RequestBody BrainDumpNote requestBody) throws Exception {
        BrainDumpNote brainDumpNote = brainDumpNoteRepository.findById(id).orElseThrow(() -> new Exception("This note does not exist!"));
        brainDumpNote.setTitle(requestBody.getTitle());
        brainDumpNote.setNote(requestBody.getNote());
        brainDumpNote.setHigh(requestBody.getHigh());
        brainDumpNote.setMed(requestBody.getMed());
        brainDumpNote.setLow(requestBody.getLow());
        brainDumpNoteRepository.save(brainDumpNote);
    }

    /** Delete a dump note */
    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable long id) throws Exception {
        BrainDumpNote brainDumpNote = brainDumpNoteRepository.findById(id).orElseThrow(() -> new Exception("This note does not exist!"));
        brainDumpNoteRepository.delete(brainDumpNote);
    }
}
