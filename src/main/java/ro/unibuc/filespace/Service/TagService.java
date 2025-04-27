package ro.unibuc.filespace.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ro.unibuc.filespace.Model.Tag;
import ro.unibuc.filespace.Repository.FileRepository;
import ro.unibuc.filespace.Repository.TagRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {
    private final TagRepository tagRepository;

    public Tag addTag(long groupId, long fileId, String tagName) {
        return tagRepository.save(new Tag());
    }

    public Tag getFIleTags(long groupId, long fileId) {
        return tagRepository.;
    }
}
