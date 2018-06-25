package org.hoss.util;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;

public class GitPropertiesReader {

    public static void main(String [] args) {
        if (Files.exists(FileSystems.getDefault().getPath(".","git.properties"), LinkOption.NOFOLLOW_LINKS)) { System.out.println("found it");
            System.out.println("Found it");
        } else {
            System.out.println("NOT FOUND");
        }

    }
}
