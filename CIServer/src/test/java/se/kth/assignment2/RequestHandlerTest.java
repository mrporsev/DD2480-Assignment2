package se.kth.assignment2;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;

//test class
class RequestHandlerTest {

    private JSONObject json;
    private JSONObject pull_json;
    private JSONObject head_json;
    private JSONObject repo_json;
    private JSONObject clone_json;




    RequestHandler rh;
    @BeforeEach
    void setUp() {
        json = new JSONObject();
        pull_json = new JSONObject();
        head_json = new JSONObject();
        clone_json = new JSONObject();

        clone_json.put("clone_url","my_url");
        head_json.put("ref","my_ref");
        head_json.put("sha","my_sha");
        pull_json.put("head",head_json);
        json.put("pull_request",pull_json);
        json.put("repository",clone_json);

        rh = new RequestHandler(json);

    }

    @AfterEach
    void tearDown() {
    }

    /**
     * test if functions gets a json object
     */
    @Test
    void getJsonObject() {

        assertTrue(rh.getJsonObject() == json);
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