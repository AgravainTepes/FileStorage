package com.agravain.filestorage.DAO;

import com.agravain.filestorage.Entity.FileEntity;
import com.agravain.filestorage.Exceptions.FileExceptions.FileIsAlreadyExistsException;
import com.agravain.filestorage.Exceptions.FileExceptions.NoSuchFileException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
@Profile("InMemory")
public class InMemFileRepositoryImpl implements FileRepository {


    private Map<Integer, FileEntity> fileEntityMap;

    private int currentID = 0;

    @Override
    public String saveFile(FileEntity fileEntity) {

        if (fileEntityMap == null)
            fileEntityMap = new HashMap<>();

        if (fileEntityMap.containsValue(fileEntity))
            throw new FileIsAlreadyExistsException(
                    "FIle with this name : " +
                            fileEntity.getName() +
                            " and this type : " +
                            fileEntity.getType() + " is already exists! " +
                            " If you want to update him use PATCH METHOD!");

        fileEntity.setId(currentID);

        fileEntityMap.putIfAbsent(currentID, fileEntity);

        currentID++;

        return "Content uploaded!";
    }

    @Override
    public List<String> getAllFIleNames() {

        if (fileEntityMap == null || fileEntityMap.isEmpty())
            throw new NoSuchFileException("No such files inside memory!");

        List<String> names = new ArrayList<>();

        List<FileEntity> entities =
                fileEntityMap.values().stream().toList();

        for (FileEntity entity : entities) {

            names.add(entity.getName());

        }

        return names;
    }

    @Override
    public List<FileEntity> getModelsByParams(String name,
                                              List<String> types,
                                              LocalDateTime lower,
                                              LocalDateTime upper) {

        if (fileEntityMap == null || fileEntityMap.isEmpty())
            throw new NoSuchFileException("No such files inside memory!");

        List<FileEntity> nonfilteredEntityList =
                fileEntityMap.values().stream().toList();

        List<FileEntity> filteredEntityList = new ArrayList<>();

        for (String type : types) {

            for (FileEntity entity : nonfilteredEntityList) {

                if (((name == null || entity.getName().contains(name))
                        && (type == null || type.equals(entity.getType()))
                        && (lower == null || lower.isBefore(entity.getUpdateDate()))
                        && (lower == null || lower.isBefore(entity.getUpdateDate())))) {

                    filteredEntityList.add(entity);

                }
            }
        }
        return filteredEntityList;
    }

    @Override
    public List<FileEntity> getByID(List<Integer> idList) {

        if (fileEntityMap == null || fileEntityMap.isEmpty())
            throw new NoSuchFileException("No such files inside memory!");

        List<FileEntity> entityList = new ArrayList<>();

        for (Integer id : idList) {

            if (fileEntityMap.containsKey(id))

                entityList.add(fileEntityMap.get(id));

        }

        if (entityList.isEmpty())
            throw new NoSuchFileException("No such files with id: "
                    + idList + " inside DB!");

        return entityList;
    }

    @Override
    public void patchFileById(FileEntity file, int id) {

        if (fileEntityMap == null || fileEntityMap.isEmpty())
            throw new NoSuchFileException("No such files inside memory!");

        if (!fileEntityMap.containsKey(id))
            throw new NoSuchFileException("No such file with id: "
                    + id + " inside DB!");

        fileEntityMap.put(id, file);
    }

    @Override
    public void deleteFileById(int id) {

        if (fileEntityMap == null || fileEntityMap.isEmpty())
            throw new NoSuchFileException("No such files inside memory!");

        if (!fileEntityMap.containsKey(id))
            throw new NoSuchFileException("No such file with id: "
                    + id + " inside DB!");

        fileEntityMap.remove(id);
    }
}
