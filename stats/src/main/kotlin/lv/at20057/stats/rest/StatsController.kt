package lv.at20057.stats.rest

import com.influxdb.client.WriteApi
import com.influxdb.client.domain.WritePrecision
import com.influxdb.client.write.Point
import lv.at20057.stats.models.StartPoint
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
class StatsController @Autowired constructor(val writeApi: WriteApi) {


    @PostMapping("/add")
    fun addPoint(@RequestBody data: StartPoint): StartPoint {
        val point = Point.measurement("time")
            .addTag("camunda", data.version)
            .addTag("scenario", data.scenario)
            .addTag("processInstanceId", data.processInstanceId)
            .addField("event", data.event)
            .time(Instant.now().toEpochMilli(), WritePrecision.MS);

        writeApi.writePoint(point);

        return data;
    }

}