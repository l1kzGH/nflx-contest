package com.likz;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
public class VacationService {

    // среднее количество дней в месяце за год
    private final double WORKING_DAYS_PER_MONTH = 29.3d;
    // неоплачиваемые дни
    private final Set<LocalDate> NON_WORKING_HOLIDAYS;

    {
        NON_WORKING_HOLIDAYS = new HashSet<>();
        NON_WORKING_HOLIDAYS.add(LocalDate.parse("2024-01-01"));
        NON_WORKING_HOLIDAYS.add(LocalDate.parse("2024-01-02"));
        NON_WORKING_HOLIDAYS.add(LocalDate.parse("2024-01-03"));
        NON_WORKING_HOLIDAYS.add(LocalDate.parse("2024-01-04"));
        NON_WORKING_HOLIDAYS.add(LocalDate.parse("2024-01-05"));
        NON_WORKING_HOLIDAYS.add(LocalDate.parse("2024-01-06"));
        NON_WORKING_HOLIDAYS.add(LocalDate.parse("2024-01-07"));
        NON_WORKING_HOLIDAYS.add(LocalDate.parse("2024-01-08"));
        NON_WORKING_HOLIDAYS.add(LocalDate.parse("2024-02-23"));
        NON_WORKING_HOLIDAYS.add(LocalDate.parse("2024-03-08"));
        NON_WORKING_HOLIDAYS.add(LocalDate.parse("2024-05-01"));
        NON_WORKING_HOLIDAYS.add(LocalDate.parse("2024-05-09"));
        NON_WORKING_HOLIDAYS.add(LocalDate.parse("2024-06-12"));
        NON_WORKING_HOLIDAYS.add(LocalDate.parse("2024-11-04"));
    }

    public ResponseEntity<?> getVacationMoney(double salary, int vacationDays) {

        double result = ((salary / 12) / WORKING_DAYS_PER_MONTH) * vacationDays;
        // ндфл
        result *= 0.87;
        result = roundTo2Dec(result);

        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> getVacationMoneyByDay(double salary, int vacationDays, LocalDate vacationStart) {

        LocalDate vacationEnd = vacationStart.plusDays(vacationDays - 1);

        for (LocalDate date = vacationStart; !date.isAfter(vacationEnd); date = date.plusDays(1)) {
            if (NON_WORKING_HOLIDAYS.contains(date)) {
                vacationDays -= 1;
            }
        }

        double result = ((salary / 12) / WORKING_DAYS_PER_MONTH) * vacationDays;
        // ндфл
        result *= 0.87;
        result = roundTo2Dec(result);

        return ResponseEntity.ok(result);
    }

    // округление до 2х знаков после запятой (округление в большую сторону при >=.5)
    private double roundTo2Dec(double value) {

        BigDecimal decimal = new BigDecimal(value);
        decimal = decimal.setScale(2, RoundingMode.HALF_UP);
        return decimal.doubleValue();
    }

}
