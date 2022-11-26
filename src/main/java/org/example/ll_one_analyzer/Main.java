package org.example.ll_one_analyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Main {
    private static final String DEFAULT_FILE_NAME = "input.txt";

    public static void main(final String[] args) {
        ClassLoader classLoader = Main.class.getClassLoader();

        if (args.length > 0) {
            try (InputStream is = classLoader.getResourceAsStream(DEFAULT_FILE_NAME)) {
                if (null == is) {
                    throw new IllegalArgumentException("FILE_NOT_FOUND " + args[0]);
                }
                printInputStream(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try (InputStream is = classLoader.getResourceAsStream(DEFAULT_FILE_NAME)) {
                if (null == is) {
                    throw new IllegalArgumentException("DEFAULT_FILE_NOT_FOUND " + DEFAULT_FILE_NAME);
                }
                printInputStream(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void printInputStream(InputStream is) {

        try (InputStreamReader streamReader =
                     new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}