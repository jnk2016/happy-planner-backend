package com.jnk2016.happyplannerbackend.daysymptoms;

import com.jnk2016.happyplannerbackend.cycle.Cycle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "day_symptoms")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DaySymptoms implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="day_id")
    private long dayId;
    private LocalDate date;
    private long cycleDay;

    private Boolean protect;
    private Boolean unprotected;
    private Boolean highdrive;
    private Boolean nodrive;
    // Flow
    private Boolean spotting;
    private Boolean light;
    private Boolean regular;
    private Boolean heavy;
    // Discharge
    private Boolean clear;
    private Boolean white;
    private Boolean gray;
    private Boolean yellow;
    private Boolean green;
    private Boolean pink;
    private Boolean red;
    private Boolean stretchy;
    private Boolean creamy;
    private Boolean sticky;
    private Boolean dry;
    // Other Symptoms
    private Boolean cramps;
    private Boolean tender;
    private Boolean headache;
    private Boolean bloating;
    private Boolean diarrhea;
    private Boolean constipation;
    private Boolean insomnia;
    private Boolean cravings;

    // When a DaySymptomResponse is used to create a new DaySymptoms in POST requests
    public DaySymptoms(DaySymptomsResponse daySymptomsRequest){
        this.date = daySymptomsRequest.getDate();

        this.protect = daySymptomsRequest.getIntimacy().getProtect();
        this.unprotected = daySymptomsRequest.getIntimacy().getUnprotected();
        this.highdrive = daySymptomsRequest.getIntimacy().getHighdrive();
        this.nodrive = daySymptomsRequest.getIntimacy().getNodrive();
        this.spotting = daySymptomsRequest.getFlow().getSpotting();
        this.light = daySymptomsRequest.getFlow().getLight();
        this.regular = daySymptomsRequest.getFlow().getRegular();
        this.heavy = daySymptomsRequest.getFlow().getHeavy();
        this.clear = daySymptomsRequest.getDischarge().getClear();
        this.white = daySymptomsRequest.getDischarge().getWhite();
        this.gray = daySymptomsRequest.getDischarge().gray;
        this.yellow = daySymptomsRequest.getDischarge().getYellow();
        this.green = daySymptomsRequest.getDischarge().green;
        this.pink = daySymptomsRequest.getDischarge().getPink();
        this.red = daySymptomsRequest.getDischarge().getRed();
        this.stretchy = daySymptomsRequest.getDischarge().getStretchy();
        this.creamy = daySymptomsRequest.getDischarge().getCreamy();
        this.sticky = daySymptomsRequest.getDischarge().getSticky();
        this.dry = daySymptomsRequest.getDischarge().getDry();
        this.cramps = daySymptomsRequest.getOther().getCramps();
        this.tender = daySymptomsRequest.getOther().getTender();
        this.headache = daySymptomsRequest.getOther().getHeadache();
        this.bloating = daySymptomsRequest.getOther().getBloating();
        this.diarrhea = daySymptomsRequest.getOther().getDiarrhea();
        this.constipation = daySymptomsRequest.getOther().getConstipation();
        this.insomnia = daySymptomsRequest.getOther().getInsomnia();
        this.cravings = daySymptomsRequest.getOther().getCravings();
    }
    public void updateSymptoms(DaySymptomsResponse daySymptomsRequest){
        this.protect = daySymptomsRequest.getIntimacy().getProtect();
        this.unprotected = daySymptomsRequest.getIntimacy().getUnprotected();
        this.highdrive = daySymptomsRequest.getIntimacy().getHighdrive();
        this.nodrive = daySymptomsRequest.getIntimacy().getNodrive();
        this.spotting = daySymptomsRequest.getFlow().getSpotting();
        this.light = daySymptomsRequest.getFlow().getLight();
        this.regular = daySymptomsRequest.getFlow().getRegular();
        this.heavy = daySymptomsRequest.getFlow().getHeavy();
        this.clear = daySymptomsRequest.getDischarge().getClear();
        this.white = daySymptomsRequest.getDischarge().getWhite();
        this.gray = daySymptomsRequest.getDischarge().gray;
        this.yellow = daySymptomsRequest.getDischarge().getYellow();
        this.green = daySymptomsRequest.getDischarge().green;
        this.pink = daySymptomsRequest.getDischarge().getPink();
        this.red = daySymptomsRequest.getDischarge().getRed();
        this.stretchy = daySymptomsRequest.getDischarge().getStretchy();
        this.creamy = daySymptomsRequest.getDischarge().getCreamy();
        this.sticky = daySymptomsRequest.getDischarge().getSticky();
        this.dry = daySymptomsRequest.getDischarge().getDry();
        this.cramps = daySymptomsRequest.getOther().getCramps();
        this.tender = daySymptomsRequest.getOther().getTender();
        this.headache = daySymptomsRequest.getOther().getHeadache();
        this.bloating = daySymptomsRequest.getOther().getBloating();
        this.diarrhea = daySymptomsRequest.getOther().getDiarrhea();
        this.constipation = daySymptomsRequest.getOther().getConstipation();
        this.insomnia = daySymptomsRequest.getOther().getInsomnia();
        this.cravings = daySymptomsRequest.getOther().getCravings();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cycle_id", nullable = false)
    private Cycle cycle;
}
