package se.kth.assignment2;

import org.eclipse.jgit.api.CloneCommand;
import org.json.JSONObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

import netscape.javascript.JSObject;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.util.FileUtils;

/**
 Skeleton of a ContinuousIntegrationServer which acts as webhook
 See the Jetty documentation for API documentation of those classes.
 */
public class ContinuousIntegrationServer extends AbstractHandler
{
    private String repositoryUrl;
    private String branch;
    private String commitHash;

    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response)
            throws IOException, ServletException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        System.out.println(target);

        // here you do all the continuous integration tasks
        // for example
        // 1st clone your repository
        // 2nd compile the code

        StringBuilder sb = new StringBuilder();
        
        BufferedReader reader = request.getReader();
        String line;
        System.out.println("The received payload is:");
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            sb.append(line).append('\n');
        }
        System.out.println("END OF PAYLOAD");
        
        System.out.println("HEEEEEEEEEEEEEEEEEEEEEEEEEEEEEELLLLOOO");
        //String payload = request.getReader().lines().collect(Collectors.joining());
        JSONObject jsonObject = new JSONObject(sb.toString());


        //Get repository URL and branch from HTTP payload
        //System.out.println(request.getParameterNames());
        /*
        repositoryUrl = request.getParameter("svn_url");
        branch = request.getParameter("ref"); //branch name
        commitHash = request.getParameter("sha"); //commit hash , used to checkout the branch
        */


        repositoryUrl = getRepositoryUrl(jsonObject);
        System.out.println("Repository URL: " + repositoryUrl);
        branch = getBranch(jsonObject); //branch name
        System.out.println("Branch: " + branch);
        commitHash = getCommitHash(jsonObject); //commit hash , used to checkout the branch

        
        
        System.out.println("Commit hash: " + commitHash);

        CloneCommand cloneCommand = Git.cloneRepository();
        cloneCommand.setURI(repositoryUrl);
        cloneCommand.setDirectory(new File("Repository"));
        cloneCommand.setBranch(branch);
        try {
            cloneCommand.call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        
        /*
        //Clone repository
        try(Git repository = Git.cloneRepository()
        .setURI(repositoryUrl)
        .setDirectory(new File("Repository"))
        .call()) {
            System.out.println("After clone");
            List<RemoteConfig> remotes = repository.remoteList().call();
            for (RemoteConfig remote : remotes) {
                repository.fetch()
                    .setRemote(remote.getName())
                    .setRefSpecs(remote.getFetchRefSpecs())
                    .call();
        }
            System.out.println("Utanför fetch");
            repository.checkout().setName(branch).call();



            runGradlew();
        } catch (GitAPIException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

         */

        response.getWriter().println("CI job done");
    }

    public static void runGradlew() throws IOException, InterruptedException {
        String[] commands = {"/bin/bash", "-c", "./gradlew test build"};
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.directory(new File("/home/p/o/porsev/Documents/SWE/DD2480-Assignment2/CIServer/Repository"));
        Process process = processBuilder.start();

        //Write output to console using bufferreader
        BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(process.getInputStream()));
        String line;
        System.out.println("This is output: ");
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        System.out.println("This is END");

        int exitCode = process.waitFor();
        
        if (exitCode == 0) {
            System.out.println("Success!");
        } else {
            System.out.println("Failure!");
        }
    }

    private String getBranch(JSONObject jsonObject) {
        return jsonObject.getJSONObject("pull_request").getJSONObject("head").getString("ref");

    }

    private String getRepositoryUrl(JSONObject jsonObject) {
        return jsonObject.getJSONObject("repository").getString("clone_url");
    }

    private String getCommitHash(JSONObject jsonObject) {
        return jsonObject.getJSONObject("pull_request").getJSONObject("head").getString("sha");
    }

    // used to start the CI server in command line ,
    public static void main(String[] args) throws Exception
    {
        //runGradlew();
        Server server = new Server(8028);
        server.setHandler(new ContinuousIntegrationServer());
        server.start();
        server.join();
    }
}
