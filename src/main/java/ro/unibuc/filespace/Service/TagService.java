package ro.unibuc.filespace.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.unibuc.filespace.Exception.FileDoesNotExist;
import ro.unibuc.filespace.Exception.GroupDoesNotExist;
import ro.unibuc.filespace.Exception.UserNotInGroup;
import ro.unibuc.filespace.Model.File;
import ro.unibuc.filespace.Model.Group;
import ro.unibuc.filespace.Model.GroupTag;
import ro.unibuc.filespace.Model.Tag;
import ro.unibuc.filespace.Repository.GroupTagRepository;
import ro.unibuc.filespace.Repository.TagRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {
    private final TagRepository tagRepository;
    private final GroupTagRepository groupTagRepository;
    private final FileService fileService;
    private final GroupService groupService;
    private final UserService userService;


    public Tag addTag(long groupId, long fileId, String tagName) throws FileDoesNotExist, UserNotInGroup {
        File file = fileService.getFileFromGroupById(groupId, fileId).orElseThrow(FileDoesNotExist::new);
        Group group = groupService.getGroup(groupId).orElseThrow(GroupDoesNotExist::new);
        return tagRepository.save(new Tag(tagName, new GroupTag(file, group)));
    }

    public List<Tag> getFileTags(long groupId, long fileId) throws UserNotInGroup, FileDoesNotExist {
        fileService.getFileFromGroupById(groupId, fileId).orElseThrow(FileDoesNotExist::new);
        return groupTagRepository.getFileTags(groupId, fileId);
    }

    public List<Tag> getGroupTags(long groupId) throws UserNotInGroup {
        if (!groupService.getGroupUsers(groupId).contains(userService.getAuthenticatedUser())) {
            throw new UserNotInGroup();
        }
        return groupTagRepository.getTagsInGroup(groupId);
    }
}
