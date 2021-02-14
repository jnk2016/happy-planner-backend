package com.jnk2016.happyplannerbackend.cycle;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import com.jnk2016.happyplannerbackend.user.ApplicationUserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/cycles")
public class CycleController {
    private ApplicationUserRepository applicationUserRepository;
    private CycleRepository cycleRepository;
    private Sort sort = Sort.by("recordedPeriodStart").descending();

    public CycleController(ApplicationUserRepository applicationUserRepository, CycleRepository cycleRepository){
        this.applicationUserRepository = applicationUserRepository;
        this.cycleRepository = cycleRepository;
    }

    /** Add a new cycle (start of a new period) and can be initial period */
    @PostMapping
    public CycleResponse logCycle(Authentication auth, @RequestBody CycleRequest newCycle){
        ApplicationUser user = applicationUserRepository.findByUsername((auth.getName()));

        if(newCycle.firstCycle==true){
            newCycle.previousPeriodLength -= 1;
            newCycle.recentPeriodEnd = newCycle.recentPeriodStart.plusDays(newCycle.previousPeriodLength);
        }
        else{
            Cycle previousCycle = cycleRepository.findFirstByUser(user, sort);
            newCycle.recentPeriodEnd = null;

            long cycleSpanDays = ChronoUnit.DAYS.between(previousCycle.getRecordedPeriodStart(), newCycle.recentPeriodStart);    // +1 in Cycle.java constructor
            newCycle.previousCycleLength = cycleSpanDays;

            long periodSpanDays = ChronoUnit.DAYS.between(previousCycle.getRecordedPeriodStart(), previousCycle.getRecordedPeriodEnd());
            newCycle.previousPeriodLength = periodSpanDays;

            // Record the date before new period start as the end of the previous cycle
            previousCycle.setRecordedCycleEnd(newCycle.recentPeriodStart.minusDays(1));
        }

        Cycle cycle = new Cycle(newCycle.recentPeriodStart, newCycle.recentPeriodEnd, newCycle.previousCycleLength, newCycle.previousPeriodLength);
        cycle.setUser(user);
        cycleRepository.save(cycle);
        return new CycleResponse(cycle);
    }

    /** Get all cycles pertaining to the logged in user */
    @GetMapping
    public List<CycleResponse> getCycles(Authentication auth){
        ApplicationUser user = applicationUserRepository.findByUsername(auth.getName());
        List<Cycle>userCycleData = cycleRepository.findByUser(user);
        List<CycleResponse> cycleResponse = new ArrayList<>();
        for(Cycle userCycle : userCycleData){
            CycleResponse cycle = new CycleResponse(userCycle);
            cycleResponse.add(cycle);
        }
        return cycleResponse;
    }

    /** Get the average cycle length and average period length for the user */
    @GetMapping("/average")
    public HashMap<String, Long> averageStatistics(Authentication auth){
        HashMap<String, Long> response = new HashMap<>();
        ApplicationUser user = applicationUserRepository.findByUsername(auth.getName());
        List<Cycle>userCycleData = cycleRepository.findByUser(user);
        long avrgCycleLength = 0;
        long avrgPeriodLength = 0;
        long cycles = 0;
        for(Cycle userCycle : userCycleData){
            if(userCycle.getRecordedCycleEnd() != null){
                avrgCycleLength += ChronoUnit.DAYS.between(userCycle.getRecordedPeriodStart(),userCycle.getRecordedCycleEnd())+1;
                avrgPeriodLength += ChronoUnit.DAYS.between(userCycle.getRecordedPeriodStart(),userCycle.getRecordedPeriodEnd())+1;
                cycles += 1;
            }
        }
        avrgCycleLength /= cycles;
        avrgPeriodLength /= cycles;
        response.put("averageCycleLength", avrgCycleLength);
        response.put("averagePeriodLength",avrgPeriodLength);
        return response;
    }

    /** Get the user's current cycle */
    @GetMapping("/current")
    public CycleResponse currentCycle(Authentication auth){
        ApplicationUser user = applicationUserRepository.findByUsername(auth.getName());
        Cycle currentCycle = cycleRepository.findFirstByUser(user, sort);
        CycleResponse response = new CycleResponse(currentCycle);
        return response;
    }

    /** Updates period dates */
    @PutMapping("/{id}")
    public CycleResponse updateCycle(@PathVariable long id, @RequestBody Cycle requestBody) throws Exception {
        Cycle cycle = cycleRepository.findById(id).orElseThrow(() -> new Exception("This mood does not exist!"));
        cycle.setRecordedPeriodStart(requestBody.getRecordedPeriodStart());
        cycle.setRecordedPeriodEnd(requestBody.getRecordedPeriodEnd());
        cycleRepository.save(cycle);
        return new CycleResponse(cycle);
    }

    /** Delete a cycle */
    @DeleteMapping("/{id}")
    public void deleteCycle(@PathVariable long id) throws Exception {
        Cycle cycle = cycleRepository.findById(id).orElseThrow(() -> new Exception("This cycle does not exist!"));
        cycleRepository.delete(cycle);
    }
}
