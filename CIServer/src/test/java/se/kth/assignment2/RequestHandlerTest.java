package se.kth.assignment2;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;

//test class
class RequestHandlerTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getJsonObject() {
    }

    /*
     * Tests whether the method in RequestHandler can pick out the correct branch
     * from the JSON object.
     * Should return true.
     */
    @Test
    void getBranch() {
        // TODO: CREATE A JSON OBJECT TO USE
        JSONObject jsonDummyRequestData = new JSONObject("{}");
        RequestHandler requestHandler = new RequestHandler(jsonDummyRequestData);
        // TODO: FILL IN THE EXPECTED BRANCH NAME
        String expectedBranch = "";
        String actualBranch = requestHandler.getBranch();
        assertEquals(expectedBranch, actualBranch);
    }

    /*
     * Tests whether the method in RequestHandler can pick out the correct URL, for
     * the repo which is cloned, from the JSON object.
     * Should return true.
     */
    @Test
    void getClone_url() {
        // TODO: CREATE A JSON OBJECT TO USE
        JSONObject jsonDummyRequestData = new JSONObject();
        RequestHandler requestHandler = new RequestHandler(jsonDummyRequestData);
        // TODO: FILL IN THE EXPECTED BRANCH NAME
        String expectedUrl = "";
        String actualUrl = requestHandler.getClone_url();
        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    void getCommitHash() {
    }

    @Test
    void doBuild() {
    }
}