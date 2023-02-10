package se.kth.assignment2;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
//test class
class StatusHandlerTest {
    private StatusHandler statusHandler;
    private String branch = "main";
    private String clone_url = "https://github.com/mrporsev/DD2480-Assignment2.git";
    private String commitHash = "1234567890123456789012345678901234567890";
    private String outputBuild = "Build output";
    private Build.BuildStatus buildStatus = Build.BuildStatus.SUCCESS;

    @Mock
    private CloseableHttpResponse response;

    @BeforeEach
    public void setUp() {
        response = mock(CloseableHttpResponse.class);
        statusHandler = new StatusHandler(branch, clone_url, commitHash, outputBuild, buildStatus);
    }

    @Test
    void sendStatusCorrect() throws ClientProtocolException, IOException {
        statusHandler.sendStatusCorrect();
        verify(response, times(0)).close();
    }

    @Test
    void sendStatusFailure() throws ClientProtocolException, IOException {
        statusHandler.sendStatusFailure();
        verify(response, times(0)).close();
    }

    @Test
    void sendStatusPending() throws ClientProtocolException, IOException {
        statusHandler.sendStatusPending();
        verify(response, times(0)).close();
    }
}