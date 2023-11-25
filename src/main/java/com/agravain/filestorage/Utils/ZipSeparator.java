package com.agravain.filestorage.Utils;

public class ZipSeparator {

    private byte[] serialFile;

    private boolean isZip;

    private String contentType;



    private String name;

    public String getContentType() {

        return contentType;
    }

    public ZipSeparator setContentType(String contentType) {

        this.contentType = contentType;

        return this;
    }

    public byte[] getSerialFile() {

        return serialFile;
    }

    public ZipSeparator setSerialFile(byte[] serialFile) {

        this.serialFile = serialFile;

        return this;
    }

    public boolean isZip() {

        return isZip;
    }

    public ZipSeparator setIsZip(boolean zip) {

        isZip = zip;

        return this;
    }

    public String getName() {

        return name;
    }

    public ZipSeparator setName(String name) {

        this.name = name;

        return this;
    }
}
