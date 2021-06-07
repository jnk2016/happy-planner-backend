package com.jnk2016.happyplannerbackend.payment;

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
@RequestMapping("/payment")
public class PaymentController {
    private ApplicationUserRepository applicationUserRepository;
    private PaymentRepository paymentRepository;
    private Sort sort = Sort.by("dueDate").descending();

    public PaymentController(ApplicationUserRepository applicationUserRepository, PaymentRepository paymentRepository){
        this.applicationUserRepository = applicationUserRepository;
        this.paymentRepository = paymentRepository;
    }

    /** Create a new water intake for the logged in user */
    @PostMapping
    public List<HashMap<String, Object>> newSavingsGoal(Authentication auth, @RequestBody Payment entry){
        ApplicationUser user = applicationUserRepository.findByUsername((auth.getName()));
        entry.setUser(user);
        entry.setNextDueDate(entry.getDueDate().plusWeeks(entry.getWeeksUntilRepeat()));
        paymentRepository.save(entry);

        List<HashMap<String, Object>> allResponseData = new ArrayList<>();
        allResponseData.add(entry.toResponse());

        LocalDate lastDateOfMonth = entry.getNextDueDate();
        while(lastDateOfMonth.getMonth().compareTo(LocalDate.now().getMonth()) == 0){
            Payment newEntry = new Payment();
            newEntry.setUser(user);
            newEntry.setAmount(entry.getAmount());
            newEntry.setEndDate(entry.getEndDate());
            newEntry.setDayRepeat(entry.getDayRepeat());
            newEntry.setName(entry.getName());
            newEntry.setWeeksUntilRepeat(entry.getWeeksUntilRepeat());
            newEntry.setNote(entry.getNote());
            newEntry.setDueDate(lastDateOfMonth);
            newEntry.setNextDueDate(newEntry.getDueDate().plusWeeks(newEntry.getWeeksUntilRepeat()));
            paymentRepository.save(newEntry);
            allResponseData.add(newEntry.toResponse());
            lastDateOfMonth = newEntry.getNextDueDate();
        }
        return allResponseData;
    }

    // gets all of the monthly payments due this month and SHOULD IMPLEMENT adding new entries for this month that do not surpass the "end date"
    @PostMapping("/month")
    public HashMap<String, Object> getMonthPayment(Authentication auth) {
        HashMap<String, Object> response = new HashMap<>();
        ApplicationUser user = applicationUserRepository.findByUsername((auth.getName()));
        List<Payment> paymentData = paymentRepository.findByUser(user, sort);

        double totalMonthlyExpenses = 0;
        int totalPaid = 0;
        List<HashMap<String, Object>> monthlyPayment = new ArrayList<>();
        for(Payment record : paymentData){
            if(record.getDueDate().getMonth().compareTo(LocalDate.now().getMonth()) == 0){
                monthlyPayment.add(record.toResponse());
                totalMonthlyExpenses+=record.getAmount();
                totalPaid += record.getPaid() ? 1 : 0;
            }
            else if (record.getDueDate().getMonth().compareTo(LocalDate.now().getMonth()) < 0){
                break;
            }
        }
        response.put("total_month_expenses", totalMonthlyExpenses);
        response.put("total_paid", totalPaid);
        response.put("sources", monthlyPayment);
        return response;
    }

    // Should update a payment and SHOULD delete all payments after the specified date and then go back and create dates corresponding to the new interval if there is one
    @PostMapping("/{id}")
    public void updatePayment(@PathVariable long id, @RequestBody Payment updatedEntry) throws Exception {
        Payment oldEntry = paymentRepository.findById(id).orElseThrow(()-> new Exception("none found!"));
        updatedEntry.setUser(oldEntry.getUser());
        updatedEntry.setPaymentId((oldEntry.getPaymentId()));
        updatedEntry.setNextDueDate(updatedEntry.getDueDate().plusWeeks(updatedEntry.getWeeksUntilRepeat()));
    }

    // PostMapping that gets all of the incomes for the week and maybe THIS ONE SHOULD add new entries for the month if needed and not surpassing "end date"
}
