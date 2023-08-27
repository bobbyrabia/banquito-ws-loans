package ec.edu.espe.arquitectura.banquito.loan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.arquitectura.banquito.loan.dto.RepaymentRQ;
import ec.edu.espe.arquitectura.banquito.loan.dto.RepaymentRS;
import ec.edu.espe.arquitectura.banquito.loan.model.Loan;
import ec.edu.espe.arquitectura.banquito.loan.model.Repayment;
import ec.edu.espe.arquitectura.banquito.loan.service.RepaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RepaymentController.class)
class RepaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RepaymentService repaymentService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String URL = "/api/v1/repayment";

    @Test
    void doPay() throws Exception {
        //given
        RepaymentRQ repaymentRQ = RepaymentRQ.builder()
                .amortizationUuid("123")
                .branchId(1)
                .accountTransactionId(1)
                .amountToPay(BigDecimal.valueOf(10000))
                .build();
        given(repaymentService.doPayment(repaymentRQ)).willReturn(Repayment.builder()
                .id(1)
                .loanId(1)
                .amortizationId(1)
                .state("PEN")
                .dueDate(new Date())
                .repaidDate(new Date())
                .principalDue(BigDecimal.valueOf(10000))
                .principalPaid(BigDecimal.valueOf(10000))
                .build());
        //when
        ResultActions resultActions = mockMvc.perform(post(URL + "/doPay")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(repaymentRQ)));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getRepayment() throws Exception {
        //given
        Loan loan = Loan.builder()
                .id(1)
                .uuid("123")
                .build();
        RepaymentRS repaymentRS = RepaymentRS.builder()
                .id(1)
                .loanId(loan.getId())
                .amortizationId(1)
                .state("PEN")
                .dueDate(new Date())
                .repaidDate(new Date())
                .principalDue(BigDecimal.valueOf(10000))
                .principalPaid(BigDecimal.valueOf(10000))
                .build();
        given(repaymentService.findByLoan_Uuid(loan.getUuid())).willReturn(List.of(repaymentRS));
        //when
        ResultActions resultActions = mockMvc.perform(get(URL + "/getRepayment/" + loan.getUuid())
                .contentType("application/json"));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> {
                    List<RepaymentRS> repaymentRSList = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
                    assertEquals(1, repaymentRSList.size());
                });
    }
}