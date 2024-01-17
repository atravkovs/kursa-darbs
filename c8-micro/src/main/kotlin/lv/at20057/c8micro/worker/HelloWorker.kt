package lv.at20057.c8micro.worker

import io.camunda.zeebe.client.api.response.ActivatedJob
import io.camunda.zeebe.spring.client.annotation.JobWorker
import io.camunda.zeebe.spring.client.annotation.Variable
import lv.at20057.c8micro.models.MandelbrotDto
import lv.at20057.c8micro.services.MandelbrotService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.UUID

@Component
class HelloWorker @Autowired constructor(
    val mandelbrotService: MandelbrotService
) {

    val logger: Logger = LoggerFactory.getLogger(HelloWorker::class.java);

    @JobWorker
    fun sayHi(job: ActivatedJob): Map<String, Any> {
        return mapOf("hello" to "world");
    }

    @JobWorker
    fun generateId(): Map<String, Any> {
        val uuid = UUID.randomUUID();

        return mapOf("id" to uuid.toString());
    }

    @JobWorker(fetchVariables = ["id", "event", "scenario"])
    fun addPoint(
        @Variable id: String,
        @Variable event: String,
        @Variable scenario: String
    ): Map<String, Any> {
        val client = HttpClient.newHttpClient();

        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://host.docker.internal:8070/add"))
            .header("Content-Type", "application/json")
            .POST(
                HttpRequest.BodyPublishers.ofString(
                    """{
                  "version": "8",
                  "event": "$event",
                  "scenario": "$scenario",
                  "processInstanceId": "$id"
                }""".trimIndent()
                )
            )
            .build();

        val response = client.send(request, HttpResponse.BodyHandlers.ofString());

        logger.info("Point response {}", response.body());

        return mapOf();
    }

    @JobWorker(fetchVariables = ["imageSize", "maxIterations"])
    fun mandelbrot(@Variable imageSize: Int, @Variable maxIterations: Int): Map<String, Any> {
        val config = MandelbrotDto(
            xc = -0.5,
            yc = 0.0,
            size = 2.0,
            imageSize,
            maxIterations
        );

        logger.info("[START] Mandelbrot calculation");

        mandelbrotService.mandelbrot(config);

        logger.info("[END] Mandelbrot calculation");

        return mapOf("mandelbrot" to "done");
    }

}