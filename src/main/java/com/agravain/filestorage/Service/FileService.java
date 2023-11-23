package com.agravain.filestorage.Service;

import com.agravain.filestorage.DTO.FileDTO;
import com.agravain.filestorage.Entity.FileEntity;
import com.agravain.filestorage.Utils.ZipSeparator;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface FileService {

     String saveFile(MultipartFile file, byte[] fileBytes);

     List<String> getAllFileNames();

     List<FileDTO> getModelsByParams(Map<String, String[]> params);

     ZipSeparator downloadByID(Map<String, String[]> id);

     void patchFileById(int id, MultipartFile file, byte[] fileBytes);

     void deleteFileById(int id);
}
