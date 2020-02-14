package com.practice;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;

import java.io.IOException;

import okhttp3.mockwebserver.MockWebServer;

/**
 * Base class for test classes desiring to have mockserver
 * for simulating network operation.
 */
public class MockWebServerClient {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    // Create the mock mServer
    protected MockWebServer mServer = new MockWebServer();

    @Before
    public void setup() throws IOException {
        mServer.start();
    }

    @After
    public void teardown() throws IOException {
        mServer.shutdown();
    }

    public String getMockUrl() {
        return getMockUrl("");
    }

    public String getMockUrl(String path) {
        return mServer.url(path).toString();
    }
}
