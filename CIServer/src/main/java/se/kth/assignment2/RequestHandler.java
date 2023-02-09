package se.kth.assignment2;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Handles the POST request
 * Used to extract necessary data from the payload and feed the build.
 */
public class RequestHandler {
    private JSONObject jsonObject;
    private String branch;
    private String clone_url;
    private String commitHash;

    public RequestHandler(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        this.branch = extractBranch(jsonObject);
        this.clone_url = extractUrl(jsonObject);
        this.commitHash = extractCommitHash(jsonObject);
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    /**
     * @param jsonObject
     * @return true if the payload is from a pull request, returns false if it is a push.
     */
    private boolean isPullReq(JSONObject jsonObject) {
        return jsonObject.has("pull_request");
    }

    /**
     * Extract the branch name to checkout
     * @param jsonObject
     * @return the branch name
     */
    private String extractBranch(JSONObject jsonObject) {
        return isPullReq(jsonObject) ? jsonObject.getJSONObject("pull_request").getJSONObject("head").getString("ref") : jsonObject.getString("ref").replace("refs/heads/", "");

    }

    /**
     * @param jsonObject
     * @return true if the payload is from a push action, or a pull_request. Returns false otherwise (for example:
     * if the payload comes from a comment in the pull request)
     */
    private boolean proccess(JSONObject jsonObject) {
        if (!isPullReq(jsonObject)) return true;
        else {
            String action = jsonObject.getString("action");
            return  action.equals("opened") || action.equals("reopened") || action.equals("converted_to_draft") || action.equals("ready_for_review");
        }
    }

    /**
     * @return branch name
     */
    public String getBranch() {
        return branch;
    }

    /**
     * @return clone_url
     */
    public String getClone_url() {
        return clone_url;
    }

    /**
     * @return commitHash
     */
    public String getCommitHash() {
        return commitHash;
    }

    /**
     * Extracts the url for cloning the repo
     * @param jsonObject
     * @return the string for clone url
     */
    private String extractUrl(JSONObject jsonObject) {
        return jsonObject.getJSONObject("repository").getString("clone_url");
    }

    /**
     * Extracts the commit hash
     * @param jsonObject
     * @return the string for the commit hash
     */
    private String extractCommitHash(JSONObject jsonObject) {
        return isPullReq(jsonObject) ? jsonObject.getJSONObject("pull_request").getJSONObject("head").getString("sha") : jsonObject.getString("after");
    }

    /**
     * Feed the build
     */
    public void doBuild(){
        //We create a new build only if the payload is push or a pull request.
        //Otherwise we ignore.
        if(proccess(jsonObject)) {
            Build build = new Build(branch, clone_url, commitHash);
            try {
                build.build();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
