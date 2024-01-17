package lv.at20057.stats.influxdb

import com.influxdb.client.InfluxDBClient
import com.influxdb.client.InfluxDBClientFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class InfluxDBConfiguration @Autowired constructor(val influxProperties: InfluxProperties) {

    @Bean
    fun getInfluxDbClient(): InfluxDBClient {
        return InfluxDBClientFactory.create(
            influxProperties.host,
            influxProperties.token.toCharArray(),
            influxProperties.org,
            influxProperties.bucket
        )
    }

}