package com.agravain.filestorage.DAO;

import com.agravain.filestorage.FileDataModel.FileDataModel;
import com.agravain.filestorage.Filter.Filter;

import java.util.List;

public interface FileRepository {

    void saveFile(FileDataModel fileDataModel);

    List<String> getAllFIleNames();

    List<FileDataModel> getModelsByParams(Filter filter);
}
