package com.location.home.device;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileManager {

    final File path =
            Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM + "/HomeLocation/");

    final File file = new File(path, "home.txt");

    public void write(String fileContents) {

        initializeFile();

        FileOutputStream fOut;

        try {

            if (file.length() >= 0)

                fOut = new FileOutputStream(file, true);

            else

                fOut = new FileOutputStream(file);

            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

            if (file.length() >= 0)

            myOutWriter.append(fileContents).append("\n");

            else

            myOutWriter.write(fileContents + "\n");

            myOutWriter.close();

            fOut.flush();
            fOut.close();

        } catch (IOException e) {

            Log.e("running", e.getMessage());

        } catch (Exception e) {

            Log.e("running", e.getMessage());

        }

    }

    public String read() {

        initializeFile();

        String aBuffer = "";

        try {

            FileInputStream fIn = new FileInputStream(file);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
            String aDataRow = "";

            while ((aDataRow = myReader.readLine()) != null) {

                aBuffer += aDataRow;
                aBuffer += "\n";

            }

            myReader.close();

        } catch (Exception e) {

        }

        return aBuffer;
    }

    private void initializeFile() {

        if (!path.exists()) {

            path.mkdirs();
        }

        if (!file.exists())

            try {

                file.createNewFile();

                file.mkdir();

            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    public void deleteFile(){

        file.delete();

        initializeFile();

    }


}
