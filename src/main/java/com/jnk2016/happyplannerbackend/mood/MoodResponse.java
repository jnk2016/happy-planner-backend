package com.jnk2016.happyplannerbackend.mood;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoodResponse {
    private long moodId;
    private String label;
    private String note = "";
    private LocalDate timestamp;


}
