package com.agravain.filestorage.Service;

import com.agravain.filestorage.DAO.FileRepository;
import com.agravain.filestorage.DTO.FileDTO;
import com.agravain.filestorage.Entity.FileEntity;
import com.agravain.filestorage.Exceptions.FileExceptions.IncorrectFileTimeException;
import com.agravain.filestorage.Exceptions.FileExceptions.NoSuchFileException;
import com.agravain.filestorage.Exceptions.FileExceptions.ZipFailException;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Service
@Transactional
public class FileServiceImpl implements FileService {

    private FileRepository fileRepository;

    @Autowired
    public void setDBFileRepository(FileRepository fileRepository) {

        this.fileRepository = fileRepository;

    }


    public String saveFile(MultipartFile file, byte[] fileBytes) {

        FileEntity fileEntity = new FileEntity()
                .setType(file.getContentType())
                .setName(file.getOriginalFilename())
                .setSize(file.getSize())
                .setFile(fileBytes)
                .setCreateDate(LocalDateTime.now())
                .setUpdateDate(LocalDateTime.now());

        return fileRepository.saveFile(fileEntity);

    }


    public List<String> getAllFileNames() {

        List<String> names =
                new ArrayList<>(fileRepository.getAllFIleNames());

        if (names.isEmpty())
            throw new NoSuchFileException("No such files inside DB!");

        return names;

    }


    public List<FileDTO> getModelsByParams(Map<String, String[]> params) {


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

        if (params.containsKey("lowerDateTime")
                && params.containsKey("upperDateTime")) {

            try {

                lowerDateTimeThreshold =
                        LocalDateTime
                                .parse(params.get("lowerDateTime")[0]);

                upperDateTimeThreshold =
                        LocalDateTime
                                .parse(params.get("upperDateTime")[0]);

            } catch (DateTimeParseException dateTimeParseException) {

                try {

                    lowerDateThreshold =
                            LocalDate
                                    .parse(params.get("lowerDateTime")[0]);

                    upperDateThreshold =
                            LocalDate
                                    .parse(params.get("upperDateTime")[0]);

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

        List<FileEntity> entityList =
                new ArrayList<>(fileRepository
                .getModelsByParams(
                        name,
                        types,
                        lowerDateTimeThreshold,
                        upperDateTimeThreshold));

        if (entityList.isEmpty())
            throw new NoSuchFileException(
                    "No such files with this parameters in data base!");

        List<FileDTO> fileDTOS = new ArrayList<>();

        for (FileEntity entity : entityList) {

            FileDTO fileDTO = new FileDTO();

            fileDTO.entityToDTO(entity);

            fileDTOS.add(fileDTO);

        }

        return fileDTOS;

    }


    public ZipSeparator downloadByID(Map<String, String[]> id) {

        List<String> StrIDList = new ArrayList<>();

        List<Integer> IntIDList = new ArrayList<>();

        if (id.containsKey("id"))
            StrIDList.addAll(Arrays.asList(id.get("id")));

        if (!StrIDList.isEmpty())

            for (String StrID : StrIDList) {

                IntIDList.add(Integer.parseInt(StrID));

            }

        List<FileEntity> entityList =
                new ArrayList<>(fileRepository.getByID(IntIDList));

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

            return new ZipSeparator()
                    .setSerialFile(serialFile)
                    .setIsZip(false)
                    .setContentType(contentType)
                    .setName(fileName);

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

            return new ZipSeparator()
                    .setSerialFile(bos.toByteArray())
                    .setIsZip(true)
                    .setName("Archive")
                    .setContentType("application/zip");

        } catch (IOException e) {

            throw new ZipFailException("Compression collapsed!");

        }
    }


    public String patchFileById(int id, MultipartFile file, byte[] fileBytes) {

        List<Integer> idForSearch = new ArrayList<>();

        idForSearch.add(id);

        FileEntity fileEntity =
                fileRepository
                        .getByID(idForSearch)
                        .get(0)
                        .setUpdateDate(LocalDateTime.now())
                        .setFile(fileBytes)
                        .setType(file.getContentType())
                        .setName(file.getOriginalFilename());

        return fileRepository.patchFileById(fileEntity, id);

    }


    public String deleteFileById(int id) {

        return fileRepository.deleteFileById(id);

    }

}




