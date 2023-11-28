package com.agravain.filestorage.RESTController;

import com.agravain.filestorage.DTO.FileDTO;
import com.agravain.filestorage.Exceptions.FileExceptions.NoSuchFileException;
import com.agravain.filestorage.Service.FileServiceImpl;
import com.agravain.filestorage.Utils.ZipSeparator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
@EnableAspectJAutoProxy
@Tag(name = "Хранилище файлов", description = "Методы для работы с файлами")
public class RESTController {

    private FileServiceImpl service;

    @Autowired
    public void setService(FileServiceImpl service) {

        this.service = service;

    }

    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Загрузка файла")

    @ApiResponse(responseCode = "200",
            description = "Content uploaded!",
            content = {@Content(
                    schema = @Schema(defaultValue = "Content uploaded!"),
                    mediaType = "application/json")})

    @ApiResponse(responseCode = "400",
            description = "File is already exists!",
            content = {@Content(
                    schema = @Schema(defaultValue = "File is already exists"),
                    mediaType = "application/json")})

    @ApiResponse(responseCode = "406",
            description = "Bytes reading failed!",
            content = {@Content(
                    schema = @Schema(defaultValue = "Bytes reading failed!"),
                    mediaType = "application/json")})

    public ResponseEntity<String> uploadFile(
            @RequestBody
            MultipartFile file) {

        byte[] fileBytes;

        try {

            fileBytes = file.getBytes();

        } catch (IOException e) {

            return new ResponseEntity<>(
                    "Bytes reading failed!", HttpStatusCode.valueOf(406));
        }

        String responseMessage =
                service.saveFile(file, fileBytes);

        return new ResponseEntity<>(responseMessage, HttpStatus.OK);

    }


    @GetMapping("/names")
    @Operation(summary = "Возвращает список всех имён файлов, находящихся в БД или ОЗУ")

    @ApiResponse(responseCode = "200",
            description = "List of names received!",
            content = {@Content(
                    schema = @Schema(defaultValue = "listOfNames : {Mammal.png," +
                            " BirdMan.pdf, Udult.gif}"),
                    mediaType = "application/json")})

    @ApiResponse(responseCode = "404",
            description = "Storage is empty",
            content = {@Content(
                    schema = @Schema(defaultValue = "info " + ":" +
                            " No such files inside DB!"),
                    mediaType = "application/json")})

    public ResponseEntity<List<String>> getAllFileNames() {

        List<String> names = service.getAllFileNames();

        return new ResponseEntity<>(names, HttpStatus.OK);

    }

    @GetMapping("/filtered")
    @Operation(summary = "Возвращает список всех моделей данных," +
            " удовлетворяющих переданным параметрам")

    @ApiResponse(responseCode = "200",
            description = "List of models received!",
            content = {@Content(
                    schema = @Schema(defaultValue = """
                            id: 0
                            name: Aboba.pdf
                            type: application/pdf
                            size: 224438
                            createDate: 2023-11-25 20:48:23
                            updateDate: 2023-11-25 20:48:23
                            downloadURL: /api/download?id=0\s"""), mediaType = "*/*")})

    @ApiResponse(responseCode = "404",
            description = "No such files with this parameters in data base!",
            content = {@Content(
                    schema = @Schema(defaultValue = "info" + ":" +
                            " No such files with this parameters in data base!"),
                    mediaType = "application/json")})

    public ResponseEntity<List<FileDTO>> getModelsByParams(
            @RequestParam(required = false)
            String name,

            @RequestParam(required = false)
            String[] type,

            @RequestParam(required = false, defaultValue = "2024-01-01T12:00:00")
            String lowerDateTime,

            @RequestParam(required = false, defaultValue = "2024-01-01T12:01:00")
            String upperDateTime,

            HttpServletRequest request) {

        Map<String, String[]> requestParams = request.getParameterMap();

        List<FileDTO> fileModels = service.getModelsByParams(requestParams);

        return new ResponseEntity<>(fileModels, HttpStatus.OK);

    }

    @GetMapping(value = "/download", produces = MediaType.ALL_VALUE)
    @Operation(summary = "Скачивание одного файла или нескольких архивом." +
            " Поле archiveName никак не влияет на одиночный файл." +
            " Даже для архива оно опционально.")

    @ApiResponse(responseCode = "200",
            description = "Content downloaded!",
            content = {@Content(
                    schema = @Schema(defaultValue = """
                            accept-ranges: bytes
                            connection: keep-alive
                            content-disposition: attachment; Aboba.pdf
                            content-length: 224438  content-type: application/pdf
                            date: Sat,25 Nov 2023 16:37:27 GMT
                            keep-alive: timeout=60\s"""), mediaType = "*/*")})

    @ApiResponse(responseCode = "404",
            description = "No such files with this IDs inside DB ",
            content = {@Content(
                    schema = @Schema(defaultValue = "info " + ":" +
                            " No such files with id 5, 13 and 8 inside DB!"),
                    mediaType = "application/json")})

    public ResponseEntity<Resource> downloadFilesByID(
            @RequestParam
            @PathVariable int[] id,
            @RequestParam(defaultValue = "nameless", required = false)
            String archiveName,
            HttpServletRequest request
    ) {

        Map<String, String[]> idMap = request.getParameterMap();

        if (idMap.isEmpty())
            throw new NoSuchFileException("Indicate at least one ID!");

        ZipSeparator separator = service.downloadByID(idMap);

        if (separator.isZip()) {

            if (archiveName != null && !archiveName.isEmpty())
                separator.setName(archiveName);

            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=" + separator.getName())
                    .contentType(MediaType.parseMediaType(separator.getContentType()))
                    .body(new ByteArrayResource(separator.getSerialFile()));

        }

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + separator.getName())
                .contentType(MediaType.parseMediaType(separator.getContentType()))
                .body(new ByteArrayResource(separator.getSerialFile()));

    }

    @PatchMapping(value = "/patch/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Обновление файла c указанным Id")

    @ApiResponse(responseCode = "200",
            description = "File updated successfully!",
            content = {@Content(
                    schema = @Schema(defaultValue = """
                            id: 0
                            name: Aboba.pdf
                            type: application/pdf
                            size: 224438
                            createDate: 2023-11-25 20:48:23
                            updateDate: 2023-11-29 17:33:05
                            downloadURL: /api/download?id=0\s"""), mediaType = "*/*")})

    @ApiResponse(responseCode = "404",
            description = "No such files with id 0 inside DB ",
            content = {@Content(
                    schema = @Schema(defaultValue = "info" + ":" +
                            " No such files with id 0 inside DB!"),
                    mediaType = "application/json")})

    public ResponseEntity<String> patchFileById(
            @PathVariable int id,
            @RequestBody MultipartFile file) {

        byte[] fileBytes;

        try {

            fileBytes = file.getBytes();

        } catch (IOException e) {

            return new ResponseEntity<>(
                    "Bytes reading failed!", HttpStatus.NOT_FOUND);
        }

        String responseMessage =
                service.patchFileById(id, file, fileBytes);

        return new ResponseEntity<>(responseMessage, HttpStatus.OK);

    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Удаление файла с указанным Id")

    @ApiResponse(responseCode = "200",
            description = "File delete successfully!")

    @ApiResponse(responseCode = "404",
            description = "No such file with id 0 inside DB ",
            content = {@Content(
                    schema = @Schema(defaultValue = "info" + ":" +
                            " No such file with id 0 inside DB!"),
                    mediaType = "application/json")})

    public ResponseEntity<String> deleteFileById(
            @PathVariable int id) {

        String responseMessage =
                service.deleteFileById(id);

        return new ResponseEntity<>(responseMessage, HttpStatus.OK);

    }
}