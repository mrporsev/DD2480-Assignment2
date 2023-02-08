package se.kth.assignment2;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void getBranch() {
    }

    @Test
    void getClone_url() {
    }

    @Test
    void getCommitHash() {
    }

    @Test
    void doBuild() {
    }
}