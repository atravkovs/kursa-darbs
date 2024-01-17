package lv.at20057.c8micro.services

import io.camunda.zeebe.client.ZeebeClient
import lv.at20057.c8micro.models.StartProcessDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CamundaService @Autowired constructor(val client: ZeebeClient) {

    fun startProcess(processDefinitionId: String, startProcessDto: StartProcessDto) {
        client.newCreateInstanceCommand()
            .bpmnProcessId(processDefinitionId)
            .latestVersion()
            .variables(startProcessDto.variables)
            .send();
    }

}