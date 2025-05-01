package ro.unibuc.filespace.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ro.unibuc.filespace.Exception.FileDoesNotExist;
import ro.unibuc.filespace.Exception.UserNotInGroup;
import ro.unibuc.filespace.Model.File;
import ro.unibuc.filespace.Model.Tag;
import ro.unibuc.filespace.Repository.FileRepository;
import ro.unibuc.filespace.Repository.TagRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {
    private final TagRepository tagRepository;
    private final FileService fileService;


    public Tag addTag(long groupId, long fileId, String tagName) throws FileDoesNotExist, UserNotInGroup {
        File file = fileService.getFileFromGroupById(groupId,fileId).orElseThrow(FileDoesNotExist::new);
        return tagRepository.save(new Tag(tagName,file));
    }

//    public Tag getFileTags(long groupId, long fileId) {
//        return tagRepository.;
//    }
}
