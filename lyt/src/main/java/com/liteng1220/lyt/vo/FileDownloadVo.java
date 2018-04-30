package com.liteng1220.lyt.vo;

public class FileDownloadVo {
    public String url;
    public String filename;
    public String filePath;

    public FileDownloadVo() {
    }

    public FileDownloadVo(String url, String filename, String filePath) {
        this.url = url;
        this.filename = filename;
        this.filePath = filePath;
    }
}
