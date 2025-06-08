package ro.unibuc.filespace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FileSpaceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileSpaceApplication.class, args);
    }
}
