package ro.unibuc.filespace.Runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.sqlite.FileException;
import ro.unibuc.filespace.Exception.*;
import ro.unibuc.filespace.Model.File;
import ro.unibuc.filespace.Model.Group;
import ro.unibuc.filespace.Model.User;

import java.io.IOException;
import java.util.Collections;


@RequiredArgsConstructor
@Component
@Profile("!test")
public class Seeder implements ApplicationRunner {
    private final SeederHelper seederHelper;
    ApplicationContext context;

    @Override
    public void run(ApplicationArguments args) throws FileException, IOException, UserNotInGroup, FileWithNameAlreadyExists, FileIsEmpty, FileDoesNotExist, CommentIsEmpty, CommentDoesNotExist, InterruptedException {
        User user1 = seederHelper.createUser("string", "string");
        User user2 = seederHelper.createUser("user2", "string");
        User user3 = seederHelper.createUser("user3", "string");

        Group group1 = seederHelper.createGroup("group1", user1);

//        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user1, null, Collections.emptyList());
//        SecurityContextHolder.getContext().setAuthentication(auth);
//
//        File file1 = seederHelper.createFile(group1, seederHelper.createMockMultipartFile("asd", "asd".getBytes()));
//        seederHelper.storeFileMetadata(file1);

        ////////// test
//        seederHelper.add10Groups();
        ////////// test

        SecurityContextHolder.clearContext();
//        int exitCode = SpringApplication.exit(context, () -> 0);
//        System.exit(exitCode);
    }
}
