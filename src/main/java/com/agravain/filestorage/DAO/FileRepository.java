package com.agravain.filestorage.DAO;

import com.agravain.filestorage.Entity.FileEntity;
import com.agravain.filestorage.Filter.Filter;

import java.util.List;
import java.util.Optional;

public interface FileRepository {

    void saveFile(FileEntity fileEntity);

    List<String> getAllFIleNames();

    List<FileEntity> getModelsByParams(Filter filter);

    FileEntity downloadByID(int id);
}
