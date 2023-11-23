package com.agravain.filestorage.RESTControllerTest;

import com.agravain.filestorage.DAO.FileRepository;
import com.agravain.filestorage.Entity.FileEntity;
import com.agravain.filestorage.RESTController.RESTController;
import com.agravain.filestorage.Service.FileService;
import com.agravain.filestorage.Service.FileServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.JsonPath;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
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
                        MediaType.TEXT_PLAIN_VALUE, "some xml".getBytes());

        byte[] fileBytes = mockFile.getBytes();

        when(fileService.saveFile(mockFile, fileBytes))
                .thenReturn("Content uploaded!");


        mockMvc.perform(multipart("/api/upload")
                        .file(mockFile))
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .string("Content uploaded!"));

    }

}
