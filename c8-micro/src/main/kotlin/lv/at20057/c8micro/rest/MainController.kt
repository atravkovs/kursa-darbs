package lv.at20057.c8micro.rest

import lv.at20057.c8micro.models.MandelbrotDto
import lv.at20057.c8micro.models.StartProcessDto
import lv.at20057.c8micro.services.CamundaService
import lv.at20057.c8micro.services.MandelbrotService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController @Autowired constructor(
    val mandelbrotService: MandelbrotService,
    val camundaService: CamundaService
) {

    @GetMapping("/")
    fun test(): String {
        return "Hello World!";
    }

    @PostMapping("/start/{processDefinitionId}")
    fun start(@PathVariable processDefinitionId: String, @RequestBody startProcessDto: StartProcessDto) {
        camundaService.startProcess(processDefinitionId, startProcessDto);
    }

    @PostMapping("/mandelbrot")
    fun mandelbrot(@RequestBody body: MandelbrotDto): String {
        mandelbrotService.mandelbrot(body)

        return "OK";
    }

}