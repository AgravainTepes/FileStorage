package com.agravain.filestorage.DAO;


import com.agravain.filestorage.Exceptions.FileExceptions.FileIsAlreadyExistsException;
import com.agravain.filestorage.FileDataModel.FileDataModel;
import com.agravain.filestorage.Filter.Filter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FileRepositoryImpl implements FileRepository {
    EntityManager entityManager;

    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void saveFile(FileDataModel fileDataModel) {
        Query query = entityManager
                .createQuery("select name from FileDataModel where name = :name and type = :type");
        query.setParameter("name", fileDataModel.getName());
        query.setParameter("type", fileDataModel.getType());
        List<FileDataModel> model = query.getResultList();
        if (!model.isEmpty())
            throw new FileIsAlreadyExistsException("" +
                    "FIle with this name : " +
                    fileDataModel.getName() +
                    " and this type : " +
                    fileDataModel.getType() + " is already exists! " +
                    " If you want to update him use PATCH METHOD!");
        entityManager.merge(fileDataModel);
    }

    @Override
    public List<String> getAllFIleNames() {
        Query query = entityManager
                .createQuery("select name from FileDataModel");
        List<String> names = query.getResultList();
        return names;
    }

    @Override
    public List<FileDataModel> getModelsByParams(Filter filter) {
        String nameForFilter = filter.getNameForFilterQuery();
        LocalDateTime startDateTime = filter.getStartDateTimeForFilterQuery();
        LocalDateTime endDateTime = filter.getEndDateTimeForFilterQuery();
        List<FileDataModel> files = new ArrayList<>();

        Query query = entityManager.createQuery(
                "from FileDataModel where" +
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
}
