package ro.unibuc.filespace.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.unibuc.filespace.Model.File;
import ro.unibuc.filespace.Model.Group;
import ro.unibuc.filespace.Model.Storage;
import ro.unibuc.filespace.Repository.StorageRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageService {
    private final StorageRepository storageRepository;

    Storage addFileToGroup(File file, Group group) {
        return storageRepository.save(new Storage(group, file));
    }
}
