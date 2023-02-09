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
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Contains information about the build and its outcome in order to send status to github
 */
public class StatusHandler {
    private String branch;
    private String clone_url;
    private String commitHash;
    private String outputBuild;
    private String ACCESS_TOKEN = new Secrets().ACCESS_TOKEN;
    private String BASE_URL = "https://api.github.com/repos/";
    private String REPO_OWNER ="mrporsev";
    private String repo_name = "DD2480-Assignment2";
    private Build.BuildStatus status;

    /**
     *
     * @param branch Branch name
     * @param clone_url url of the repo
     * @param commitHash commitHash
     * @param outputBuild output of the build
     * @param status SUCCESS or FAILURE or ERROR
     */
    public StatusHandler(String branch, String clone_url, String commitHash, String outputBuild, Build.BuildStatus status) {
        this.branch = branch;
        this.clone_url = clone_url;
        this.commitHash = commitHash;
        this.outputBuild = outputBuild;
        this.status = status;
    }

    /**
     * Reports the build result as a status on GitHub when its a SUCCESS
     * @throws ClientProtocolException
     * @throws IOException
     */
    public void sendStatusCorrect() throws ClientProtocolException, IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(BASE_URL + REPO_OWNER + "/" + repo_name + "/statuses/" + commitHash);
        System.out.println("httpPost link: " + httpPost.toString());
        httpPost.addHeader("Accept", "application/vnd.github+json");
        httpPost.addHeader("Authorization", "Bearer " + ACCESS_TOKEN);
        httpPost.addHeader("X-Github-Api-Version", "2022-11-28");
        
        //Create request body
        String state = "success";
        String target_url = "https://example.com/build/status";
        String description = "The build has completed successfully";
        String context = "continuous-integration/jenkins";

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode requestBody = mapper.createObjectNode();
        requestBody.put("state", state);
        requestBody.put("target_url", target_url);
        requestBody.put("description", description);
        requestBody.put("context", context);
        String requestBodyStr = mapper.writeValueAsString(requestBody);

        
        
        //JSONObject jsonObject = new JSONObject(requestBodyBuilder.toString());
        StringEntity requestBody1 = new StringEntity(requestBodyStr, ContentType.APPLICATION_JSON);
        
        httpPost.setEntity(requestBody1);

        CloseableHttpResponse response = httpClient.execute(httpPost);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 201) {
            System.out.println("Failed to create commit status: " + statusCode);
        } else {
            System.out.println("Successfully created commit status (SUCCESS)");
        }



        response.close();
    }

    /**
     * Reports the build result as a status on GitHub when its a FAILURE
     * @throws ClientProtocolException
     * @throws IOException
     */
    public void sendStatusFailure() throws ClientProtocolException, IOException {

        //System.out.println("OUTPRINT WITHIN FUNCTION: " + repo_name + " " + commitHash);

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(BASE_URL + REPO_OWNER + "/" + repo_name + "/statuses/" + commitHash);
        System.out.println("httpPost link: " + httpPost.toString());
        httpPost.addHeader("Accept", "application/vnd.github+json");
        httpPost.addHeader("Authorization", "Bearer " + ACCESS_TOKEN);
        httpPost.addHeader("X-Github-Api-Version", "2022-11-28");
        
        //Create request body
        String state = "failure";
        String target_url = "https://example.com/build/status";
        String description = "The build has completed successfully";
        String context = "continuous-integration/jenkins";

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode requestBody = mapper.createObjectNode();
        requestBody.put("state", state);
        requestBody.put("target_url", target_url);
        requestBody.put("description", description);
        requestBody.put("context", context);
        String requestBodyStr = mapper.writeValueAsString(requestBody);

        
        
        //JSONObject jsonObject = new JSONObject(requestBodyBuilder.toString());
        StringEntity requestBody1 = new StringEntity(requestBodyStr, ContentType.APPLICATION_JSON);
        
        httpPost.setEntity(requestBody1);

        CloseableHttpResponse response = httpClient.execute(httpPost);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 201) {
            System.out.println("Failed to create commit status: " + statusCode);
        } else {
            System.out.println("Successfully created commit status (FAILURE)");
        }



        response.close();
    }

    /**
     * Reports the build result as a status on GitHub when its still pending
     * @throws ClientProtocolException
     * @throws IOException
     */
    public void sendStatusPending() throws ClientProtocolException, IOException {

        //System.out.println("OUTPRINT WITHIN FUNCTION: " + repo_name + " " + commitHash);

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(BASE_URL + REPO_OWNER + "/" + repo_name + "/statuses/" + commitHash);
        System.out.println("httpPost link: " + httpPost.toString());
        httpPost.addHeader("Accept", "application/vnd.github+json");
        httpPost.addHeader("Authorization", "Bearer " + ACCESS_TOKEN);
        httpPost.addHeader("X-Github-Api-Version", "2022-11-28");
        
        //Create request body
        String state = "pending";
        String target_url = "https://example.com/build/status";
        String description = "The build has completed successfully";
        String context = "continuous-integration/jenkins";

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode requestBody = mapper.createObjectNode();
        requestBody.put("state", state);
        requestBody.put("target_url", target_url);
        requestBody.put("description", description);
        requestBody.put("context", context);
        String requestBodyStr = mapper.writeValueAsString(requestBody);

        
        
        //JSONObject jsonObject = new JSONObject(requestBodyBuilder.toString());
        StringEntity requestBody1 = new StringEntity(requestBodyStr, ContentType.APPLICATION_JSON);
        
        httpPost.setEntity(requestBody1);

        CloseableHttpResponse response = httpClient.execute(httpPost);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 201) {
            System.out.println("Failed to create commit status: " + statusCode);
        } else {
            System.out.println("Successfully created commit status (PENDING)");
        }



        response.close();
    }
}
