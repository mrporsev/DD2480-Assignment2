package se.kth.assignment2;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ContinuousIntegrationServerTest {

    @Test
    void correctBranch() {

    }

    @Test
    void icorrectBranch() {

    }

    @Test
    void correctRepositoryURL() {

    }

    @Test
    void icorrectRepositoryURL() {

    }

    @Test
    void correctRepositoryName() {

    }

    @Test
    void icorrectRepositoryName() {

    }

    @Test
    void correctCommitHash() {

    }

    @Test
    void icorrectCommitHash() {

    }

    @Test
    void correctCloneURL() {

    }

    @Test
    void icorrectCloneURL() {

    }

    @Test
    void handlerValidTypeBranch() {
        // Make different json Objects to try?
        RequestHandler requestHandler = new RequestHandler(null);
        var testBranch = requestHandler.getBranch();
        boolean testBranchBoolean = false;
        if (testBranch instanceof String) {
            testBranchBoolean = true;
        }
        assertEquals(testBranchBoolean, true);
    }

    @Test
    void handlerInvalidTypeBranch() {
        // Make different json Objects to try?
        RequestHandler requestHandler = new RequestHandler(null);
        var testBranch = requestHandler.getBranch();
        boolean testBranchBoolean = false;
        if (testBranch instanceof String) {
            testBranchBoolean = true;
        }
        assertEquals(testBranchBoolean, false);
    }

    @Test
    void statushandlerValidInput() {

    }

    @Test
    void statushandlerIvalidInput() {

    }

    @Test
    void validJasonObject() {

    }
}