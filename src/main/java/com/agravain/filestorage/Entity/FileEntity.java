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

    public void setSize(long size) {

        this.size = size;
    }

    public String getName() {

        return name;
    }

    public byte[] getFile() {

        return file;
    }

    public void setFile(byte[] file) {

        this.file = file;
    }

    public String getType() {

        return type;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setType(String type) {

        this.type = type;
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

    public void setCreateDate(LocalDateTime createDate) {

        this.createDate =
                formatLocalDateTime(createDate);
    }

    public LocalDateTime getUpdateDate() {

        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {

        this.updateDate =
                formatLocalDateTime(updateDate);
    }

    public LocalDateTime formatLocalDateTime(LocalDateTime reference) {

        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss");

        String middleResult =
                reference.format(formatter);

        return LocalDateTime.parse(middleResult, formatter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileEntity entity = (FileEntity) o;
        return Objects.equals(name, entity.name) && Objects.equals(type, entity.type);
    }

}