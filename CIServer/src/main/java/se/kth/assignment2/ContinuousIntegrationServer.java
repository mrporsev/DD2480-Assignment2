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
 CI server that checks if pull_requests and commits are upp to par with Group28's set of requirements
 */
public class ContinuousIntegrationServer extends AbstractHandler
{
    /**
     *
     * @param target The target of the request - either a URI or a name.
     * @param baseRequest The original unwrapped request object.
     * @param request The request either as the {@link Request} object or a wrapper of that request. The
     * <code>{@link HttpConnection#getCurrentConnection()}.{@link HttpConnection#getHttpChannel() getHttpChannel()}.{@link HttpChannel#getRequest() getRequest()}</code>
     * method can be used access the Request object if required.
     * @param response The response as the {@link Response} object or a wrapper of that request. The
     * <code>{@link HttpConnection#getCurrentConnection()}.{@link HttpConnection#getHttpChannel() getHttpChannel()}.{@link HttpChannel#getResponse() getResponse()}</code>
     * method can be used access the Response object if required.
     * @throws IOException
     * @throws ServletException
     */

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
        JSONObject jsonObject = new JSONObject(sb.toString());
        RequestHandler requestHandler = new RequestHandler(jsonObject);
        requestHandler.doBuild();


        response.getWriter().println("CI job done");
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
