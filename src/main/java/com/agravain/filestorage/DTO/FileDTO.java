package com.agravain.filestorage.DTO;

import com.agravain.filestorage.FileDataModel.FileDataModel;

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

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public void fileDataModelToDTO(FileDataModel model) {
        this.id = model.getId();
        this.name = model.getName();
        this.size = model.getSize();
        this.createDate = model.getCreateDate();
        this.updateDate = model.getUpdateDate();
        this.type = model.getType();
        this.downloadURL = "localhost:8080/api/download-by-id/" + model.getId();
    }

}
