package com.agravain.filestorage.Service;

import com.agravain.filestorage.DAO.FileRepositoryImpl;
import com.agravain.filestorage.DTO.FileDTO;
import com.agravain.filestorage.Entity.FileEntity;
import com.agravain.filestorage.Exceptions.FileExceptions.IncorrectFileTimeException;
import com.agravain.filestorage.Exceptions.FileExceptions.NoSuchFileException;
import com.agravain.filestorage.Exceptions.FileExceptions.ZipFailException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Service
public class FileServiceImpl implements FileService {

    private FileRepositoryImpl fileRepository;

    @Autowired
    public void setFileRepository(FileRepositoryImpl fileRepository) {

        this.fileRepository = fileRepository;
    }

    @Transactional
    public void saveFile(FileEntity fileModel) {

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
    public List<FileDTO> getModelsByParams(Map<String, String[]> params) {
        String name = "";

        LocalDateTime lowerTimeThreshold = null;

        LocalDateTime upperTimeThreshold = null;

        List<String> types = new ArrayList<>();

        if (params.containsKey("name"))
            name = params.get("name")[0];

        if (params.containsKey("lower") && params.containsKey("upper")) {

            try {
                lowerTimeThreshold = LocalDateTime.parse(params.get("lower")[0]);
                upperTimeThreshold = LocalDateTime.parse(params.get("upper")[0]);

            } catch (DateTimeParseException e) {
                throw new IncorrectFileTimeException("Invalid time parameter!" +
                        " Please use pattern : 2024-12-31 23:59:59");
            }
        }
        if (params.containsKey("type"))
            types.addAll(Arrays.asList(params.get("type")));

        if (types.isEmpty())
            types.add(null);

        List<FileEntity> entityList = fileRepository
                .getModelsByParams(name, types, lowerTimeThreshold, upperTimeThreshold);

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

    public byte[] downloadByID(Map<String, String[]> id) {

        List<String> StrIDList = new ArrayList<>();

        List<Integer> IntIDList = new ArrayList<>();

        if (id.containsKey("id"))
            StrIDList.addAll(Arrays.asList(id.get("id")));

        if (!StrIDList.isEmpty())
            for (String StrID : StrIDList) {
                IntIDList.add(Integer.parseInt(StrID));
            }

        List<FileEntity> entityList = fileRepository.downloadByID(IntIDList);

        if (entityList.size() == 1)
            return entityList.get(0).getFile();

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ZipOutputStream zop = new ZipOutputStream(bos)) {

            for (FileEntity entity : entityList) {

                ZipEntry entry = new ZipEntry("/" + entity.getName());

                CRC32 crc32 = new CRC32();

                crc32.update(entity.getFile());

                entry.setCrc(crc32.getValue());

                entry.setSize(entity.getFile().length);

                zop.putNextEntry(entry);

                zop.write(entity.getFile());

                zop.closeEntry();
            }
            zop.finish();

            return bos.toByteArray();
        }
        catch (IOException e) {

            throw new ZipFailException("Compression collapsed!");
        }
    }
}




