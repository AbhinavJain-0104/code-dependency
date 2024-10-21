package com.example.developer.util;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {
    public static void unpackZipFile(File zipFile, File destDir) throws IOException {
        try (ZipFile zip = new ZipFile(zipFile)) {
            zip.getEntries().asIterator().forEachRemaining(entry -> {
                try {
                    File destFile = new File(destDir, entry.getName());
                    if (entry.isDirectory()) {
                        destFile.mkdirs();
                    } else {
                        destFile.getParentFile().mkdirs();
                        try (InputStream in = zip.getInputStream(entry);
                             OutputStream out = new FileOutputStream(destFile)) {
                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = in.read(buffer)) > 0) {
                                out.write(buffer, 0, len);
                            }
                        }
                    }
                } catch (IOException e) {
                    // Handle exception
                    e.printStackTrace();
                }
            });
        }
    }


    public static String readFileContent(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    public static String getFileExtension(String filePath) {
        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return filePath.substring(lastDotIndex + 1);
        }
        return "";
    }
}
