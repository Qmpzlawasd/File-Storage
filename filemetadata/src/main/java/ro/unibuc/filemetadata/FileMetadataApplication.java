package ro.unibuc.filemetadata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class FileMetadataApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileMetadataApplication.class, args);
    }
}
