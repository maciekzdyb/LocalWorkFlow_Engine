package inz.lwe_v103;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
/**
 * Created by Maciek on 10.11.2016.
 */
public class zip {

    public static String exc;
    public static final int BUFFER_SIZE = 8192; //2048
    private static String parentPath = "";
    private static String TAG= zip.class.getName().toString();

    public static void zipFile(ZipOutputStream zipOutputStream, String sourcePath) throws IOException {
        File files = new File(sourcePath);
        File[] filelist = files.listFiles();

        String entryPath="";
        BufferedInputStream input;
        for(File file : filelist) {
            if (file.isDirectory()){
                zipFile(zipOutputStream,file.getPath());
            }
            else {
                byte data[] = new byte[BUFFER_SIZE];
                FileInputStream fileInputStream = new FileInputStream(file.getPath());
                input = new BufferedInputStream(fileInputStream,BUFFER_SIZE);
                entryPath = file.getAbsolutePath().replace(parentPath,"");

                ZipEntry entry = new ZipEntry(entryPath);
                zipOutputStream.putNextEntry(entry);

                int count;
                while ((count = input.read(data,0,BUFFER_SIZE)) !=-1) {
                    zipOutputStream.write(data,0,count);
                }
                input.close();
            }
        }
    }

    public static boolean zip( String sourcePath, String destinationPath, String destinationFileName, Boolean includeParentFolder)  {
        new File(destinationPath ).mkdirs();
        FileOutputStream fileOutputStream ;
        ZipOutputStream zipOutputStream =  null;
        try{
            if (!destinationPath.endsWith("/")) destinationPath+="/";
            String destination = destinationPath + destinationFileName;
            File file = new File(destination);
            if (!file.exists()) file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            zipOutputStream =  new ZipOutputStream(new BufferedOutputStream(fileOutputStream));
            if (includeParentFolder)
                parentPath=new File(sourcePath).getParent() + "/";
            else
                parentPath=sourcePath;
            zipFile(zipOutputStream, sourcePath);
        }
        catch (IOException ioe){
            Log.d(TAG,ioe.getMessage());
            return false;
        }finally {
            if(zipOutputStream!=null)
                try {
                    zipOutputStream.close();
                } catch(IOException e) {
                    // ToDo
                }
        }
        return true;
    }

    public static Boolean unzip(String sourceFile, String destinationFolder){
        ZipInputStream zipInputStream =null;
        try{
            zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(sourceFile)));
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((ze = zipInputStream.getNextEntry())!=null){
                String fileName = ze.getName();
                fileName = fileName.substring(fileName.indexOf("/")+1);
                File file = new File(destinationFolder,fileName);
                File dir = ze.isDirectory() ? file : file.getParentFile();

                if (!dir.isDirectory() && !dir.mkdirs()){
                    exc = "Invalid path: " + dir.getAbsolutePath();
                }
                if (ze.isDirectory()) continue;
                FileOutputStream fout = new FileOutputStream(file);
                try{
                    while ((count = zipInputStream.read(buffer)) != -1)
                        fout.write(buffer,0,count);
                } finally {
                    fout.close();
                }
            }
        }
        catch (IOException ex){
            exc = ex.toString();
            return false;
        } finally {
            if(zipInputStream!=null)
                try {
                    zipInputStream.close();
                } catch (IOException ex){
                    exc= ex.toString();
                }
        }
        return true;
    }

    public static void mkDir(String path){
        File newFile = new File(path);
        newFile.mkdir();
    }

    public static void delete(File file){
        if (file.isDirectory()){
            for(File child : file.listFiles())
                delete(child);
        }
        file.delete();
    }

    public static void renameFile(String originalPath, String newPath){
        File origin = new File(originalPath);
        File newF = new File(newPath);
        origin.renameTo(newF);
    }

    public static boolean fileExists(String dir, String file){
        File directory = new File(dir);
        for(File child : directory.listFiles()){
            if(child.getName().equals(file)){
                return true;
            }
        }
        return false;
    }
}

