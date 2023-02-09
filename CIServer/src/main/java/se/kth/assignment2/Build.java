package se.kth.assignment2;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.RemoteConfig;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Build {
    private String branch;
    private String clone_url;
    private String commitHash;
    private String outputBuild;
    private BuildStatus status;

    public Build(String branch, String clone_url, String commitHash) {
        this.branch = branch;
        this.clone_url = clone_url;
        this.commitHash = commitHash;
    }

    public void build() throws IOException, InterruptedException {
        cloneRepo();
        StatusHandler statusHandler = new StatusHandler(branch, clone_url, commitHash, outputBuild, status);
        statusHandler.sendStatusPending();
        outputBuild = runGradlew();

    private void cloneRepo() {
        System.out.println("cloning the repository...");
        CloneCommand cloneCommand = Git.cloneRepository();
        Git repository = null;
        try {
            repository = cloneCommand.setURI(clone_url).call();
            List<RemoteConfig> remotes = repository.remoteList().call();
            System.out.println("checking out the branch...");
            repository.checkout().setName("origin/" + branch).call();
            for (RemoteConfig remote : remotes) {
                repository.fetch()
                        .setRemote(remote.getName())
                        .setRefSpecs(remote.getFetchRefSpecs())
                        .call();
            }
        } catch (GitAPIException e) {
            e.printStackTrace();
        } finally {
            if (repository != null) {
                repository.close();
            }
        }
        System.out.println("clone and checkout SUCCESS !");
    }

    private String runGradlew() throws IOException, InterruptedException {
        String[] commands = { "/bin/bash", "-c", "./gradlew test build" };
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        String currentWorkingDirectory = System.getProperty("user.dir");
        processBuilder.directory(new File(currentWorkingDirectory));
        Process process = processBuilder.start();

        // Write output to console using bufferreader
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(process.getInputStream()));
        String line;
        System.out.println("BUILD : This is output: ");
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            sb.append(line).append('\n');
        }
        System.out.println("BUILD : This is END");

        int exitCode = process.waitFor();
        StatusHandler statusHandler = new StatusHandler(branch, clone_url, commitHash, outputBuild,
                BuildStatus.SUCCESS);

        if (exitCode == 0) {
            statusHandler.sendStatusCorrect();
            System.out.println("BUILD RESULT : Success!");
            status = BuildStatus.SUCCESS;
        } else {
            System.out.println("BUILD RESULT : Failure!");
            status = BuildStatus.FAILURE;
            statusHandler.sendStatusFailure();
        }

        // Removes the cloned directory so we can clone again without removing it
        // manually
        File directory = new File(currentWorkingDirectory + "/DD2480-Assignment2");
        FileUtils.forceDelete(directory);

        if (directory.exists() && directory.isDirectory()) {
            System.out.println("Directory still exists processing to delete...");
            File currentWorkingStation = new File(currentWorkingDirectory);
            File[] files = currentWorkingStation.listFiles();
            for (File file : files) {
                if (file.getName().equals("DD2480-Assignment2")) {
                    System.out.println(file.getName());
                    FileUtils.forceDelete(file);
                }
            }
            System.out.println("Directory should be deleted...");
        }
        // FileUtils.deleteDirectory(directory);

        return sb.toString();
    }

    public enum BuildStatus {
        SUCCESS("success"),
        FAILURE("failure"),
        ERROR("error");

        private String status;

        BuildStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }
}
