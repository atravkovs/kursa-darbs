package lv.at20057.c8micro

import io.camunda.zeebe.spring.client.annotation.Deployment
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@Deployment(resources = ["classpath:test.bpmn"])
class C8MicroApplication

fun main(args: Array<String>) {
    runApplication<C8MicroApplication>(*args)
}
