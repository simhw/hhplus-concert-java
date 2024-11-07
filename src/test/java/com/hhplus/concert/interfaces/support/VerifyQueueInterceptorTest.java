package com.hhplus.concert.interfaces.support;

import com.hhplus.concert.domain.queue.Queue;
import com.hhplus.concert.domain.queue.QueueRepository;
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
class VerifyQueueInterceptorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QueueRepository queueRepository;

    private final String VALID_TOKEN = "valid-token";
    private final String INVALID_TOKEN = "invalid-token";

    @BeforeEach
    public void init() throws CoreException {
        Queue mockQueue = Mockito.mock(Queue.class);
        when(queueRepository.getActiveQueue(VALID_TOKEN)).thenReturn(mockQueue);
        doThrow(new CoreException(ErrorType.NOT_ACTIVE_QUEUE, INVALID_TOKEN))
                .when(queueRepository).getActiveQueue(INVALID_TOKEN);
    }

    @Test
    public void 대기열_활성화_토큰_검증() throws Exception {
        mockMvc.perform(get("/api/concerts")
                .header("Queue-Token", VALID_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void 대기열_비활성화_토큰_검증() throws Exception {
        mockMvc.perform(get("/api/concerts")
                        .header("Queue-Token", INVALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

}