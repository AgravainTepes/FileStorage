package com.agravain.filestorage.Service;

import com.agravain.filestorage.DAO.FileRepository;
import com.agravain.filestorage.FileDataModel.FileDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class FileServiceImpl implements FileService{

    @Autowired
    private FileRepository fileRepository;


    public void saveFile(FileDataModel fileModel){
       fileRepository.save(fileModel);
    }
}
