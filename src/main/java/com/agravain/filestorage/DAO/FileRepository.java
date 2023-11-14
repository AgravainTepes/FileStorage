package com.agravain.filestorage.DAO;

import com.agravain.filestorage.Entity.FileEntity;

import java.time.LocalDateTime;
import java.util.List;


public interface FileRepository {

    void saveFile(FileEntity fileEntity);

    List<String> getAllFIleNames();

    List<FileEntity> getModelsByParams(String name,
                                      List<String> types,
                                       LocalDateTime lower,
                                       LocalDateTime upper);

    FileEntity downloadByID(int id);
}
