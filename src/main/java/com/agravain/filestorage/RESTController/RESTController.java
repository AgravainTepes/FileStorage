package com.agravain.filestorage.RESTController;

import com.agravain.filestorage.FileDataModel.FileDataModel;
import com.agravain.filestorage.Service.FileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;


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
            @RequestBody MultipartFile file)  {
        FileDataModel fileModel = new FileDataModel();
        byte [] fileBytes;
        try {
         fileBytes = file.getBytes();
        }
        catch (IOException e){
            return new ResponseEntity<>(
                    "Something went wrong!", HttpStatus.BAD_REQUEST);
        }
        fileModel.setType(file.getContentType());
        fileModel.setName(file.getOriginalFilename());
        fileModel.setSize(file.getSize());
        fileModel.setFile(fileBytes);
        fileModel.setCreateDate(LocalDateTime.now());
        fileModel.setUpdateDate(LocalDateTime.now());
        service.saveFile(fileModel);
        return new ResponseEntity<>("File uploaded!", HttpStatus.OK);
    }
}