package com.agravain.filestorage.Utils;

public class ZipSeparator {

private byte [] serialFile;

private boolean isZip;

private String contentType;

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
}
