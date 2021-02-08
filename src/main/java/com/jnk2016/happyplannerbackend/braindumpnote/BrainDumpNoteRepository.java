package com.jnk2016.happyplannerbackend.braindumpnote;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrainDumpNoteRepository extends JpaRepository<BrainDumpNote,Long> {
    List<BrainDumpNote> findByUser(ApplicationUser user);
}
