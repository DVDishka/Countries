package dvdcraft.countries.common;

import java.io.File;

public class FileDir {

    public static boolean fileDirExist(String path, String name) {
        if (path.length() != 0 && path.charAt(path.length() - 1) != '/') {
            path += "/";
        }
        path += name;
        File file = new File(path);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean createDirectory(String path, String name) {
        if (path.length() != 0 && path.charAt(path.length() - 1) != '/') {
            path += "/";
        }
        path += name;
        File directory = new File(path);
        if (directory.exists()) {
            return false;
        }
        if (directory.mkdir()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean createFile(String path, String name) {
        if (path.length() != 0 && path.charAt(path.length() - 1) != '/') {
            path += "/";
        }
        path += name;
        File file = new File(path);
        if (file.exists()) {
            return false;
        }
        try {
            if (!file.createNewFile()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
