package se.kth.assignment2;

public class StatusHandler {
    private String branch;
    private String clone_url;
    private String commitHash;
    private String outputBuild;
    private Build.BuildStatus status;

    public StatusHandler(String branch, String clone_url, String commitHash, String outputBuild, Build.BuildStatus status) {
        this.branch = branch;
        this.clone_url = clone_url;
        this.commitHash = commitHash;
        this.outputBuild = outputBuild;
        this.status = status;
    }

    public void sendStatus() {
    }
}
