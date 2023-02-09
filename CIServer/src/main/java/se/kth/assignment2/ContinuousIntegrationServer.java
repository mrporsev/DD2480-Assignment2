package se.kth.assignment2;

import org.eclipse.jgit.api.CloneCommand;
import org.json.JSONObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response)
            throws IOException, ServletException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        if(request.getMethod().equals("GET")) {
            if (target.equals("/builds")) {
                File dir = new File("builds");
                if (dir.exists() && dir.isDirectory()) {
                    response.getWriter().println("<html><body>");
                    File[] files = dir.listFiles();
                    for (File file : files) {
                        response.getWriter().println("<a href='" + file.getName() + "'>" + file.getName() + "</a><br>");
                    }
                    response.getWriter().println("</body></html>");
                } else {
                    response.getWriter().println("Builds folder not found");
                }
            } else {
                try {
                    File file = new File("builds/" + target);
                    FileReader fileReader = new FileReader(file);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    String line;
                    StringBuilder content = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    fileReader.close();
                    response.getWriter().println("<p>" + content.toString() + "</p>");
                } catch (IOException e) {
                    e.printStackTrace();
                }   
            }
            
        } else {

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
        JSONObject jsonObject = new JSONObject(sb.toString());
        RequestHandler requestHandler = new RequestHandler(jsonObject);
        requestHandler.doBuild();


        response.getWriter().println("CI job done");
        }
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
