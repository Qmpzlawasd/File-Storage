package ro.unibuc.filespace.Runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Seeder implements ApplicationRunner {
    private final SeederHelper seederHelper;

    @Override
    public void run(ApplicationArguments args) {
        seederHelper.createUser("user1", "string");
        seederHelper.createUser("user2", "string");
        seederHelper.createUser("user3", "string");
    }
}
