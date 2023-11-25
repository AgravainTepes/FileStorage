package com.agravain.filestorage.DTO;

import com.agravain.filestorage.Entity.FileEntity;

import java.time.LocalDateTime;

public class FileDTO {

    private int id;

    private String name;

    private String type;

    private long size;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private String downloadURL;


    public int getId() {

        return id;
    }


    public void setId(int id) {

        this.id = id;
    }


    public String getName() {

        return name;
    }


    public FileDTO setName(String name) {

        this.name = name;

        return this;
    }


    public String getType() {

        return type;
    }


    public FileDTO setType(String type) {

        this.type = type;

        return this;
    }


    public long getSize() {

        return size;
    }


    public FileDTO setSize(int size) {

        this.size = size;

        return this;
    }

    public LocalDateTime getCreateDate() {

        return createDate;
    }



    public FileDTO setCreateDate(LocalDateTime createDate) {

        this.createDate =
                createDate;

        return this;
    }

    public LocalDateTime getUpdateDate() {

        return updateDate;
    }


    public FileDTO setUpdateDate(LocalDateTime updateDate) {

        this.updateDate =
                updateDate;

        return this;
    }

    public String getDownloadURL() {

        return downloadURL;
    }


    public FileDTO setDownloadURL(String downloadURL) {

        this.downloadURL =
                downloadURL;

        return this;
    }

    public void entityToDTO(FileEntity model) {

        this.id =
                model.getId();

        this.name =
                model.getName();

        this.size =
                model.getSize();

        this.createDate =
                model.getCreateDate();

        this.updateDate =
                model.getUpdateDate();

        this.type =
                model.getType();

        this.downloadURL =
                "/api/download?id=" + model.getId();
    }

}
