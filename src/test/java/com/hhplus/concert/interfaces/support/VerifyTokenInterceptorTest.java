package com.hhplus.concert.interfaces.support;

import com.hhplus.concert.domain.queue.ActiveQueueRepository;
import com.hhplus.concert.domain.queue.Token;
import com.hhplus.concert.domain.support.error.CoreException;
import com.hhplus.concert.domain.support.error.ErrorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VerifyTokenInterceptorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActiveQueueRepository activeQueueRepository;

    private final String ACTIVE_QUEUE_TOKEN = "active-token";
    private final String WAITING_QUEUE_TOKEN = "waiting-token";

    @BeforeEach
    public void init() throws CoreException {
        Token mockToken = Mockito.mock(Token.class);
        when(activeQueueRepository.getActiveQueueToken(mockToken)).thenReturn(mockToken);

        doThrow(new CoreException(ErrorType.NOT_ACTIVE_QUEUE, ACTIVE_QUEUE_TOKEN))
                .when(activeQueueRepository).getActiveQueueToken(mockToken);
    }

    @Test
    public void 대기열_활성화_토큰_검증() throws Exception {
        mockMvc.perform(get("/api/concerts")
                        .header("Queue-Token", ACTIVE_QUEUE_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void 대기열_비활성화_토큰_검증() throws Exception {
        mockMvc.perform(get("/api/concerts")
                        .header("Queue-Token", WAITING_QUEUE_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

}