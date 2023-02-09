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
     * from the created JSON object which is used for testing.
     * Should be equal.
     */
    @Test
    void getBranch() {
        JSONObject json = new JSONObject();
        JSONObject pull_json = new JSONObject();
        JSONObject head_json = new JSONObject();
        JSONObject clone_json = new JSONObject();
        clone_json.put("clone_url", "https://github.com/testuser/testrepo.git");
        head_json.put("ref", "test-branch-#1");
        head_json.put("sha", "abcdef123456789");
        pull_json.put("head", head_json);
        json.put("pull_request", pull_json);
        json.put("repository", clone_json);

        RequestHandler requestHandler = new RequestHandler(json);
        String expectedBranch = "test-branch-#1";
        String actualBranch = requestHandler.getBranch();
        assertEquals(expectedBranch, actualBranch);
    }

    /*
     * Tests whether the method in RequestHandler can pick out the correct URL, for
     * the repo which is cloned, from the created JSON object which is used for
     * testing.
     * Should be equal.
     */
    @Test
    void getClone_url() {
        JSONObject json = new JSONObject();
        JSONObject pull_json = new JSONObject();
        JSONObject head_json = new JSONObject();
        JSONObject clone_json = new JSONObject();
        clone_json.put("clone_url", "https://github.com/testuser/testrepo.git");
        head_json.put("ref", "test-branch-#1");
        head_json.put("sha", "abcdef123456789");
        pull_json.put("head", head_json);
        json.put("pull_request", pull_json);
        json.put("repository", clone_json);

        RequestHandler requestHandler = new RequestHandler(json);
        String expectedUrl = "https://github.com/testuser/testrepo.git";
        String actualUrl = requestHandler.getClone_url();
        assertEquals(expectedUrl, actualUrl);
    }

    /*
     * Tests whether the method in RequestHandler can pick out the correct commit
     * hash from the created JSON object which is used for testing.
     * Should be equal.
     */
    @Test
    void getCommitHash() {
        JSONObject json = new JSONObject();
        JSONObject pull_json = new JSONObject();
        JSONObject head_json = new JSONObject();
        JSONObject clone_json = new JSONObject();
        clone_json.put("clone_url", "https://github.com/testuser/testrepo.git");
        head_json.put("ref", "test-branch-#1");
        head_json.put("sha", "abcdef123456789");
        pull_json.put("head", head_json);
        json.put("pull_request", pull_json);
        json.put("repository", clone_json);

        RequestHandler requestHandler = new RequestHandler(json);
        String expectedHash = "abcdef123456789";
        String actualHash = requestHandler.getCommitHash();
        assertEquals(expectedHash, actualHash);
    }

    @Test
    void doBuild() {
    }
}