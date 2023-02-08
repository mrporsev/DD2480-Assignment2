package se.kth.assignment2;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.RemoteConfig;

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
    }

    private void cloneRepo() {
        System.out.println("cloning the repository...");
        CloneCommand cloneCommand = Git.cloneRepository();
        try {
            Git repository = cloneCommand.setURI(clone_url).call();
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
        StatusHandler statusHandler = new StatusHandler(branch, clone_url, commitHash, outputBuild, BuildStatus.SUCCESS);

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
        if (directory.exists()) {
            deleteDirectory(directory);
            System.out.println("Directory deleted.");
        } else {
            System.out.println("Directory not found.");
        }

        return sb.toString();
    }

    /*
     * Method for removing the recently cloned directory in order to be able to
     * clone another one during next pull request
     */
    private static void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
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
