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

public class AddPointDelegate implements JavaDelegate {

    private final static Logger LOGGER = Logger.getLogger("ADD-POINT");

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        LOGGER.info("AddPointDelegate");

        String id = (String) delegateExecution.getVariable("id");
        String event = (String) delegateExecution.getVariable("event");
        String scenario = (String) delegateExecution.getVariable("scenario");

        LOGGER.info("Variables: " + id + ", " + event + ", " + scenario);

        HttpPost post = getPostRequest(id, event, scenario);

        try (
                CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(post)
        ) {
            LOGGER.info("Point response " + EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            LOGGER.severe(e.toString());
        }
    }

    private HttpPost getPostRequest(String id, String event, String scenario) throws UnsupportedEncodingException {
        HttpPost post = new HttpPost("http://host.docker.internal:8070/add");

        String json = "{\n" +
                "                  \"version\": \"7\",\n" +
                "                  \"event\": \"" + event + "\",\n" +
                "                  \"scenario\": \"" + scenario + "\",\n" +
                "                  \"processInstanceId\": \"" + id + "\"\n" +
                "                }";

        StringEntity entity = new StringEntity(json);

        post.setEntity(entity);
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");

        return post;
    }
}
