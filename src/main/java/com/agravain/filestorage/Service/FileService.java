package com.agravain.filestorage.Service;

import com.agravain.filestorage.DTO.FileDTO;
import com.agravain.filestorage.Entity.FileEntity;
import java.util.List;
import java.util.Map;

public interface FileService {

     void saveFile(FileEntity fileEntity);
     List<String> getAllFileNames();
     List<FileDTO> getModelsByParams(Map<String, String[]> params);
     FileEntity downloadByID(int id);
}
