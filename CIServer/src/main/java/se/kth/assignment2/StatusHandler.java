package se.kth.assignment2;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class StatusHandler {
    private String branch;
    private String clone_url;
    private String commitHash;
    private String outputBuild;
    private String ACCESS_TOKEN = "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";
    private String BASE_URL = "https://api.github.com/repos/";
    private String REPO_OWNER ="mrporsev";
    private String repo_name = "DD2480-Assignment2";
    private Build.BuildStatus status;

    public StatusHandler(String branch, String clone_url, String commitHash, String outputBuild, Build.BuildStatus status) {
        this.branch = branch;
        this.clone_url = clone_url;
        this.commitHash = commitHash;
        this.outputBuild = outputBuild;
        this.status = status;
    }

    public void sendStatus() throws ClientProtocolException, IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(BASE_URL + REPO_OWNER + "/" + repo_name + "commits/" + commitHash + "/status");
        httpPost.addHeader("Accept", "application/vnd.github+json");
        httpPost.addHeader("Authorization", "Bearer" + ACCESS_TOKEN);
        httpPost.addHeader("X-Github-Api-Version", "2022-11-28");
        
        //Create request body
        String state = "success";
        String target_url = "google.se";
        String description = "The build has completed successfully";
        String context = "continuous-integration/jenkins";

        StringBuilder requestBodyBuilder = new StringBuilder();
        requestBodyBuilder.append("'{");
        requestBodyBuilder.append('"');
        requestBodyBuilder.append("state");
        requestBodyBuilder.append('"');
        requestBodyBuilder.append(":");
        requestBodyBuilder.append('"');
        requestBodyBuilder.append(state);
        requestBodyBuilder.append('"');
        requestBodyBuilder.append(',');
        requestBodyBuilder.append('"');
        requestBodyBuilder.append("target_url");
        requestBodyBuilder.append('"');
        requestBodyBuilder.append(":");
        requestBodyBuilder.append('"');
        requestBodyBuilder.append(target_url);
        requestBodyBuilder.append('"');
        requestBodyBuilder.append(',');
        requestBodyBuilder.append('"');
        requestBodyBuilder.append("description");
        requestBodyBuilder.append('"');
        requestBodyBuilder.append(":");
        requestBodyBuilder.append('"');
        requestBodyBuilder.append(description);
        requestBodyBuilder.append('"');
        requestBodyBuilder.append(',');
        requestBodyBuilder.append('"');
        requestBodyBuilder.append("context");
        requestBodyBuilder.append('"');
        requestBodyBuilder.append(":");
        requestBodyBuilder.append('"');
        requestBodyBuilder.append(context);
        requestBodyBuilder.append('"');
        requestBodyBuilder.append('}');
        

        StringEntity requestBody = new StringEntity(requestBodyBuilder.toString(), ContentType.APPLICATION_JSON);
        
        httpPost.setEntity(requestBody);

        CloseableHttpResponse response = httpClient.execute(httpPost);




        response.close();
    }
}
