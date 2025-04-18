package cfp.wecare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class WecareApplication {

    public static void main(String[] args) {
        SpringApplication.run(WecareApplication.class, args);
    }

}
