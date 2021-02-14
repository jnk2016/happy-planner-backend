package com.jnk2016.happyplannerbackend.daysymptoms;

import com.jnk2016.happyplannerbackend.cycle.Cycle;
import com.jnk2016.happyplannerbackend.cycle.CycleRepository;
import org.springframework.web.bind.annotation.*;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/symptoms")
public class DaySymptomsController {
    private CycleRepository cycleRepository;
    private DaySymptomsRepository daySymptomsRepository;

    public DaySymptomsController(CycleRepository cycleRepository , DaySymptomsRepository daySymptomsRepository){
        this.cycleRepository = cycleRepository;
        this.daySymptomsRepository = daySymptomsRepository;
    }

    /** Add symptoms for a given day */
    @PostMapping
    public DaySymptomsResponse logSymptoms(@RequestBody DaySymptomsResponse newDaySymptomsRequest) throws Exception {
        Cycle cycle = cycleRepository.findById(newDaySymptomsRequest.getCycleId()).orElseThrow(()-> new Exception("No cycle found"));
        DaySymptoms daySymptoms = new DaySymptoms(newDaySymptomsRequest);
        daySymptoms.setCycleDay(ChronoUnit.DAYS.between(cycle.getRecordedPeriodStart(), daySymptoms.getDate())+1);
        daySymptoms.setCycle(cycle);
        daySymptomsRepository.save(daySymptoms);
        return new DaySymptomsResponse(daySymptoms);
    }

    /** Get the all of the symptoms for all days recorded in a given user cycle */
    @GetMapping("/cycle/{id}")
    public List<DaySymptomsResponse> getCyclesSymptoms(@PathVariable long id) throws Exception {
        Cycle cycle = cycleRepository.findById(id).orElseThrow(()-> new Exception("No cycle found"));
        List<DaySymptoms> cycleDaysData = daySymptomsRepository.findByCycle(cycle);
        List<DaySymptomsResponse> cycleDaysResponse = new ArrayList<>();
        for(DaySymptoms cycleDay : cycleDaysData){
            DaySymptomsResponse daySymptoms = new DaySymptomsResponse(cycleDay);
            cycleDaysResponse.add(daySymptoms);
        }
        return cycleDaysResponse;
    }

    /** Get the symptoms of a specific day using the dayId */
    @GetMapping("/{id}")
    public DaySymptomsResponse getDaySymptoms(@PathVariable long id) throws Exception {
        DaySymptoms daySymptoms = daySymptomsRepository.findById(id).orElseThrow(()->new Exception("No cycle found"));
        return new DaySymptomsResponse(daySymptoms);
    }

    /** Update the symptoms of a specific day using the dayId */
    @PutMapping("/{id}")
    public DaySymptomsResponse updateDaySymptoms(@PathVariable long id, @RequestBody DaySymptomsResponse requestBody) throws Exception {
        DaySymptoms daySymptoms = daySymptomsRepository.findById(id).orElseThrow(()->new Exception("No cycle found"));
        daySymptoms.updateSymptoms(requestBody);
        daySymptomsRepository.save(daySymptoms);
        return new DaySymptomsResponse(daySymptoms);
    }
}
