package com.agravain.filestorage.DAO;


import com.agravain.filestorage.Exceptions.FileExceptions.FileIsAlreadyExistsException;
import com.agravain.filestorage.Entity.FileEntity;
import com.agravain.filestorage.Exceptions.FileExceptions.NoSuchFileException;
import com.agravain.filestorage.Filter.Filter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class FileRepositoryImpl implements FileRepository {
    EntityManager entityManager;

    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void saveFile(FileEntity fileEntity) {
        Query query = entityManager
                .createQuery("select name from FileEntity where name = :name and type = :type");
        query.setParameter("name", fileEntity.getName());
        query.setParameter("type", fileEntity.getType());
        List<FileEntity> model = query.getResultList();
        if (!model.isEmpty())
            throw new FileIsAlreadyExistsException("" +
                    "FIle with this name : " +
                    fileEntity.getName() +
                    " and this type : " +
                    fileEntity.getType() + " is already exists! " +
                    " If you want to update him use PATCH METHOD!");
        entityManager.merge(fileEntity);
    }

    @Override
    public List<String> getAllFIleNames() {
        Query query = entityManager
                .createQuery("select name from FileEntity");
        List<String> names = query.getResultList();
        return names;
    }

    @Override
    public List<FileEntity> getModelsByParams(Filter filter) {
        String nameForFilter = filter.getNameForFilterQuery();
        LocalDateTime startDateTime = filter.getStartDateTimeForFilterQuery();
        LocalDateTime endDateTime = filter.getEndDateTimeForFilterQuery();
        List<FileEntity> files = new ArrayList<>();

        Query query = entityManager.createQuery(
                "from FileEntity where" +
                        "((:type1 IS NULL and :type2 IS NULL and :type3 IS NULL" +
                        " and :type4 IS NULL and :type5 IS NULL) or " +
                        "(type = :type1) or (type = :type2) or (type = :type3)" +
                        " or (type = :type4) or (type = :type5))  AND " +
                        "((:name IS NULL) or " +
                        "(lower(name) like concat('%', :name, '%'))) AND" +
                        "((:start IS NULL) or " +
                        "(updateDate >= :start AND " +
                        "updateDate <= :end))");
        query.setParameter("type1", filter.ifTypeExist(0));
        query.setParameter("type2", filter.ifTypeExist(1));
        query.setParameter("type3", filter.ifTypeExist(2));
        query.setParameter("type4", filter.ifTypeExist(3));
        query.setParameter("type5", filter.ifTypeExist(4));
        query.setParameter("name", nameForFilter.toLowerCase());
        query.setParameter("start", startDateTime);
        query.setParameter("end", endDateTime);
        files.addAll(query.getResultList());


        return files;
    }

    public FileEntity downloadByID(int id) {
        Query query = entityManager.createQuery(
                "from FileEntity where id = :id");
        query.setParameter("id", id);
        List<FileEntity> fileEntityList = query.getResultList();
        if (fileEntityList.isEmpty())
            throw new NoSuchFileException("No such file with id: "
                    + id + " inside DB!");
        return fileEntityList.get(0);
    }


}
