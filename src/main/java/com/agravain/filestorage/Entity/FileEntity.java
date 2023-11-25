package com.agravain.filestorage.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@Table(name = "files_liquibase")
public class FileEntity {

    public FileEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "size")
    private long size;

    @Column(name = "createDate")
    private LocalDateTime createDate;

    @Column(name = "updateDate")
    private LocalDateTime updateDate;

    @Column(name = "file")
    private byte[] file;

    public long getSize() {

        return size;

    }

    public FileEntity setSize(long size) {

        this.size = size;

        return this;

    }

    public String getName() {

        return name;

    }

    public byte[] getFile() {

        return file;

    }

    public FileEntity setFile(byte[] file) {

        this.file = file;

        return this;

    }

    public String getType() {

        return type;

    }

    public FileEntity setName(String name) {

        this.name = name;

        return this;

    }

    public FileEntity setType(String type) {

        this.type = type;

        return this;

    }

    public int getId() {

        return id;

    }

    public void setId(int id) {

        this.id = id;

    }

    public LocalDateTime getCreateDate() {

        return createDate;

    }

    public FileEntity setCreateDate(LocalDateTime createDate) {

        this.createDate =
                formatLocalDateTime(createDate);

        return this;

    }

    public LocalDateTime getUpdateDate() {

        return updateDate;

    }

    public FileEntity setUpdateDate(LocalDateTime updateDate) {

        this.updateDate =
                formatLocalDateTime(updateDate);

        return this;

    }

    public LocalDateTime formatLocalDateTime(LocalDateTime reference) {

        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss");

        String middleResult =
                reference.format(formatter);

        return LocalDateTime.parse(middleResult, formatter);

    }

}