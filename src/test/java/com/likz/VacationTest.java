package com.likz;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class VacationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VacationService service;

    @Test
    public void testController() throws Exception {

        ResponseEntity expectedResponse = ResponseEntity.ok("Expected mock response");
        Mockito.when(service.getVacationMoneyByDay(480000, 14, LocalDate.parse("2024-05-01")))
                .thenReturn(expectedResponse);

        mockMvc.perform(get("/calculacte")
                        .param("salary", "480000")
                        .param("vacationDays", "14")
                        .param("vacationStart", "2024-05-01")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Expected mock response"));
    }

    @Test
    public void testVacationMoney() {
        
        double salary = 480000;
        int vacationDays = 14;
        LocalDate vacationStart = LocalDate.parse("2024-05-01");

        // не учитывается 1 и 9 мая. 13% ндфл
        double expectedMoney = ((salary / 12) / 29.3) * 12 * 0.87;
        expectedMoney = roundTo2Dec(expectedMoney);

        VacationService vacService = new VacationService();
        ResponseEntity<?> response = vacService.getVacationMoneyByDay(salary, vacationDays, vacationStart);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(expectedMoney, response.getBody());
    }

    // округление до 2х знаков после запятой (округление в большую сторону при >=.5)
    private double roundTo2Dec(double value) {

        BigDecimal decimal = new BigDecimal(value);
        decimal = decimal.setScale(2, RoundingMode.HALF_UP);
        return decimal.doubleValue();
    }

}
