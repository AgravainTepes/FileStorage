package com.agravain.filestorage.Service;

import com.agravain.filestorage.DTO.FileDTO;
import com.agravain.filestorage.FileDataModel.FileDataModel;

import java.util.List;

public interface FileService {

     void saveFile(FileDataModel fileDataModel);
     List<String> getAllFileNames();
     List<FileDTO> getModelsByParams(String params);
}
