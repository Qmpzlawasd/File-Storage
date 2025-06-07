package ro.unibuc.filespace.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.unibuc.filespace.Exception.*;
import ro.unibuc.filespace.Model.Group;
import ro.unibuc.filespace.Repository.FileRepository;
import ro.unibuc.filespace.Repository.GroupRepository;
import ro.unibuc.filespace.Service.FileService;
import ro.unibuc.filespace.Service.GroupService;
import ro.unibuc.filespace.Service.UserService;

import java.io.IOException;
import java.util.Collections;

@Controller
@RequiredArgsConstructor
public class ViewController {
    private final GroupRepository groupRepository;
    private final FileService fileService;
    private final UserService userService;
    private final FileRepository fileRepository;
    private final GroupService groupService;

    @GetMapping("/groups")
    public String getGroups(Model model) {
        groupRepository.findAll();
        model.addAttribute("groups", groupRepository.findAll());
        return "list-groups.html";
    }

    @GetMapping("/files/{groupId}")
    public String showCreateGroupForm(@PathVariable Long groupId, Model model) {
        model.addAttribute("files", groupRepository.findFilesInGroup(groupId));
        return "list-files.html";
    }
    @GetMapping("/create-group")
    public String showCreateGroupForm(Model model) {
        model.addAttribute("group", new Group());
        return "create-group";
    }

    @PostMapping("/create-group")
    public String createGroup(@ModelAttribute Group group) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userService.findUserByUsername("string").get(), null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);
        groupService.createGroup(group.getGroupName());
        return "redirect:/groups";
    }

    @PostMapping("/upload-file/{groupId}")
    public String handleFileUpload(
            @PathVariable Long groupId,
            @RequestParam("file") MultipartFile file,
            Model model) {

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userService.findUserByUsername("string").get(), null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);

        try {
            fileService.storeFile(groupId, file);
            return "redirect:/files/" + groupId;
        } catch (UserNotInGroup e) {
            model.addAttribute("error", "You don't have permission to upload to this group");
        } catch (FileWithNameAlreadyExists e) {
            model.addAttribute("error", "A file with this name already exists in the group");
        } catch (FileIsEmpty e) {
            model.addAttribute("error", "Cannot upload empty file");
        } catch (GroupDoesNotExist e) {
            model.addAttribute("error", "Group not found");
        } catch (IOException e) {
            model.addAttribute("error", "Error processing file upload");
        }
        model.addAttribute("files", groupRepository.findFilesInGroup(groupId));
        model.addAttribute("groupId", groupId);

        return "list-files";
    }

    @GetMapping("/delete-file/{groupId}/{fileName}")
    public String handleFileDelete(
            @PathVariable Long groupId,
            @PathVariable  String fileName,
            Model model) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userService.findUserByUsername("string").get(), null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);
        try {
            fileService.deleteFileFromGroup(groupId, fileName);
            SecurityContextHolder.clearContext();
            return "redirect:/files/" + groupId;
        } catch (FileDoesNotExist e) {
            model.addAttribute("error", "File not found");
        } catch (GroupDoesNotExist e) {
            model.addAttribute("error", "Group not found");
        } catch (UserNotInGroup e) {
            model.addAttribute("error", "You don't have permission to delete from this group");
        }
        model.addAttribute("files", groupRepository.findFilesInGroup(groupId));
        model.addAttribute("groupId", groupId);
        return "list-files";
    }
}