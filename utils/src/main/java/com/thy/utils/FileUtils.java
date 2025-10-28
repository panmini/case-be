package com.thy.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.InputStream;

public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public static InputStream getFileAsInputStream(String filePath) {
        try {
            Resource resource = new DefaultResourceLoader().getResource(filePath);
            if (resource.exists()) {
                return resource.getInputStream();
            }
            return null;
        } catch (Exception e) {
            logger.error("load file $filePath by inputStream error", e);
            return null;
        }
    }
}
