package com.agravain.filestorage.Utils;

public class ZipSeparator {

    private byte[] serialFile;

    private boolean isZip;

    private String contentType;



    private String name;

    public String getContentType() {

        return contentType;
    }

    public void setContentType(String contentType) {

        this.contentType = contentType;
    }

    public byte[] getSerialFile() {

        return serialFile;
    }

    public void setSerialFile(byte[] serialFile) {

        this.serialFile = serialFile;
    }

    public boolean isZip() {

        return isZip;
    }

    public void setIsZip(boolean zip) {

        isZip = zip;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }
}
