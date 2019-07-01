/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.huberb.csvcompare.war.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.enterprise.context.RequestScoped;

/**
 * Manage {@link File}: create temp file, write file, delete file.
 *
 * @author berni3
 */
@RequestScoped
public class FileManager {

    public static class FileElement {

        private File f;

        public File getF() {
            return f;
        }
    }

    public FileElement createFileElement(String suffix) throws IOException {
        File f = File.createTempFile("csvread_", suffix);
        FileElement fe = new FileElement();
        fe.f = f;
        return fe;
    }

    public void writeToFileElement(FileElement fe, String content) throws IOException {
        Path p = fe.f.toPath();
        Files.write(p, content.getBytes(Charset.forName("UTF-8")));
    }

    public void deleteFileElement(FileElement fe) {
        File f = fe.f;
        f.delete();
    }
}
