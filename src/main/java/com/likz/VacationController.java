package com.likz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class VacationController {

    VacationService service;

    @Autowired
    public VacationController(VacationService service) {
        this.service = service;
    }

    @GetMapping("/calculacte")
    public ResponseEntity<?> getVacationMoney(@RequestParam("salary") double salary,
                                              @RequestParam("vacationDays") int vacationDays,
                                              @RequestParam(value = "vacationStart", required = false)
                                              @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate vacationStart
    ) {
        if (vacationStart == null) {
            return service.getVacationMoney(salary, vacationDays);
        }
        return service.getVacationMoneyByDay(salary, vacationDays, vacationStart);
    }

//    @GetMapping("/calculacte")
//    public ResponseEntity<?> getVacationMoney(@RequestBody VacationDTO body) {
//
//        if (body.getVacationStart() == null) {
//            return service.getVacationMoney(body.getSalary(), body.getVacationDays());
//        }
//        return service.getVacationMoneyByDay(body.getSalary(), body.getVacationDays(), body.getVacationStart());
//    }

}
