package com.agravain.filestorage.RESTController;

import com.agravain.filestorage.DTO.FileDTO;
import com.agravain.filestorage.Entity.FileEntity;
import com.agravain.filestorage.Service.FileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api")
@EnableAspectJAutoProxy
public class RESTController {

    private FileServiceImpl service;

    @Autowired
    public void setService(FileServiceImpl service) {
        this.service = service;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestBody MultipartFile file) {
        FileEntity fileModel = new FileEntity();
        byte[] fileBytes;
        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            return new ResponseEntity<>(
                    "Something went wrong!", HttpStatus.BAD_REQUEST);
        }
        fileModel.setType(MediaType.parseMediaType(file.getContentType()).getType());
        fileModel.setName(file.getOriginalFilename());
        fileModel.setSize(file.getSize());
        fileModel.setFile(fileBytes);
        fileModel.setCreateDate(LocalDateTime.now());
        fileModel.setUpdateDate(LocalDateTime.now());
        service.saveFile(fileModel);
        return new ResponseEntity<>("File uploaded!", HttpStatus.OK);
    }


    @GetMapping("/names")
    public ResponseEntity<List<String>> getAllFileNames() {
        List<String> names = service.getAllFileNames();
        return new ResponseEntity<>(names, HttpStatus.OK);
    }

    @GetMapping("/filtered/{params}")
    public ResponseEntity<List<FileDTO>> getModelsByParams(
            @PathVariable String params) {
        List<FileDTO> result = service.getModelsByParams(params);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }




}