package lv.at20057.stats.influxdb

import com.influxdb.client.AuthorizationsApi
import com.influxdb.client.BucketsApi
import com.influxdb.client.InfluxDBClient
import com.influxdb.client.OrganizationsApi
import com.influxdb.client.QueryApi
import com.influxdb.client.WriteApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class InfluxDbApiConfiguration @Autowired constructor(val influxDBClient: InfluxDBClient) {

    @Bean
    fun getOrganizationsApi(): OrganizationsApi {
        return influxDBClient.organizationsApi;
    }

    @Bean
    fun getBucketApi(): BucketsApi {
        return influxDBClient.bucketsApi;
    }

    @Bean
    fun getAuthorizationsApi(): AuthorizationsApi {
        return influxDBClient.authorizationsApi;
    }

    @Bean
    fun getQueryApi(): QueryApi {
        return influxDBClient.queryApi;
    }

    @Bean
    fun getWriteApi(): WriteApi {
        return influxDBClient.makeWriteApi();
    }

}