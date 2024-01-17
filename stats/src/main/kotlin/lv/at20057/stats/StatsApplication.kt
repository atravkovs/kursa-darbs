package lv.at20057.stats

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class StatsApplication

fun main(args: Array<String>) {
    runApplication<StatsApplication>(*args)
}
