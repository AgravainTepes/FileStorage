package com.agravain.filestorage.Service;

import com.agravain.filestorage.DAO.FileRepository;
import com.agravain.filestorage.Exceptions.FileExceptions.NoSuchFileException;
import com.agravain.filestorage.FileDataModel.FileDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepository fileRepository;


    public void saveFile(FileDataModel fileModel) {
        fileRepository.save(fileModel);
    }

    public List<String> getAllFileNames() {
        List<FileDataModel> fileDataModelList = fileRepository.findAll();
        List<String> names = fileDataModelList.stream().map(fileDataModel
                -> fileDataModel.getName()).toList();
        if (names.isEmpty())
            throw new NoSuchFileException("No such files inside DB!");
        return names;
    }


}
