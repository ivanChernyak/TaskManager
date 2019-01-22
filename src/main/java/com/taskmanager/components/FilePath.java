package com.taskmanager.components;

import java.io.File;

/**
 * This enum is responsible for the paths to the data files.
 */
public enum FilePath {
    DATA("/data/data.txt");
//    DATA(new File("data/data.txt").getAbsolutePath());

    private String path;

    FilePath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
