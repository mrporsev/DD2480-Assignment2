package se.kth.assignment2;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.RemoteConfig;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;


/**
 * Contains information needed for the build and methods for building
 */
public class Build {
    private String branch;
    private String clone_url;
    private String commitHash;
    private String outputBuild;
    private BuildStatus status;

    /**
     * Constructs with properties about what (branch) to clone and build
     * @param branch branch name
     * @param clone_url url to clone the repo
     * @param commitHash commit hash
     */
    public Build(String branch, String clone_url, String commitHash) {
        this.branch = branch;
        this.clone_url = clone_url;
        this.commitHash = commitHash;
    }

    /*
     * Stores the build
     * @param outputBuild the output of the build
     */
    public void storeBuild(String outputBuild) {
        try {
            String currentWorkingDirectory = System.getProperty("user.dir");
            File newFile = new File(currentWorkingDirectory + "/builds/" + commitHash + ".txt");
            FileWriter fileWriter = new FileWriter(newFile); 
            Date date = new Date();
            fileWriter.write(date.toString() + "\n" + outputBuild);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Runs the build
     * @throws IOException
     * @throws InterruptedException
     */
    public void build() throws IOException, InterruptedException {
        cloneRepo();
        StatusHandler statusHandler = new StatusHandler(branch, clone_url, commitHash, outputBuild, status);
        statusHandler.sendStatusPending();
        outputBuild = runGradlew();
        storeBuild(outputBuild);
    }

    /**
     * Clones the specified repository branch into a local folder to prepare testing
     */
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


    /**
     * Runs the gradle build process (including tests), and sets the BuildStatus
     * @return the output of the build
     * @throws IOException
     * @throws InterruptedException
     */
    private String runGradlew() throws IOException, InterruptedException {
        String[] commands = { "/bin/bash", "-c", "./gradlew build test" };
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        String currentWorkingDirectory = System.getProperty("user.dir");
        processBuilder.directory(new File(currentWorkingDirectory + "/DD2480-Assignment2/CIServer"));
        System.out.println("The DIRECTORY OF BUILD: " + processBuilder.directory().toString());
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
        //FileUtils.deleteDirectory(directory);

        return sb.toString();
    }

    /**
     * Contains possible states that a (completed) build can have
     *
     */
    public enum BuildStatus {
        /**
         * The build procsses completed successfully
         */
        SUCCESS("success"),

        /**
         *The build process completed without success
         */
        FAILURE("failure"),

        /**
         *The build process did not complete due to an error
         */
        ERROR("error");

        private String status;

        BuildStatus(String status) {
            this.status = status;
        }

        /**
         * Converts a BuildStatus into a string that can be easily stored or transferred
         * @return String corresponding to this BuildStatus
         */
        public String getStatus() {
            return status;
        }
    }
}
