package com.azharkova.writemesound.data;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by aniou_000 on 04.08.2015.
 */
public class FileManager {
    private static FileManager ourInstance = new FileManager();

    public static FileManager getInstance() {
        return ourInstance;
    }

    private FileManager() {
    }

    public boolean saveFile(String fileUrl) {
        return false;
    }

    public boolean move(String fileUrl, String collection) throws FileNotFoundException,IOException {
        String oldUrl = fileUrl.substring(0, fileUrl.lastIndexOf("/"));
        String newUrl = collection;
        if (!newUrl.equals(oldUrl)) {
            String newFileUrl = newUrl + "/" + getOldName(fileUrl);
            File newFile = new File(newFileUrl);
            File oldFile = new File(fileUrl);

            if (!newFile.exists()) {
                FileInputStream in = new FileInputStream(oldFile);
                FileOutputStream out = new FileOutputStream(newFile);

                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();
                in = null;

                // write the output file
                out.flush();
                out.close();
                out = null;

                oldFile.delete();

            }
        }
        return false;
    }

public boolean deleteFile(String fileUrl)
{
    File file = new File(fileUrl);
    if (file.exists()) {
        file.delete();
    }
    return true;
}


    public boolean renameFile(String fileUrl,String newName)
    {
        String oldName =FileManager.getInstance().getOldName(fileUrl);
        File oldFile = new File(fileUrl);
        String newUrl = fileUrl.substring(0, fileUrl.lastIndexOf("/") + 1) + newName + fileUrl.substring(fileUrl.lastIndexOf("."));
        File newFile = new File(newUrl);
        if (!newUrl.equals(fileUrl) && !newUrl.equals("")) {
            if (newFile.exists()) {
                int index = 0;
                do {
                    index++;
                    newUrl = fileUrl.substring(0, fileUrl.lastIndexOf("/") + 1) + newName + index + fileUrl.substring(fileUrl.lastIndexOf("."));
                    newFile = new File(newUrl);
                } while (newFile.exists());
            }

            Log.i("WRITE_ME_SOUND", newUrl);


            oldFile.renameTo(newFile);
            return true;
        }
        return false;
    }

    public String getOldName(String fileUrl) {

        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    }
}
