package com.jnk2016.happyplannerbackend.daysymptoms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DaySymptomsResponse {
    long dayId;
    LocalDate date;
    long cycleId;
    private long cycleDay;

    Intimacy intimacy;
    Flow flow;
    Discharge discharge;
    Other other;
    // Intimacy
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Intimacy{
        Boolean protect = false;
        Boolean unprotected = false;
        Boolean highdrive = false;
        Boolean nodrive = false;
    }
    // Flow
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Flow{
        Boolean spotting = false;
        Boolean light = false;
        Boolean regular = false;
        Boolean heavy = false;
    }
    // Discharge
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Discharge{
        Boolean clear = false;
        Boolean white = false;
        Boolean gray = false;
        Boolean yellow = false;
        Boolean green = false;
        Boolean pink = false;
        Boolean red = false;
        Boolean stretchy = false;
        Boolean creamy = false;
        Boolean sticky = false;
        Boolean dry = false;
    }
    // Other Symptoms
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Other{
        Boolean cramps = false;
        Boolean tender = false;
        Boolean headache = false;
        Boolean bloating = false;
        Boolean diarrhea = false;
        Boolean constipation = false;
        Boolean insomnia = false;
        Boolean cravings = false;
    }

    // For response body of GET requests
    public DaySymptomsResponse(DaySymptoms daySymptoms){
        this.setDayId(daySymptoms.getDayId());
        this.setDate(daySymptoms.getDate());
        this.setCycleDay(daySymptoms.getCycleDay());

        this.setIntimacy(new Intimacy(daySymptoms.getProtect(), daySymptoms.getUnprotected(), daySymptoms.getHighdrive(), daySymptoms.getNodrive()));
        this.setFlow(new Flow(daySymptoms.getSpotting(),daySymptoms.getLight(),daySymptoms.getRegular(),daySymptoms.getHeavy()));
        this.setDischarge(new Discharge(daySymptoms.getClear(),daySymptoms.getWhite(),daySymptoms.getGray(),daySymptoms.getYellow(),daySymptoms.getGreen(),daySymptoms.getPink(),daySymptoms.getRed(),daySymptoms.getStretchy(),daySymptoms.getCreamy(),daySymptoms.getSticky(),daySymptoms.getDry()));
        this.setOther(new Other(daySymptoms.getCramps(),daySymptoms.getTender(),daySymptoms.getHeadache(),daySymptoms.getBloating(),daySymptoms.getDiarrhea(),daySymptoms.getConstipation(),daySymptoms.getInsomnia(),daySymptoms.getCravings()));

        this.setCycleId(daySymptoms.getCycle().getCycleId());
    }
}
