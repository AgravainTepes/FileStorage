package com.agravain.filestorage.Service;

import com.agravain.filestorage.DAO.DBFileRepositoryImpl;

import com.agravain.filestorage.DAO.InMemFileRepositoryImpl;
import com.agravain.filestorage.DTO.FileDTO;
import com.agravain.filestorage.Entity.FileEntity;
import com.agravain.filestorage.Exceptions.FileExceptions.IncorrectFileTimeException;
import com.agravain.filestorage.Exceptions.FileExceptions.NoSuchFileException;
import com.agravain.filestorage.Exceptions.FileExceptions.ZipFailException;
import com.agravain.filestorage.Exceptions.ProfileExceptions.IncorrectProfileCombinationException;
import com.agravain.filestorage.Utils.ZipSeparator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Service
@Transactional
public class FileServiceImpl implements FileService {

    private DBFileRepositoryImpl DBFileRepository;


    private InMemFileRepositoryImpl inMemFileRepository;

    @Autowired(required = false)
    public void setDBFileRepository(DBFileRepositoryImpl DBFileRepository) {

        this.DBFileRepository = DBFileRepository;
    }

    @Autowired(required = false)
    public void setInMemFileRepository(InMemFileRepositoryImpl inMemFileRepository) {

        this.inMemFileRepository = inMemFileRepository;

    }


    public String saveFile(MultipartFile file, byte[] fileBytes) {

        String profile = defineProfile();

        String responseMessage = "";

        FileEntity fileEntity = new FileEntity();

        fileEntity
                .setType(file
                        .getContentType());

        fileEntity
                .setName(file.getOriginalFilename());

        fileEntity
                .setSize(file.getSize());

        fileEntity
                .setFile(fileBytes);

        fileEntity
                .setCreateDate(LocalDateTime.now());

        fileEntity
                .setUpdateDate(LocalDateTime.now());

        if (profile.equals("InDB"))
            responseMessage =
                    DBFileRepository.saveFile(fileEntity);

        responseMessage =
                inMemFileRepository.saveFile(fileEntity);

        return responseMessage;
    }


    public List<String> getAllFileNames() {

        String profile = defineProfile();

        List<String> names = new ArrayList<>();

        if (profile.equals("InDB"))
            names.addAll(DBFileRepository.getAllFIleNames());
        names.addAll(inMemFileRepository.getAllFIleNames());


        if (names.isEmpty())
            throw new NoSuchFileException("No such files inside DB!");
        return names;
    }


    public List<FileDTO> getModelsByParams(Map<String, String[]> params) {

        String profile = defineProfile();

        String name = "";

        LocalDateTime lowerDateTimeThreshold = null;

        LocalDate lowerDateThreshold;

        LocalDateTime upperDateTimeThreshold = null;

        LocalDate upperDateThreshold;

        LocalTime timeStub =
                LocalTime
                        .of(0, 0, 0);

        List<String> types = new ArrayList<>();

        if (params.containsKey("name"))
            name = params.get("name")[0];

        if (params.containsKey("lower") && params.containsKey("upper")) {

            try {

                lowerDateTimeThreshold =
                        LocalDateTime
                                .parse(params.get("lower")[0]);

                upperDateTimeThreshold =
                        LocalDateTime
                                .parse(params.get("upper")[0]);

            } catch (DateTimeParseException dateTimeParseException) {

                try {

                    lowerDateThreshold =
                            LocalDate
                                    .parse(params.get("lower")[0]);

                    upperDateThreshold =
                            LocalDate
                                    .parse(params.get("upper")[0]);

                    lowerDateTimeThreshold =
                            LocalDateTime
                                    .of(lowerDateThreshold, timeStub);


                    upperDateTimeThreshold =
                            LocalDateTime
                                    .of(upperDateThreshold, timeStub);

                } catch (DateTimeParseException dateParseException) {

                    throw new IncorrectFileTimeException("Invalid time parameter!" +
                            " Please use pattern : 2024-12-31 23:59:59 " +
                            "or without time : 2024-12-31");

                }

            }

        }

        if (params.containsKey("type"))
            types.addAll(Arrays.asList(params.get("type")));

        if (types.isEmpty())
            types.add(null);

        List<FileEntity> entityList = new ArrayList<>();

        if (profile.equals("InDB"))
            entityList.addAll(DBFileRepository
                    .getModelsByParams(
                            name,
                            types,
                            lowerDateTimeThreshold,
                            upperDateTimeThreshold));

        entityList.addAll(inMemFileRepository
                .getModelsByParams(
                        name,
                        types,
                        lowerDateTimeThreshold,
                        upperDateTimeThreshold));

        if (entityList.isEmpty())
            throw new NoSuchFileException(
                    "No such files with this parameters in data base!!!");

        List<FileDTO> fileDTOS = new ArrayList<>();

        for (FileEntity entity : entityList) {

            FileDTO fileDTO = new FileDTO();

            fileDTO.entityToDTO(entity);

            fileDTOS.add(fileDTO);
        }
        return fileDTOS;
    }


    public ZipSeparator downloadByID(Map<String, String[]> id) {

        String profile = defineProfile();

        List<String> StrIDList = new ArrayList<>();

        List<Integer> IntIDList = new ArrayList<>();

        if (id.containsKey("id"))
            StrIDList.addAll(Arrays.asList(id.get("id")));

        if (!StrIDList.isEmpty())
            for (String StrID : StrIDList) {
                IntIDList.add(Integer.parseInt(StrID));
            }

        List<FileEntity> entityList = new ArrayList<>();

        if (profile.equals("InDB"))
            entityList.addAll(DBFileRepository.getByID(IntIDList));

        entityList.addAll(inMemFileRepository.getByID(IntIDList));

        ZipSeparator separator =
                new ZipSeparator();

        if (entityList.size() == 1) {
            FileEntity singleEntity =
                    entityList
                            .get(0);


            byte[] serialFile =
                    singleEntity
                            .getFile();

            String contentType =
                    singleEntity
                            .getType();

            String fileName =
                    singleEntity
                            .getName();

            separator
                    .setSerialFile(serialFile);

            separator
                    .setIsZip(false);

            separator
                    .setContentType(contentType);

            separator
                    .setName(fileName);

            return separator;
        }


        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ZipOutputStream zop = new ZipOutputStream(bos)) {

            for (FileEntity entity : entityList) {

                ZipEntry entry = new ZipEntry(entity.getName());

                entry.setSize(entity.getFile().length);

                zop.putNextEntry(entry);

                zop.write(entity.getFile());

                zop.closeEntry();
            }

            zop.finish();

            ZipSeparator zipSeparator =
                    new ZipSeparator();
            zipSeparator
                    .setSerialFile(bos.toByteArray());
            zipSeparator
                    .setIsZip(true);

            return zipSeparator;

        } catch (IOException e) {

            throw new ZipFailException("Compression collapsed!");
        }
    }


    public String patchFileById(int id, MultipartFile file, byte[] fileBytes) {

        String profile = defineProfile();

        String responseMessage = "";

        List<Integer> idForSearch = new ArrayList<>();

        idForSearch.add(id);

        if (profile.equals("InDB")) {

            FileEntity fileEntity =
                    DBFileRepository
                            .getByID(idForSearch)
                            .get(0);

            fileEntity
                    .setUpdateDate(LocalDateTime.now());

            fileEntity
                    .setFile(fileBytes);

            fileEntity
                    .setType(fileEntity.getType());

            fileEntity
                    .setName(file.getOriginalFilename());

            responseMessage =
                    DBFileRepository.patchFileById(fileEntity, id);

        } else {
            FileEntity fileEntity =
                    inMemFileRepository
                            .getByID(idForSearch)
                            .get(0);

            fileEntity
                    .setUpdateDate(LocalDateTime.now());

            fileEntity
                    .setFile(fileBytes);

            fileEntity
                    .setType(fileEntity.getType());

            fileEntity
                    .setName(file.getOriginalFilename());

            responseMessage =
                    inMemFileRepository.patchFileById(fileEntity, id);

        }

        return responseMessage;
    }


    public void deleteFileById(int id) {

        String profile = defineProfile();

        if (profile.equals("InDB")) {

            List<Integer> idForSearch = new ArrayList<>();

            idForSearch.add(id);

            FileEntity fileEntity =
                    DBFileRepository
                            .getByID(idForSearch)
                            .get(0);

            DBFileRepository.deleteFileById(id);
        } else {

            inMemFileRepository.deleteFileById(id);
        }

    }

    private String defineProfile() {

        if (inMemFileRepository == null && DBFileRepository == null)
            throw new IncorrectProfileCombinationException(
                    "Specify the launch profile!");

        if (inMemFileRepository != null && DBFileRepository != null)
            throw new IncorrectProfileCombinationException(
                    "Remove one of the profiles!");

        if (inMemFileRepository != null)
            return "InMemory";

        return "InDB";
    }
}




