package com.jnk2016.happyplannerbackend.braindumpnote;
import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="brain_dump_note")
@Data
public class BrainDumpNote implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="note_id")
    private long noteId;
    private int type;
    private String title;

    @Column(columnDefinition = "TEXT")
    private String note;
//    @Column(length = 32000)
    private String high = null;   // Priority takeaway
//    @Column(length = 32000)
    private String med = null;
//    @Column(length = 32000)
    private String low = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private ApplicationUser user;
}
