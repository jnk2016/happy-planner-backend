package com.jnk2016.happyplannerbackend.cycle;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import com.jnk2016.happyplannerbackend.user.ApplicationUserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
    public CycleResponse logCycle(Authentication auth, @RequestBody CycleRequest requestBody){
        ApplicationUser user = applicationUserRepository.findByUsername((auth.getName()));
        System.out.println(requestBody.getRecentPeriodStart());

        if(requestBody.firstCycle==true){
            requestBody.previousPeriodLength -= 1;
            requestBody.recentPeriodEnd = requestBody.recentPeriodStart.plusDays(requestBody.previousPeriodLength);
        }
        else{
            Cycle previousCycle = cycleRepository.findFirstByUser(user, sort);
            requestBody.recentPeriodEnd = null;

            long cycleSpanDays = ChronoUnit.DAYS.between(previousCycle.getRecordedPeriodStart(), requestBody.recentPeriodStart);    // +1 in Cycle.java constructor
            requestBody.previousCycleLength = cycleSpanDays;

            long periodSpanDays = ChronoUnit.DAYS.between(previousCycle.getRecordedPeriodStart(), previousCycle.getRecordedPeriodEnd());
            requestBody.previousPeriodLength = periodSpanDays;
        }

        Cycle cycle = new Cycle(requestBody.recentPeriodStart, requestBody.recentPeriodEnd, requestBody.previousCycleLength, requestBody.previousPeriodLength);
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
