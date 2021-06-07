package com.jnk2016.happyplannerbackend.income;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import com.jnk2016.happyplannerbackend.user.ApplicationUserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/income")
public class IncomeController {
    private ApplicationUserRepository applicationUserRepository;
    private IncomeRepository incomeRepository;
    private Sort sort = Sort.by("dateReceived").descending();

    public IncomeController(ApplicationUserRepository applicationUserRepository, IncomeRepository incomeRepository){
        this.applicationUserRepository = applicationUserRepository;
        this.incomeRepository = incomeRepository;
    }

    /** Create a new water intake for the logged in user */
    @PostMapping
    public List<HashMap<String, Object>> newSavingsGoal(Authentication auth, @RequestBody Income entry){
        ApplicationUser user = applicationUserRepository.findByUsername((auth.getName()));
        entry.setUser(user);
        entry.setNextDateReceived(entry.getDateReceived().plusWeeks(entry.getWeeksUntilRepeat()));
        incomeRepository.save(entry);

        List<HashMap<String, Object>> allResponseData = new ArrayList<>();
        allResponseData.add(entry.toResponse());

        LocalDate lastDateOfMonth = entry.getNextDateReceived();
        while(lastDateOfMonth.getMonth().compareTo(LocalDate.now().getMonth()) == 0){
            Income newEntry = new Income();
            newEntry.setUser(user);
            newEntry.setAmount(entry.getAmount());
            newEntry.setEndDate(entry.getEndDate());
            newEntry.setDayRepeat(entry.getDayRepeat());
            newEntry.setName(entry.getName());
            newEntry.setWeeksUntilRepeat(entry.getWeeksUntilRepeat());
            newEntry.setNote(entry.getNote());
            newEntry.setDateReceived(lastDateOfMonth);
            newEntry.setNextDateReceived(newEntry.getDateReceived().plusWeeks(newEntry.getWeeksUntilRepeat()));
            incomeRepository.save(newEntry);
            allResponseData.add(newEntry.toResponse());
            lastDateOfMonth = newEntry.getNextDateReceived();
        }
        return allResponseData;
    }

    @GetMapping
    public HashMap<String, Object> getMonthIncome(Authentication auth) {
        HashMap<String, Object> response = new HashMap<>();
        ApplicationUser user = applicationUserRepository.findByUsername((auth.getName()));
        List<Income> incomeData = incomeRepository.findByUser(user, sort);

        double totalMonthlyIncome = 0;
        List<HashMap<String, Object>> monthlyIncome = new ArrayList<>();
        for(Income record : incomeData){
            if(record.getDateReceived().getMonth().compareTo(LocalDate.now().getMonth()) == 0){
                monthlyIncome.add(record.toResponse());
                totalMonthlyIncome+=record.getAmount();
            }
            else if (record.getDateReceived().getMonth().compareTo(LocalDate.now().getMonth()) < 0){
                break;
            }
        }
        response.put("total_month_income", totalMonthlyIncome);
        response.put("sources", monthlyIncome);
        return response;
    }
}
