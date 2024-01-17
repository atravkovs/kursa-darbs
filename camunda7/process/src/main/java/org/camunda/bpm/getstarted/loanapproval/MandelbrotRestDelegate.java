package org.camunda.bpm.getstarted.loanapproval;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

public class MandelbrotRestDelegate implements JavaDelegate {

    private final static Logger LOGGER = Logger.getLogger("MANDELBROT-REST");

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        LOGGER.info("Start MandelbrotRestDelegate");

        int n = Math.toIntExact((Long) delegateExecution.getVariable("imageSize"));
        int max = Math.toIntExact((Long) delegateExecution.getVariable("maxIterations"));

        LOGGER.info("Variables: " + n + ", " + max);

        HttpPost post = getPostRequest(n, max);

        try (
                CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(post)
        ) {
            LOGGER.info("Mandelbrot response " + EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            LOGGER.severe(e.toString());
        }
    }

    private HttpPost getPostRequest(int imageSize, int maxIterations) throws UnsupportedEncodingException {
        HttpPost post = new HttpPost("http://host.docker.internal:8080/mandelbrot");

        String json = "{\n" +
                "                  \"xc\": -0.5,\n" +
                "                  \"yc\": 0,\n" +
                "                  \"size\": 2,\n" +
                "                  \"imageSize\": \"" + imageSize + "\",\n" +
                "                  \"maxIterations\": \"" + maxIterations + "\"\n" +
                "                }";

        StringEntity entity = new StringEntity(json);

        post.setEntity(entity);
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");

        return post;
    }
}
