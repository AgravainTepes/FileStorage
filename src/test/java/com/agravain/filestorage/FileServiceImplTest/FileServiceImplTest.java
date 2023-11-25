package com.agravain.filestorage.FileServiceImplTest;

import com.agravain.filestorage.DAO.FileRepository;
import com.agravain.filestorage.Entity.FileEntity;
import com.agravain.filestorage.Service.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FileServiceImplTest {

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private FileServiceImpl fileService;

    @Test
    @DisplayName("Создаёт Entity и передаёт его репозиторию." +
            " Возвращает сообщение об успешном выполнении.")
    void handleUploadFile_ReturnsValidResponseEntity() throws Exception {

        MockMultipartFile mockFile =
                new MockMultipartFile(
                        "file", "filename.txt",
                        MediaType.TEXT_PLAIN_VALUE, "some txt".getBytes());

        byte[] fileBytes = mockFile.getBytes();

        FileEntity fileEntity = new FileEntity()
                .setType(mockFile.getContentType())
                .setName(mockFile.getOriginalFilename())
                .setSize(mockFile.getSize())
                .setFile(fileBytes)
                .setCreateDate(LocalDateTime.now())
                .setUpdateDate(LocalDateTime.now());

        String responseMessage = "Content uploaded!";


        when(fileRepository.saveFile(fileEntity))
                .thenReturn(responseMessage);


        assertEquals(responseMessage, fileRepository.saveFile(fileEntity));
    }

    @Test
    @DisplayName("Возвращает список всех имён файлов, находящихся в ОЗУ или БД")
    void handleGetAllFileNames_ReturnsNotEmptyList() throws Exception {

        List<String> names = new ArrayList<>();
        names.add("testName");
        names.add("testName");
        names.add("testName");
        names.add("testName");


        when(fileRepository.getAllFIleNames())
                .thenReturn(names);


        assertEquals(names, fileRepository.getAllFIleNames());
    }

    @Test
    @DisplayName("Принимает набор параметров для фильтрации в виде Map." +
            "Достаёт каждый параметр и передаёт в репозиторий." +
            "Возвращает удовлетворяющий параметрам набор моделей")
    void handleGetModelsByParams_ReturnsNotEmptyList() throws Exception {

        List<FileEntity> fileEntities = new ArrayList<>();

        fileEntities
                .add(new FileEntity()
                        .setType("image/jpeg")
                        .setName("testName_1")
                        .setSize(3)
                        .setFile(new byte[]{1, 0, 1})
                        .setCreateDate(LocalDateTime.now())
                        .setUpdateDate(LocalDateTime.now()));

        fileEntities
                .add(new FileEntity()
                        .setType("image/png")
                        .setName("testName_2")
                        .setSize(3)
                        .setFile(new byte[]{1, 0, 1})
                        .setCreateDate(LocalDateTime.now())
                        .setUpdateDate(LocalDateTime.now()));


        when(fileRepository.getModelsByParams(anyString(), anyList(), any(), any()))
                .thenReturn(fileEntities);


        assertEquals(fileEntities, fileRepository
                .getModelsByParams(anyString(), anyList(), any(), any()));
    }


    @Test
    @DisplayName("Принимает набор ID в виде Map. " +
            "Превращает в List и передаёт в репозиторий. " +
            "Если файлов несколько, то сожмёт их в один архив и вернёт, " +
            "иначе вернёт одиночный файл."
    )
    void handleDownloadFilesByID_ReturnsSingleFile() throws Exception {

        List<Integer> listOfId = List.of(0, 1);

        List<FileEntity> fileEntities = new ArrayList<>();

        fileEntities
                .add(new FileEntity()
                        .setType("image/jpeg")
                        .setName("testName_1")
                        .setSize(3)
                        .setFile(new byte[]{1, 0, 1})
                        .setCreateDate(LocalDateTime.now())
                        .setUpdateDate(LocalDateTime.now()));

        fileEntities
                .add(new FileEntity()
                        .setType("image/png")
                        .setName("testName_2")
                        .setSize(3)
                        .setFile(new byte[]{1, 0, 1})
                        .setCreateDate(LocalDateTime.now())
                        .setUpdateDate(LocalDateTime.now()));

        when(fileRepository.getByID(listOfId))
                .thenReturn(fileEntities);

        assertEquals(fileEntities, fileRepository.getByID(listOfId));

    }

    @Test
    @DisplayName("Принимает параметры для обновления и id файла." +
            " Создаёт новый образ Entity и передаёт в репозиторий. " +
            "Возвращает сообщение об успешном обновлении.")
    void handlePatchFileById_ReturnsValidResponseEntity() throws Exception {

        FileEntity entity = new FileEntity()
                .setType("image/jpeg")
                .setName("testName_1")
                .setSize(3)
                .setFile(new byte[]{1, 0, 1})
                .setCreateDate(LocalDateTime.now())
                .setUpdateDate(LocalDateTime.now());

        int id = 1;

        String responseMessage = "File updated successfully!";

        when(fileRepository.patchFileById(entity, id))
                .thenReturn(responseMessage);


        assertEquals(responseMessage, fileRepository.patchFileById(entity, id));
    }

    @Test
    @DisplayName("Принимает на вход id подлежащего удалению файла" +
            " и передаёт репозиторию." +
            "Возвращает сообщение об успешном удалении.")
    void handleDeleteFileById_ReturnsValidResponseEntity() throws Exception {

        int id = 1;

        String responseMessage = "File delete successfully!";

        when(fileRepository.deleteFileById(id))
                .thenReturn(responseMessage);


        assertEquals(responseMessage, fileRepository.deleteFileById(id));
    }
}
