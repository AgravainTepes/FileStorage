package com.agravain.filestorage.RESTController;

import com.agravain.filestorage.DTO.FileDTO;
import com.agravain.filestorage.Entity.FileEntity;
import com.agravain.filestorage.Service.FileServiceImpl;
import com.agravain.filestorage.Utils.ZipSeparator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


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

        FileEntity fileEntity = new FileEntity();

        byte[] fileBytes;

        try {

            fileBytes = file.getBytes();

        } catch (IOException e) {

            return new ResponseEntity<>(
                    "Bytes reading failed!", HttpStatus.BAD_REQUEST);
        }
        fileEntity
                .setType(file
                        .getContentType());

        fileEntity
                .setName(file.getOriginalFilename());

        fileEntity
                .setSize(file.getSize());

        fileEntity
                .setFile(fileBytes);

        fileEntity
                .setCreateDate(LocalDateTime.now());

        fileEntity
                .setUpdateDate(LocalDateTime.now());

        service
                .saveFile(fileEntity);

        return new ResponseEntity<>("Content uploaded!", HttpStatus.OK);
    }


    @GetMapping("/names")
    public ResponseEntity<List<String>> getAllFileNames() {
        List<String> names = service.getAllFileNames();
        return new ResponseEntity<>(names, HttpStatus.OK);
    }

    @GetMapping("/filtered")
    public ResponseEntity<List<FileDTO>> getModelsByParams(
            HttpServletRequest request) {

        Map<String, String[]> params = request.getParameterMap();

        List<FileDTO> fileModels = service.getModelsByParams(params);

        return new ResponseEntity<>(fileModels, HttpStatus.OK);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFilesByID(
            HttpServletRequest request) {

        Map<String, String[]> id = request.getParameterMap();

        ZipSeparator separator = service.downloadByID(id);

        if (separator.isZip())
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.parseMediaType("application/zip"))
                    .body(new ByteArrayResource(separator.getSerialFile()));

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(separator.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + separator.getName() + "\"")
                .body(new ByteArrayResource(separator.getSerialFile()));
    }

    @PatchMapping("/patch/{id}")
    public ResponseEntity<String> patchFileById(@PathVariable int id,
                                                @RequestBody MultipartFile file) {

        byte[] fileBytes;

        try {

            fileBytes = file.getBytes();

        } catch (IOException e) {

            return new ResponseEntity<>(
                    "Bytes reading failed!", HttpStatus.BAD_REQUEST);
        }

        service.patchFileById(id, file, fileBytes);

        return new ResponseEntity<>("File updated successfully!", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFileById(@PathVariable int id) {

        service.deleteFileById(id);

        return new ResponseEntity<>("File delete successfully!", HttpStatus.OK);
    }
}