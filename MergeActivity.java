package inz.lwe_v103;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MergeActivity extends AppCompatActivity {

    File dir;

    Button buttonSend;

    Button refreschAct;
    Button checkEmails;
    Button buttonTransform;
    Button buttonClear;
    ImageView image;
    TextView description;
    TableLayout tableLayout;
    TableRow tableRow;
    int filesNo = 6;
    boolean[] presentDoc = new boolean[filesNo];
    String mailTo;
    private String tempPath;

    private void deleteFolder(){
        SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String dir = prefs.getString("DIR","");
        File fdir = new File(dir);
        zip.delete(fdir);
        Toast.makeText(this,"Folder deleted...",Toast.LENGTH_SHORT).show();
    }


    private String CutExp(String str){
        if (str== null) return null;
        int pos = str.lastIndexOf(".");
        if (pos == -1) return  str;
        return str.substring(0,pos);
    }

    public String lastChar(String str){
        if (str== null) return null;
        int pos = str.length()-1;
        if (pos == -1) return  str;
        return str.substring(str.length()-1);
    }

    public void updatePresentDoc(){
        File mergeDir = new File(tempPath);
        File[] mergeFiles = mergeDir.listFiles();
        for(File file : mergeFiles) {
            String fileName = CutExp(file.getName());
            int no = Integer.parseInt(lastChar(fileName));
            presentDoc[no]=true;
        }
    }

    private void mail(String from, String psd, String to, String subject, String message, String path,String folder,File file) {
        SendEmail sendEmail = new SendEmail(this, from, psd, to, subject, message, path, folder, file);
        sendEmail.execute();
    }

    public void generateXSL(String src, String dest, String xsl){
        xslt1.generate(this,src,dest,xsl);
    }

    public void checkMergeFolder(String path){
        File tmpPath = Environment.getExternalStoragePublicDirectory("documents/LWE/tmp");
        File[] tmpPathList = tmpPath.listFiles();

        for (File dir : tmpPathList){
            File[] MINDdir = dir.listFiles();
            for (File doks : MINDdir){
                if (doks.getName().startsWith("dok")){
                    String dokName = doks.getName();
                    String src = doks.getPath();
                    File fsrc = new File(src);
                    String dest = path+"/merge/"+dokName;
                    File fdest = new File(dest);
                    fsrc.renameTo(fdest);
                }
            }
        }

    }

    public String CutExpr(String str) {
        if (str == null) return null;
        int pos = str.lastIndexOf(".");
        if (pos == -1) return str;
        return str.substring(0, pos);
    }

    public void checkEmails(String path, String folder, String host,String port,String from,String fromPass){
        UpdateEmails updateEmails = new UpdateEmails(this,path,folder,host,port,from,fromPass);
        updateEmails.execute();

        File zipPath = Environment.getExternalStoragePublicDirectory("documents/LWE/ZIP");
        File fdestFolder = Environment.getExternalStoragePublicDirectory("documents/LWE/tmp");
        File[] zipPathList = zipPath.listFiles();

        for(File file : zipPathList){
            String sourcefile = file.getPath();
            String folderName = CutExpr(file.getName());
            //String destFolder = path+"/tmp/"+folderName;
            String destFolder = fdestFolder.getPath()+"/"+folderName;
            zip.mkDir(destFolder);
            zip.unzip(sourcefile,destFolder);
        }
        checkMergeFolder(path);
    }

    public void refreshActivity(){
        finish();
        startActivity(getIntent());
    }


    private void setSeen(String folder, String host, String port, String from, String fromPass){
        SeenClass seenClass = new SeenClass(this,folder,host,port,from,fromPass);
        seenClass.execute();
    }

    private void copyFile(String inputPath, String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath + inputFile);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;

        }  catch (FileNotFoundException fnfe1) {
            //Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            //Log.e("tag", e.getMessage());
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merge);

        buttonSend = (Button) findViewById(R.id.button3Send);

        refreschAct = (Button) findViewById(R.id.buttonRefresh);
        buttonTransform = (Button) findViewById(R.id.buttonMergeSend);
        checkEmails = (Button) findViewById(R.id.buttonCheckEmail);
        buttonClear = (Button) findViewById(R.id.buttonClearMerge);
        final SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        final String path = prefs.getString("DIR","");
        dir= new File(path);
        final String folder = prefs.getString("DIRNAME","");
        final String host = prefs.getString("HOST","");
        final String port = prefs.getString("PORT","");
        final String from = prefs.getString("EMAIL","");
        final String fromPass = prefs.getString("PASS","");

        checkMergeFolder(path);

        tempPath=path+"/merge";
        String joinNo = prefs.getString("JoinNo","");
        filesNo = Integer.parseInt(joinNo);

        for (int a=0; a<6; a++){
            presentDoc[a] = false;
        }
        updatePresentDoc();

        tableLayout = (TableLayout) findViewById(R.id.mergeTableLayout);
        tableLayout.setColumnStretchable(0,true);
        tableLayout.setColumnStretchable(1,true);
        int counter =0;
        for(int a = 1; a<filesNo+1; a++){
            tableRow = new TableRow(this);
            image = new ImageView(this);
            if (presentDoc[a]) {
                image.setImageResource(R.drawable.ok);
                counter++;
            }
            else image.setImageResource((R.drawable.not));
            description = new TextView(this);
            description.setText("part "+a+"of "+filesNo);
            description.setTextSize(20);
            description.setGravity(Gravity.CENTER);

            tableRow.addView(image);
            tableRow.addView(description);

            tableLayout.addView(tableRow);
        }
        if (counter!=filesNo){
            buttonTransform.setVisibility(View.GONE);
        }
        else {
            buttonTransform.setVisibility(View.VISIBLE);
        }

        checkEmails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File directory = new File(path+"/ZIP");
                zip.delete(directory);
                zip.mkDir(path+"/ZIP");

                checkEmails(path,folder,host,port,from,fromPass);
            }
        });

        buttonTransform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String xsltPath;
                String sourcePath;
                String resultPath;
                if(filesNo==2){
                    File fxslt = Environment.getExternalStoragePublicDirectory("documents/LWE/tmp/"+folder+"/services/merge_2.xsl");
                    xsltPath= fxslt.getPath();

                    File from1 = Environment.getExternalStoragePublicDirectory("documents/LWE/merge/dok_1.xml");
                    File to1 =Environment.getExternalStoragePublicDirectory("documents/LWE/tmp/"+folder+"/dok_1.xml");
                    from1.renameTo(to1);

                    File from2 = Environment.getExternalStoragePublicDirectory("documents/LWE/merge/dok_2.xml");
                    File to2 =Environment.getExternalStoragePublicDirectory("documents/LWE/tmp/"+folder+"/dok_2.xml");
                    from2.renameTo(to2);

                    File fsource=to1;
                    sourcePath=fsource.getPath();
                    File fResult = Environment.getExternalStoragePublicDirectory("documents/LWE/tmp/"+folder+"/dok.xml");
                    resultPath=fResult.getPath();
                    generateXSL(sourcePath,resultPath,xsltPath);
                    mailTo = prefs.getString("Jmail","");
                    zip.delete(to1);
                    zip.delete(to2);
                }
                if(filesNo==4){
                    File fxslt = Environment.getExternalStoragePublicDirectory("documents/LWE/tmp/"+folder+"/services/merge_4.xsl");
                    xsltPath= fxslt.getPath();

                    File from1 = Environment.getExternalStoragePublicDirectory("documents/LWE/merge/dok_2_1.xml");
                    File to1 =Environment.getExternalStoragePublicDirectory("documents/LWE/tmp/"+folder+"/dok_2_1.xml");
                    from1.renameTo(to1);

                    File from2 = Environment.getExternalStoragePublicDirectory("documents/LWE/merge/dok_2_2.xml");
                    File to2 =Environment.getExternalStoragePublicDirectory("documents/LWE/tmp/"+folder+"/dok_2_2.xml");
                    from2.renameTo(to2);

                    File from3 = Environment.getExternalStoragePublicDirectory("documents/LWE/merge/dok_2_3.xml");
                    File to3 =Environment.getExternalStoragePublicDirectory("documents/LWE/tmp/"+folder+"/dok_2_3.xml");
                    from3.renameTo(to3);

                    File from4 = Environment.getExternalStoragePublicDirectory("documents/LWE/merge/dok_2_4.xml");
                    File to4 =Environment.getExternalStoragePublicDirectory("documents/LWE/tmp/"+folder+"/dok_2_4.xml");
                    from4.renameTo(to4);

                    File fsource=to1;
                    sourcePath=fsource.getPath();
                    File fResult = Environment.getExternalStoragePublicDirectory("documents/LWE/tmp/"+folder+"/dok_2.xml");
                    resultPath=fResult.getPath();
                    generateXSL(sourcePath,resultPath,xsltPath);
                    mailTo = prefs.getString("Jmail","");
                    zip.delete(to1);
                    zip.delete(to2);
                    zip.delete(to3);
                    zip.delete(to4);
                }
                if(filesNo==5){
                    File fxslt = Environment.getExternalStoragePublicDirectory("documents/LWE/tmp/"+folder+"/services/merge_5.xsl");
                    xsltPath= fxslt.getPath();

                    File from1 = Environment.getExternalStoragePublicDirectory("documents/LWE/merge/dok_1_1.xml");
                    File to1 =Environment.getExternalStoragePublicDirectory("documents/LWE/tmp/"+folder+"/dok_1_1.xml");
                    from1.renameTo(to1);

                    File from2 = Environment.getExternalStoragePublicDirectory("documents/LWE/merge/dok_1_2.xml");
                    File to2 =Environment.getExternalStoragePublicDirectory("documents/LWE/tmp/"+folder+"/dok_1_2.xml");
                    from2.renameTo(to2);

                    File from3 = Environment.getExternalStoragePublicDirectory("documents/LWE/merge/dok_1_3.xml");
                    File to3 =Environment.getExternalStoragePublicDirectory("documents/LWE/tmp/"+folder+"/dok_1_3.xml");
                    from3.renameTo(to3);

                    File from4 = Environment.getExternalStoragePublicDirectory("documents/LWE/merge/dok_1_4.xml");
                    File to4 =Environment.getExternalStoragePublicDirectory("documents/LWE/tmp/"+folder+"/dok_1_4.xml");
                    from4.renameTo(to4);

                    File from5 = Environment.getExternalStoragePublicDirectory("documents/LWE/merge/dok_1_5.xml");
                    File to5 =Environment.getExternalStoragePublicDirectory("documents/LWE/tmp/"+folder+"/dok_1_5.xml");
                    from5.renameTo(to5);

                    File fsource=to1;
                    sourcePath=fsource.getPath();
                    File fResult = Environment.getExternalStoragePublicDirectory("documents/LWE/tmp/"+folder+"/dok_1.xml");
                    resultPath=fResult.getPath();
                    generateXSL(sourcePath,resultPath,xsltPath);

                    mailTo = prefs.getString("Jmail","");
                    zip.delete(to1);
                    zip.delete(to2);
                    zip.delete(to3);
                    zip.delete(to4);
                    zip.delete(to5);
                }
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String source = path+"/tmp/"+folder;
                String filename = folder+".zip";
                String fullPath = path+"/"+filename;
                zip.zip(source, path, filename, true);
                mail(from, fromPass, mailTo, folder, "with best regards...", fullPath, folder, dir);
                setSeen(folder,host,port,from,fromPass);
            }
        });

        refreschAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshActivity();
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFolder();
            }
        });
    }
}
