package com.jnk2016.happyplannerbackend.braindumpnote;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrainDumpNoteResponse {
    private long noteId;
    private int type;
    private String title;
    private String note;
    private String high;   // Priority takeaway
    private String med;
    private String low;

    public BrainDumpNoteResponse(BrainDumpNote brainDumpNote){
        this.noteId  = brainDumpNote.getNoteId();
        this.type    = brainDumpNote.getType();
        this.title   = brainDumpNote.getTitle();
        this.note    = brainDumpNote.getNote();
        this.high    = brainDumpNote.getHigh();
        this.med     = brainDumpNote.getMed();
        this.low     = brainDumpNote.getLow();
    }
}
