package ec.edu.espe.arquitectura.banquito.loan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.arquitectura.banquito.loan.dto.AmortizationRQ;
import ec.edu.espe.arquitectura.banquito.loan.dto.AmortizationRS;
import ec.edu.espe.arquitectura.banquito.loan.dto.AmortizationSimulationRQ;
import ec.edu.espe.arquitectura.banquito.loan.model.Amortization;
import ec.edu.espe.arquitectura.banquito.loan.service.AmortizationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AmortizationController.class)
class AmortizationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AmortizationService amortizationService;
    @Autowired
    private ObjectMapper objectMapper;
    private static final String URL = "/api/v1/amortization";
    @Test
    void generateAmortization() throws Exception {
        //given
        AmortizationRQ amortizationRQ = AmortizationRQ.builder()
                .loanId(1)
                .type("FRA")
                .build();
        Amortization amortization = Amortization.builder()
                .id(1)
                .loanId(1)
                .uuid(UUID.randomUUID().toString())
                .type("FRA")
                .quotaNum(1)
                .dueDate(new Date())
                .quotaCapital(BigDecimal.valueOf(10000))
                .quotaInterest(BigDecimal.valueOf(100))
                .quotaAmount(BigDecimal.valueOf(10100))
                .remainingBalance(BigDecimal.valueOf(10000))
                .quotaStatus("PEN")
                .build();
        Amortization amortization1 = Amortization.builder()
                .id(2)
                .loanId(1)
                .uuid(UUID.randomUUID().toString())
                .type("FRA")
                .quotaNum(2)
                .dueDate(new Date())
                .quotaCapital(BigDecimal.valueOf(10000))
                .quotaInterest(BigDecimal.valueOf(100))
                .quotaAmount(BigDecimal.valueOf(10100))
                .remainingBalance(BigDecimal.valueOf(10000))
                .quotaStatus("PEN")
                .build();
        given(amortizationService.generateAmortization(amortizationRQ)).willReturn(List.of(amortization, amortization1));
        //when
        ResultActions response = mockMvc.perform(post(URL + "/generate")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(amortizationRQ)));
        //then
        response.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void simulateAmortization() throws Exception {
        //given
        AmortizationSimulationRQ amortizationRQ = AmortizationSimulationRQ.builder()
                .type("FRA")
                .amount(BigDecimal.valueOf(10000))
                .repaymentInstallments(10)
                .build();
        AmortizationRS amortizationRS = AmortizationRS.builder()
                .id(1)
                .loanUuid(UUID.randomUUID().toString())
                .uuid(UUID.randomUUID().toString())
                .type("FRA")
                .quotaNum(1)
                .dueDate(new Date())
                .quotaCapital(BigDecimal.valueOf(10000))
                .quotaInterest(BigDecimal.valueOf(100))
                .quotaAmount(BigDecimal.valueOf(10100))
                .remainingBalance(BigDecimal.valueOf(10000))
                .quotaStatus("PEN")
                .build();
        List<AmortizationRS> amortizationRSList = List.of(amortizationRS);
        given(amortizationService.simulateAmortization(amortizationRQ)).willReturn(amortizationRSList);
        //when
        ResultActions response = mockMvc.perform(post(URL + "/simulate")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(amortizationRQ)));
        //then
        response.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void findByLoanTest() throws Exception {
        //given
        String uuid = UUID.randomUUID().toString();
        AmortizationRS amortizationRS = AmortizationRS.builder()
                .id(1)
                .loanUuid(uuid)
                .uuid(UUID.randomUUID().toString())
                .type("FRA")
                .quotaNum(1)
                .dueDate(new Date())
                .quotaCapital(BigDecimal.valueOf(10000))
                .quotaInterest(BigDecimal.valueOf(100))
                .quotaAmount(BigDecimal.valueOf(10100))
                .remainingBalance(BigDecimal.valueOf(10000))
                .quotaStatus("PEN")
                .build();
        AmortizationRS amortizationRS1 = AmortizationRS.builder()
                .id(2)
                .loanUuid(uuid)
                .uuid(UUID.randomUUID().toString())
                .type("FRA")
                .quotaNum(2)
                .dueDate(new Date())
                .quotaCapital(BigDecimal.valueOf(10000))
                .quotaInterest(BigDecimal.valueOf(100))
                .quotaAmount(BigDecimal.valueOf(10100))
                .remainingBalance(BigDecimal.valueOf(10000))
                .quotaStatus("PEN")
                .build();
        List<AmortizationRS> amortizationRSList = List.of(amortizationRS, amortizationRS1);
        given(amortizationService.findByLoanUuid(uuid)).willReturn(amortizationRSList);
        //when
        ResultActions response = mockMvc.perform(get(URL + "/findByLoan/" + uuid)
                .contentType("application/json"));
        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> {
                    List<AmortizationRS> actual = List.of(objectMapper.readValue(result.getResponse().getContentAsString(), AmortizationRS[].class));
                    assertEquals(2, actual.size());
                });
    }

    @Test
    void findByLoanAndQuotaStatus() throws Exception {
        //given
        String uuid = UUID.randomUUID().toString();
        String quotaStatus = "PEN";
        AmortizationRS amortizationRS = AmortizationRS.builder()
                .id(1)
                .loanUuid(uuid)
                .uuid(UUID.randomUUID().toString())
                .type("FRA")
                .quotaNum(1)
                .dueDate(new Date())
                .quotaCapital(BigDecimal.valueOf(10000))
                .quotaInterest(BigDecimal.valueOf(100))
                .quotaAmount(BigDecimal.valueOf(10100))
                .remainingBalance(BigDecimal.valueOf(10000))
                .quotaStatus("PEN")
                .build();
        AmortizationRS amortizationRS1 = AmortizationRS.builder()
                .id(2)
                .loanUuid(uuid)
                .uuid(UUID.randomUUID().toString())
                .type("FRA")
                .quotaNum(2)
                .dueDate(new Date())
                .quotaCapital(BigDecimal.valueOf(10000))
                .quotaInterest(BigDecimal.valueOf(100))
                .quotaAmount(BigDecimal.valueOf(10100))
                .remainingBalance(BigDecimal.valueOf(10000))
                .quotaStatus("PEN")
                .build();
        given(amortizationService.findByLoanUuidAndQuotaStatus(uuid, quotaStatus)).willReturn(List.of(amortizationRS, amortizationRS1));
        //when
        ResultActions response = mockMvc.perform(get(URL + "/findByLoan-status/" + uuid + "/" + quotaStatus)
                .contentType("application/json"));
        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> {
                    List<AmortizationRS> actual = List.of(objectMapper.readValue(result.getResponse().getContentAsString(), AmortizationRS[].class));
                    assertEquals(2, actual.size());
                });
    }
}