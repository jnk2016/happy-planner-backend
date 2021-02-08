package com.jnk2016.happyplannerbackend.mood;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoodResponse {
    private long moodId;
    private int label;
    private String note = "";
    private LocalDateTime timestamp;

    public MoodResponse(Mood mood){
        this.moodId = mood.getMoodId();
        this.label = mood.getLabel();
        this.note = mood.getNote();
        this.timestamp = mood.getTimestamp();
    }
}
