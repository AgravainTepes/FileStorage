package com.agravain.filestorage.Service;

import com.agravain.filestorage.DAO.FileRepositoryImpl;
import com.agravain.filestorage.DTO.FileDTO;
import com.agravain.filestorage.Exceptions.FileExceptions.NoSuchFileException;
import com.agravain.filestorage.Exceptions.FilterExceptions.IncorrectFilterParams;
import com.agravain.filestorage.FileDataModel.FileDataModel;
import com.agravain.filestorage.Filter.Filter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepositoryImpl fileRepository;

    @Transactional
    public void saveFile(FileDataModel fileModel) {
        fileRepository.saveFile(fileModel);
    }

    @Transactional
    public List<String> getAllFileNames() {
        List<String> names = fileRepository.getAllFIleNames();
        if (names.isEmpty())
            throw new NoSuchFileException("No such files inside DB!");
        return names;
    }

    @Transactional
    public List<FileDTO> getModelsByParams(String params) {
        Filter filter = new Filter();
        filter.setNameForFilterQuery(params);
        filter.setStartDateTimeForFilterQuery(params);
        filter.setEndDateTimeForFilterQuery(params);
        filter.setFileTypesForFilterQuery(params);
        if (filter.isEmpty() && !params.isEmpty())
            throw new IncorrectFilterParams("error in parameters!");
        List<FileDataModel> middleResult = fileRepository.getModelsByParams(filter);
//        if (middleResult.isEmpty())
//            throw new NoSuchFileException(
//                    "No such files with this parameters inside DB!");
        List<FileDTO> finalResult = new ArrayList<>();
        for (FileDataModel model : middleResult) {
            FileDTO fileDTO = new FileDTO();
            fileDTO.fileDataModelToDTO(model);
            finalResult.add(fileDTO);
        }
        return finalResult;
    }

}
