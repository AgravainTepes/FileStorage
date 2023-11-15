package com.agravain.filestorage.DAO;


import com.agravain.filestorage.Exceptions.FileExceptions.FileIsAlreadyExistsException;
import com.agravain.filestorage.Entity.FileEntity;
import com.agravain.filestorage.Exceptions.FileExceptions.NoSuchFileException;
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
        this.entityManager =
                entityManager;
    }

    @Override
    public void saveFile(FileEntity fileEntity) {
        Query query = entityManager
                .createQuery("select name from FileEntity where name = :name and type = :type");

        query.setParameter("name", fileEntity.getName());

        query.setParameter("type", fileEntity.getType());

        List<FileEntity> model = query.getResultList();

        if (!model.isEmpty())
            throw new FileIsAlreadyExistsException(
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

        return (List<String>) query.getResultList();
    }

    @Override
    public List<FileEntity> getModelsByParams(String name,
                                              List<String> types,
                                              LocalDateTime lower,
                                              LocalDateTime upper) {

        List<FileEntity> files = new ArrayList<>();

        for (int i = 0; i < types.size(); i++) {
            Query query = entityManager.createQuery(
                    "from FileEntity where" +
                            "((:type IS NULL ) or " +
                            "(lower(type) like concat('%', :type, '%')))  AND " +
                            "((:name IS NULL) or " +
                            "(lower(name) like concat('%', :name, '%'))) AND" +
                            "((:start IS NULL) or " +
                            "(updateDate >= :start AND " +
                            "updateDate <= :end))");

            query.setParameter("type", types.get(i));

            query.setParameter("name", name);

            query.setParameter("start", lower);

            query.setParameter("end", upper);

            files.addAll(query.getResultList());
        }

        return files;

    }

    public List<FileEntity> downloadByID(List<Integer> id) {

        List<FileEntity> fileEntityList = new ArrayList<>();

        for (Integer integer : id) {

            Query query = entityManager.createQuery(
                    "from FileEntity where id = :id");

            query.setParameter("id", integer.intValue());

            fileEntityList.addAll(query.getResultList());

        }

        if (fileEntityList.isEmpty())
            throw new NoSuchFileException("No such file with id: "
                    + id + " inside DB!");

        return fileEntityList;
    }


}
