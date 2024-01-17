package lv.at20057.stats.influxdb

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "influxdb")
data class InfluxProperties(
    val host: String,
    val token: String,
    val org: String,
    val bucket: String
)
