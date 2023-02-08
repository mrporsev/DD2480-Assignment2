package se.kth.assignment2;

import org.json.JSONObject;

import java.io.IOException;

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

    private boolean isPullReq(JSONObject jsonObject) {
        return jsonObject.has("pull_request");
    }

    private String extractBranch(JSONObject jsonObject) {
        return isPullReq(jsonObject) ? jsonObject.getJSONObject("pull_request").getJSONObject("head").getString("ref") : jsonObject.getString("ref").replace("refs/heads/", "");

    }

    private boolean proccess(JSONObject jsonObject) {
        if (!isPullReq(jsonObject)) return true;
        else {
            String action = jsonObject.getString("action");
            return  action.equals("opened") || action.equals("reopened") || action.equals("converted_to_draft");
        }
    }

    public String getBranch() {
        return branch;
    }

    public String getClone_url() {
        return clone_url;
    }

    public String getCommitHash() {
        return commitHash;
    }

    private String extractUrl(JSONObject jsonObject) {
        return jsonObject.getJSONObject("repository").getString("clone_url");
    }

    private String extractCommitHash(JSONObject jsonObject) {
        return isPullReq(jsonObject) ? jsonObject.getJSONObject("pull_request").getJSONObject("head").getString("sha") : jsonObject.getString("after");
    }

    public void doBuild(){
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
