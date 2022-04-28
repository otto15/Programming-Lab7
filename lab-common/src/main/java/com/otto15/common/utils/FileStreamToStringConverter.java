package com.otto15.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class is used for input stream conversion.
 */
public final class FileStreamToStringConverter {

    private FileStreamToStringConverter() {

    }

    public static String fileStreamToString(File file) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        bufferedReader.readLine();
        String line = bufferedReader.readLine();
        while (line != null) {
            stringBuilder.append(line.trim());
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }
}
