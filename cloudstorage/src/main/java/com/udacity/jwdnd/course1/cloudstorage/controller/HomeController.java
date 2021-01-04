package com.udacity.jwdnd.course1.cloudstorage.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
public class HomeController {
    private FileService fileService;
    private UserService userService;
    private NoteService noteService;
    private CredentialService credentialService;

    //Additional messages
    private String errorMsg = null;
    private String successMsg = null;
    private boolean isRedirected = false;

    public HomeController(FileService fileService,  UserService userService, NoteService noteService, CredentialService credentialService) {
        this.fileService = fileService;
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService = credentialService;
    }

    private void resetNull(){
        this.errorMsg = null;
        this.successMsg = null;
    }
    private void updateModel(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        int userId = userService.getUser(auth.getName()).getUserId();

        model.addAttribute("errorMsg", errorMsg);
        model.addAttribute("successMsg", successMsg);
        model.addAttribute("files", this.fileService.getAllFiles(userId));
        model.addAttribute("notes", this.noteService.getNotes(userId));
        model.addAttribute("credentials", this.credentialService.getCredentials(userId));
    }

    @GetMapping("/home")
    public String getHomePage(@ModelAttribute("note") Note note, @ModelAttribute("credential") Credential credential, Model model){
        if(!isRedirected){
            resetNull();
        }
        updateModel(model);

        isRedirected = false;
        return "home";
    }

    @PostMapping("/upload-file")
    public String handleUploadFile(@RequestParam("fileUpload") MultipartFile fileUpload, Authentication auth, Model model){
        resetNull();

        int userId = userService.getUser(auth.getName()).getUserId();

        if(fileUpload.getOriginalFilename().length() == 0){
            errorMsg = "Please choose a file to upload.";
        }else if(fileUpload.isEmpty()){
            errorMsg = "Your file is empty. Please choose another.";
        }

        if(!this.fileService.isFilenameAvailable(userId, fileUpload.getOriginalFilename())){
            errorMsg = "It seems the file already uploaded. Please check it again!";
        }

        if(errorMsg == null){
            int rowsAdded = this.fileService.storeUploadedFile(userId, fileUpload);
            if (rowsAdded < 0) {
                errorMsg = "There was an error uploading the file. Please try again.";
            }
        }

        if(errorMsg == null){
            successMsg = "Your file uploaded! ";
        }

        isRedirected = true;
        return "redirect:/home";
    }

    @GetMapping("/delete/{id}")
    public String deleteFile(@PathVariable("id") int id, Authentication auth, Model model) {
        resetNull();

        int userId = userService.getUser(auth.getName()).getUserId();
        int deleteReturn = fileService.deleteFile(userId, id);
        if(deleteReturn <= 0){
            errorMsg = "Can't find the file to delete. Please check it again.";
        }else {
            successMsg = "File deleted!";
        }

        isRedirected = true;
        return "redirect:/home";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") int id, Authentication auth, Model model) {
        resetNull();
        int userId = userService.getUser(auth.getName()).getUserId();

        File file = fileService.getFile(userId, id);

        if(file == null){
            String a = "Error 404. \n The file does not exist \n";
            ByteArrayResource resource = new ByteArrayResource(a.getBytes(StandardCharsets.UTF_8));
            return ResponseEntity.badRequest()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(resource);
        }else{
            ByteArrayResource resource = new ByteArrayResource(file.getFileData());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+file.getFileName())
                    .contentType(MediaType.parseMediaType(file.getContentType()))
                    .contentLength(Long.parseLong(file.getFileSize()))
                    .body(resource);
        }
    }

    @PostMapping("/edit-note")
    public String editNote(@ModelAttribute("note") Note note, Authentication auth, Model model){
        resetNull();
        int userId = userService.getUser(auth.getName()).getUserId();

//        System.out.println("note "+note.getNoteId() + ", "+note.getNoteTitle() + ", "+note.getNoteDescription());

        int noteId = -1;
        if(note.getNoteId() == null){
            noteId = noteService.storeNote(userId, note);
        }else{
            noteId = noteService.updateNote(userId, note);
        }

        if(noteId <= 0){
            errorMsg = "There is an error during storing the note. Please try again!";
        }else{
            successMsg = "Done! ";
        }

        this.isRedirected = true;
        return "redirect:/home";
    }

    @GetMapping("/delete-note/{id}")
    public String deleteNote(@PathVariable("id") int id, Authentication auth, Model model) {
        resetNull();

        int userId = userService.getUser(auth.getName()).getUserId();
        int deleteReturn = noteService.deleteNote(userId, id);
        if(deleteReturn <= 0){
            errorMsg = "Can't find the note to delete. Please check it again.";
        }else {
            successMsg = "Note deleted!";
        }

        this.isRedirected = true;
        return "redirect:/home";
    }

    @PostMapping("/edit-cred")
    public String editNote(@ModelAttribute("credential") Credential credential, Authentication auth, Model model) {
        resetNull();
        int userId = userService.getUser(auth.getName()).getUserId();

//        System.out.println("... "+credential.getUrl() + ", "+credential.getUsername() +", "+credential.getPassword());
        int retValue = -1;
        if(credential.getCredentialId() == null){
            retValue = credentialService.createCredential(userId, credential);
        }else{
            retValue = credentialService.updateCredential(userId, credential);
        }

        if(retValue <= 0){
            errorMsg = "There is an error during storing the credential. Please try again!";
        }else{
            successMsg = "Done! ";
        }

        this.isRedirected = true;
        return "redirect:/home";
    }

    @GetMapping("/delete-cred/{id}")
    public String deleteCred(@PathVariable("id") int id, Authentication auth, Model model) {
        resetNull();

        int userId = userService.getUser(auth.getName()).getUserId();
        int deleteReturn = credentialService.deleteCredential(userId, id);
        if(deleteReturn <= 0){
            errorMsg = "Can't find the credential to delete. Please check it again.";
        }else {
            successMsg = "Credential deleted!";
        }

        this.isRedirected = true;
        return "redirect:/home";
    }
}
