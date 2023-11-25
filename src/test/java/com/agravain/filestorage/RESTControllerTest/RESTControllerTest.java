package com.agravain.filestorage.RESTControllerTest;

import com.agravain.filestorage.DTO.FileDTO;
import com.agravain.filestorage.RESTController.RESTController;
import com.agravain.filestorage.Service.FileServiceImpl;
import com.agravain.filestorage.Utils.ZipSeparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class RESTControllerTest {

    @Mock
    private FileServiceImpl fileService;

    @InjectMocks
    private RESTController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void seUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

    }

    @Test
    @DisplayName("POST /api/upload возвращает HTTP-ответ со статусом 200 OK" +
            " и сообщением об успешном сохранении.")
    void handleUploadFile_ReturnsValidResponseEntity() throws Exception {

        MockMultipartFile mockFile =
                new MockMultipartFile(
                        "file", "filename.txt",
                        MediaType.TEXT_PLAIN_VALUE, "some txt".getBytes());

        byte[] fileBytes = mockFile.getBytes();

        String responseMessage = "Content uploaded!";

        when(fileService.saveFile(mockFile, fileBytes))
                .thenReturn(responseMessage);


        mockMvc.perform(multipart("/api/upload")
                        .file(mockFile))
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .string(responseMessage));
    }

    @Test
    @DisplayName("GET /api/names возвращает HTTP-ответ со статусом 200 OK" +
            " и списком имён файлов.")
    void handleGetAllFileNames_ReturnsNotEmptyList() throws Exception {

        List<String> names = new ArrayList<>();
        names.add("testName");
        names.add("testName");
        names.add("testName");
        names.add("testName");

        when(fileService.getAllFileNames())
                .thenReturn(names);


        mockMvc.perform(get("/api/names"))
                .andExpect(status()
                        .isOk())
                .andExpect(content().json(names.toString()));
    }

    @Test
    @DisplayName("GET /api/filtered возвращает HTTP-ответ со статусом 200 OK" +
            " и JSONArray отфильтрованных моделей.")
    void handleGetModelsByParams_ReturnsNotEmptyList() throws Exception {

        List<FileDTO> fileModels = new ArrayList<>();

        fileModels
                .add(new FileDTO()
                        .setName("testName")
                        .setSize(1024)
                        .setType("image/png")
                        .setCreateDate(LocalDateTime.now())
                        .setUpdateDate(LocalDateTime.now()));

        fileModels
                .add(new FileDTO()
                        .setName("testName2")
                        .setSize(1024)
                        .setType("image/png")
                        .setCreateDate(LocalDateTime.now())
                        .setUpdateDate(LocalDateTime.now()));


        when(fileService.getModelsByParams(anyMap())).thenReturn(fileModels);


        mockMvc.perform(get("/api/filtered"))
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }


    @Test
    @DisplayName("GET /api/download возвращает HTTP-ответ со статусом 200 OK" +
            " и файл в теле ответа.")
    void handleDownloadFilesByID_ReturnsSingleFile() throws Exception {

        ZipSeparator zipSeparator = new ZipSeparator()
                .setName("testName")
                .setContentType("image/png")
                .setSerialFile(new byte[]{1, 0, 1})
                .setIsZip(false);

        when(fileService.downloadByID(anyMap()))
                .thenReturn(zipSeparator);


        mockMvc.perform(get("/api//download").param("id", "1"))
                .andExpect(status()
                        .isOk())
                .andExpect(content().bytes(zipSeparator.getSerialFile()))
                .andExpect(content().contentType(zipSeparator.getContentType()));
    }

    @Test
    @DisplayName("GET /api/download возвращает HTTP-ответ со статусом 200 OK" +
            " и архив в теле ответа.")
    void handleDownloadFilesByID_ReturnsArchive() throws Exception {

        ZipSeparator zipSeparator = new ZipSeparator()
                .setName("testName")
                .setContentType("application/zip")
                .setSerialFile(new byte[]{1, 0, 1})
                .setIsZip(true);

        when(fileService.downloadByID(anyMap()))
                .thenReturn(zipSeparator);


        mockMvc.perform(get("/api/download").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().bytes(zipSeparator.getSerialFile()))
                .andExpect(content().contentType(zipSeparator.getContentType()));
    }

    @Test
    @DisplayName("PATCH /api/patch/{id} возвращает HTTP-ответ со статусом 200 OK" +
            " и сообщением об успешном обновлении.")
    void handlePatchFileById_ReturnsValidResponseEntity() throws Exception {

        MockMultipartFile mockFile =
                new MockMultipartFile(
                        "file", "filename.txt",
                        MediaType.TEXT_PLAIN_VALUE, "some txt".getBytes());

        byte[] fileBytes = mockFile.getBytes();

        int id = 1;

        String responseMessage = "File updated successfully!";

        when(fileService.patchFileById(id, mockFile, fileBytes))
                .thenReturn(responseMessage);


        mockMvc.perform(multipart(HttpMethod.PATCH,"/api/patch/{id}", id)
                        .file(mockFile))
                .andExpect(status().isOk())
                .andExpect(content().string(responseMessage));
    }


}
