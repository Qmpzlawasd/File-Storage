package ro.unibuc.filespace.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ro.unibuc.filespace.Service.GroupService;

@RequiredArgsConstructor
@RestController
public class GroupController {
    private final GroupService groupService;
}
