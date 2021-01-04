package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class FileService {
    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public boolean isFilenameAvailable(int userId, String fileName){
        return this.fileMapper.getFilesWithName(userId, fileName).size() == 0;
    }

    public int storeUploadedFile(int userId, MultipartFile fileUpload) {
        File file = null;
        try {
            file = new File(null, fileUpload.getOriginalFilename(), fileUpload.getContentType(), fileUpload.getSize()+"", userId, fileUpload.getBytes());
        }catch (IOException e){
            e.printStackTrace();
        }
        return this.fileMapper.insertFile(file);
    }

    public int deleteFile(int userId, int fileId){
        return fileMapper.deleteFile(userId, fileId);
    }

    public File getFile(int userId, int fileId){
        return fileMapper.getFile(userId, fileId);
    }

    public List<File> getAllFiles(int userId){
        return  this.fileMapper.getFiles(userId);
    }
}
