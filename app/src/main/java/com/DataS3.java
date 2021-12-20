package com;

public class DataS3 {

    String path;
    String key;
    String origin;

    public DataS3(){};
    public DataS3(String path, String key, String origin) {
        this.path = path;
        this.key = key;
        this.origin = origin;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}

