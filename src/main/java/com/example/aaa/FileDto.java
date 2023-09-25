package com.example.aaa;

import org.springframework.core.io.Resource;

import java.util.Date;

public class FileDto {
    private String fileName;
    private Date fileDate;
    private String filePath;
    private Resource resource;

    public FileDto(String fileName, Date fileDate, String filePath, Resource resource) {
        this.fileName = fileName;
        this.fileDate = fileDate;
        this.filePath = filePath;
        this.resource = resource;
    }

    public FileDto() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getFileDate() {
        return fileDate;
    }

    public void setFileDate(Date fileDate) {
        this.fileDate = fileDate;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
