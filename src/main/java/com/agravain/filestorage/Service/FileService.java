package com.agravain.filestorage.Service;

import com.agravain.filestorage.DTO.FileDTO;
import com.agravain.filestorage.Entity.FileEntity;

import java.util.List;

public interface FileService {

     void saveFile(FileEntity fileEntity);
     List<String> getAllFileNames();
     List<FileDTO> getModelsByParams(String params);
     FileEntity downloadByID(int id);
}
