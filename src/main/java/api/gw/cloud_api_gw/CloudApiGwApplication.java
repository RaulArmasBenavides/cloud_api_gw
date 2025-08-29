package api.gw.cloud_api_gw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.PropertySource;
@SpringBootApplication
@PropertySource("classpath:env.properties")
@EnableDiscoveryClient
public class CloudApiGwApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudApiGwApplication.class, args);
	}

}
