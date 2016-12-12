package inz.lwe_v103;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class CurrentActivity extends AppCompatActivity {

    TextView activityId;
    TextView activityTo;
    TextView activityTo1;
    TextView activityTo2;
    TextView activityTo3;
    TextView activityTo4;
    TextView activityTransition;
    TextView activityAttachment;
    TextView nextActivity;
    Context context;
    Button btnSend, btnEdit, btnClear;

    public void newAct1(){
        Intent intent = new Intent(this, TableActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current);

        ToDo.td.finish();

        context = this;
        activityId = (TextView) findViewById(R.id.textViewId);
        activityTo =(TextView) findViewById(R.id.textViewTo);
        activityTo1 =(TextView) findViewById(R.id.textViewTo1);
        activityTo2 =(TextView) findViewById(R.id.textViewTo2);
        activityTo3 =(TextView) findViewById(R.id.textViewTo3);
        activityTo4 =(TextView) findViewById(R.id.textViewTo4);
        activityTransition = (TextView) findViewById(R.id.textViewTransition);
        activityAttachment = (TextView) findViewById(R.id.textViewAttachment);
        nextActivity = (TextView) findViewById(R.id.textViewNextActivity);
        btnSend = (Button) findViewById(R.id.buttonSend);
        btnEdit = (Button) findViewById(R.id.buttonEdit);
        btnClear = (Button) findViewById(R.id.buttonClear);

        String currentActivityId = getActivityId();
        final String transition = getTransition(currentActivityId);
        activityId.setText(currentActivityId);
        activityTransition.setText(transition);
        final String[] nextActivities = new String[15];
        final String[] participantsEmail = new String[15];

        switch (transition){
            case "Get Document":
                String nextAct = getNextActivity(currentActivityId);
                nextActivities[0]=nextAct;
                nextActivity.setText(nextAct);
                participantsEmail[0] = getReceiverEmail(nextAct);
                activityTo.setText(participantsEmail[0]);
                activityTo1.setVisibility(View.GONE);
                activityTo2.setVisibility(View.GONE);
                activityTo3.setVisibility(View.GONE);
                activityTo4.setVisibility(View.GONE);
                activityAttachment.setText(getAttachmentFilename());
                btnEdit.setVisibility(View.GONE);

                break;
            case "Split2":
                //nextActivity.setVisibility(View.GONE);
                String[] nextActs =(String[]) getNextActivitis(currentActivityId);
                nextActivity.setText(nextActs[0]+"; "+nextActs[1]);
                nextActivities[0]=nextActs[0];
                nextActivities[1]=nextActs[1];
                participantsEmail[0]=getReceiverEmail(nextActs[0]);
                participantsEmail[1]=getReceiverEmail(nextActs[1]);
                activityTo.setText(participantsEmail[0]);
                activityTo1.setText(participantsEmail[1]);
                activityTo2.setVisibility(View.GONE);
                activityTo3.setVisibility(View.GONE);
                activityTo4.setVisibility(View.GONE);
                activityAttachment.setText(getAttachmentFilename());
                btnEdit.setVisibility(View.GONE);
                break;
            case "Split5":
                String[] nextActs5 =(String[]) getNextActivitis(currentActivityId);
                nextActivity.setText(nextActs5[0]+"; "+nextActs5[4]);
                nextActivities[0]=nextActs5[0];
                nextActivities[1]=nextActs5[1];
                nextActivities[2]=nextActs5[2];
                nextActivities[3]=nextActs5[3];
                nextActivities[4]=nextActs5[4];
                participantsEmail[0]=getReceiverEmail(nextActs5[0]);
                participantsEmail[1]=getReceiverEmail(nextActs5[1]);
                participantsEmail[2]=getReceiverEmail(nextActs5[2]);
                participantsEmail[3]=getReceiverEmail(nextActs5[3]);
                participantsEmail[4]=getReceiverEmail(nextActs5[4]);
                activityTo.setText(participantsEmail[0]);
                activityTo1.setText(participantsEmail[1]);
                activityTo2.setText(participantsEmail[2]);
                activityTo3.setText(participantsEmail[3]);
                activityTo4.setText(participantsEmail[4]);
                activityAttachment.setText(getAttachmentFilename());
                btnEdit.setVisibility(View.GONE);
                break;
            case "Split4":
                String[] nextActs4 =(String[]) getNextActivitis(currentActivityId);
                nextActivity.setText(nextActs4[0]+"; "+nextActs4[3]);
                nextActivities[0]=nextActs4[0];
                nextActivities[1]=nextActs4[1];
                nextActivities[2]=nextActs4[2];
                nextActivities[3]=nextActs4[3];
                participantsEmail[0]=getReceiverEmail(nextActs4[0]);
                participantsEmail[1]=getReceiverEmail(nextActs4[1]);
                participantsEmail[2]=getReceiverEmail(nextActs4[2]);
                participantsEmail[3]=getReceiverEmail(nextActs4[3]);
                activityTo.setText(participantsEmail[0]);
                activityTo1.setText(participantsEmail[1]);
                activityTo2.setText(participantsEmail[2]);
                activityTo3.setText(participantsEmail[3]);
                activityTo4.setVisibility(View.GONE);
                activityAttachment.setText(getAttachmentFilename());
                btnEdit.setVisibility(View.GONE);
                break;
            case "Update List":
                String nextActU1 = getNextActivity(currentActivityId);
                nextActivities[0]=nextActU1;
                nextActivity.setText(nextActU1);
                participantsEmail[0] = getReceiverEmail(nextActU1);
                activityTo.setText(participantsEmail[0]);
                activityTo1.setVisibility(View.GONE);
                activityTo2.setVisibility(View.GONE);
                activityTo3.setVisibility(View.GONE);
                activityTo4.setVisibility(View.GONE);
                activityAttachment.setText(getAttachmentFilename());
                break;
            case "Join4":
                String nextActJ4 = getNextActivity(currentActivityId);
                nextActivities[0]=nextActJ4;
                updateXSL(nextActivities[0]);
                participantsEmail[0] = getReceiverEmail(nextActJ4);

                activityTo.setText(participantsEmail[0]);
                nextActivity.setText(nextActJ4);
                SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("J4mail",participantsEmail[0]);
                editor.apply();
                String dir = prefs.getString("DIR","");
                String folder = prefs.getString("DIRNAME","");
                String source = dir+"/tmp/"+folder;


                String dokName="";
                File sourceFolder = new File(source);
                File[] files = sourceFolder.listFiles();
                for (File file : files){
                    if(file.getName().startsWith("dok")){
                        dokName =file.getName();
                    }
                }

                String src = source+"/"+dokName;
                zip.mkDir(dir+"/merge");
                File fsrc = new File(src);
                String dest = dir+"/merge/"+dokName;
                File fdest = new File(dest);
                fsrc.renameTo(fdest);

                break;
            case "Join5":
                String nextActJ5 = getNextActivity(currentActivityId);
                nextActivities[0]=nextActJ5;
                updateXSL(nextActivities[0]);
                participantsEmail[0] = getReceiverEmail(nextActJ5);

                activityTo.setText(participantsEmail[0]);
                nextActivity.setText(nextActJ5);
                SharedPreferences prefs5 = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor5 = prefs5.edit();
                editor5.putString("Jmail",participantsEmail[0]);
                editor5.apply();
                String dir5 = prefs5.getString("DIR","");
                String folder5 = prefs5.getString("DIRNAME","");
                String source5 = dir5+"/tmp/"+folder5;


                String dokName5="";
                File sourceFolder5 = new File(source5);
                File[] files5 = sourceFolder5.listFiles();
                for (File file : files5){
                    if(file.getName().startsWith("dok")){
                        dokName5 =file.getName();
                    }
                }

                String src5 = source5+"/"+dokName5;
                zip.mkDir(dir5+"/merge");
                File fsrc5 = new File(src5);
                String dest5 = dir5+"/merge/"+dokName5;
                File fdest5 = new File(dest5);
                fsrc5.renameTo(fdest5);

                break;
            case "Join2":
                String nextActJ2 = getNextActivity(currentActivityId);
                nextActivities[0]=nextActJ2;
                updateXSL(nextActivities[0]);
                participantsEmail[0] = getReceiverEmail(nextActJ2);

                activityTo.setText(participantsEmail[0]);
                nextActivity.setText(nextActJ2);
                SharedPreferences prefs2 = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = prefs2.edit();
                editor2.putString("Jmail",participantsEmail[0]);
                editor2.apply();
                String dir2 = prefs2.getString("DIR","");
                String folder2 = prefs2.getString("DIRNAME","");
                String source2 = dir2+"/tmp/"+folder2;

                String dokName2="";
                File sourceFolder2 = new File(source2);
                File[] files2 = sourceFolder2.listFiles();
                for (File file : files2){
                    if(file.getName().startsWith("dok")){
                        dokName2 =file.getName();
                    }
                }

                String src2 = source2+"/"+dokName2;
                zip.mkDir(dir2+"/merge");
                File fsrc2 = new File(src2);
                String dest2 = dir2+"/merge/"+dokName2;
                File fdest2 = new File(dest2);
                fsrc2.renameTo(fdest2);
                break;
            case "Sequency2":
                String nextActS2 = getNextActivity(currentActivityId);
                nextActivities[0]=nextActS2;
                nextActivity.setText(nextActS2);
                participantsEmail[0] = getReceiverEmail(nextActS2);
                activityTo.setText(participantsEmail[0]);
                activityTo1.setVisibility(View.GONE);
                activityTo2.setVisibility(View.GONE);
                activityTo3.setVisibility(View.GONE);
                activityTo4.setVisibility(View.GONE);
                activityAttachment.setText(getAttachmentFilename());
                btnEdit.setVisibility(View.GONE);
                break;
        }

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newAct1();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                    String dir = preferences.getString("DIR","");
                    File file = new File(dir);
                    String host = preferences.getString("HOST","");
                    String port = preferences.getString("PORT","");
                    String folder = preferences.getString("DIRNAME","");
                    String from = preferences.getString("EMAIL","");
                    String fromPass = preferences.getString("PASS","");
                    String filename = folder+".zip";
                    String source = dir+"/tmp/"+folder;
                    String path = dir+"/"+filename;

                    switch (transition){
                        case "Get Document":
                            if (updateXSL(nextActivities[0])) {
                                zip.zip(source, dir, filename, true);
                                mail(from, fromPass, participantsEmail[0], folder, "with best regards...", path, folder, file);
                                setSeen(folder,host,port,from,fromPass);
                            }
                            else sendMessage(context);
                            break;
                        case "Split2":
                            //                         1 - move dok.xml to parent dir
                            String src = source+"/dok.xml";
                            File fsrc = new File(src);
                            String dest = dir+"/dok.xml";
                            File fdest = new File(dest);
                            fsrc.renameTo(fdest);
                            //                         2 - generate and send first attachment

                            if (updateXSL(nextActivities[0])) {
                                String dest1 = source + "/dok_1.xml";
                                String xslt = source + "/services/group1.xsl";
                                gen(dest, dest1, xslt);                   //xsl transform
                                zip.zip(source, dir, filename, true);
                                mail(from, fromPass, participantsEmail[0], folder, "with best regards...", path, folder, file);
                            }
                            else sendMessage(context);
                            //                         3 - generate and send second attachment
                            if (updateXSL(nextActivities[1])) {
                                String xslt1 = source + "/services/group2.xsl";
                                String dest11 = source + "/dok_2.xml";
                                gen(dest, dest11, xslt1);
                                File fToDel = new File(source + "/dok_1.xml");
                                zip.delete(fToDel);
                                String tmpDir = dir + "/tmp";
                                zip.mkDir(tmpDir);
                                zip.zip(source, tmpDir, filename, true);
                                mail(from, fromPass, participantsEmail[1], folder, "with best regards...", tmpDir+"/"+filename, folder, file);
                            }
                            else sendMessage(context);
                            setSeen(folder,host,port,from,fromPass);
                            break;
                        case "Split5":
                            //                         1 - move dok.xml to parent dir
                            String src5 = source+"/dok_1.xml";
                            File fsrc5 = new File(src5);
                            String dest5 = dir+"/dok_1.xml";
                            File fdest5 = new File(dest5);
                            fsrc5.renameTo(fdest5);
                            if (updateXSL(nextActivities[0])) {
                                String dest1 = source + "/dok_1_1.xml";
                                String xslt = source + "/services/lab_group1.xsl";
                                gen(dest5, dest1, xslt);                   //xsl transform
                                zip.zip(source, dir, filename, true);
                                mail(from, fromPass, participantsEmail[0], folder, "with best regards...", path, folder, file);
                            }
                            else sendMessage(context);
                            if (updateXSL(nextActivities[1])) {
                                String xslt1 = source + "/services/lab_group2.xsl";
                                String dest11 = source + "/dok_1_2.xml";
                                gen(dest5, dest11, xslt1);
                                File fToDel = new File(source + "/dok_1_1.xml");
                                zip.delete(fToDel);
                                String tmpDir = dir + "/tmp2";
                                zip.mkDir(tmpDir);
                                zip.zip(source, tmpDir, filename, true);
                                mail(from, fromPass, participantsEmail[1], folder, "with best regards...", tmpDir+"/"+filename, folder, file);
                            }
                            else sendMessage(context);
                            if (updateXSL(nextActivities[2])) {
                                String xslt1 = source + "/services/lab_group3.xsl";
                                String dest11 = source + "/dok_1_3.xml";
                                gen(dest5, dest11, xslt1);
                                File fToDel = new File(source + "/dok_1_2.xml");
                                zip.delete(fToDel);
                                String tmpDir = dir + "/tmp3";
                                zip.mkDir(tmpDir);
                                zip.zip(source, tmpDir, filename, true);
                                mail(from, fromPass, participantsEmail[2], folder, "with best regards...", tmpDir+"/"+filename, folder, file);
                            }
                            else sendMessage(context);
                            if (updateXSL(nextActivities[3])) {
                                String xslt1 = source + "/services/lab_group4.xsl";
                                String dest11 = source + "/dok_1_4.xml";
                                gen(dest5, dest11, xslt1);
                                File fToDel = new File(source + "/dok_1_3.xml");
                                zip.delete(fToDel);
                                String tmpDir = dir + "/tmp4";
                                zip.mkDir(tmpDir);
                                zip.zip(source, tmpDir, filename, true);
                                mail(from, fromPass, participantsEmail[3], folder, "with best regards...", tmpDir+"/"+filename, folder, file);
                            }
                            else sendMessage(context);
                            if (updateXSL(nextActivities[4])) {
                                String xslt1 = source + "/services/lab_group4.xsl";
                                String dest11 = source + "/dok_1_5.xml";
                                gen(dest5, dest11, xslt1);
                                File fToDel = new File(source + "/dok_1_4.xml");
                                zip.delete(fToDel);
                                String tmpDir = dir + "/tmp5";
                                zip.mkDir(tmpDir);
                                zip.zip(source, tmpDir, filename, true);
                                mail(from, fromPass, participantsEmail[4], folder, "with best regards...", tmpDir+"/"+filename, folder, file);
                            }
                            else sendMessage(context);
                            setSeen(folder,host,port,from,fromPass);
                            break;
                        case "Split4":
                            //                         1 - move dok.xml to parent dir
                            String src4 = source+"/dok_2.xml";
                            File fsrc4 = new File(src4);
                            String dest4 = dir+"/dok_2.xml";
                            File fdest4 = new File(dest4);
                            fsrc4.renameTo(fdest4);
                            if (updateXSL(nextActivities[0])) {
                                String dest1 = source + "/dok_2_1.xml";
                                String xslt = source + "/services/lab_group1.xsl";
                                gen(dest4, dest1, xslt);                   //xsl transform
                                zip.zip(source, dir, filename, true);
                                mail(from, fromPass, participantsEmail[0], folder, "with best regards...", path, folder, file);
                            }
                            else sendMessage(context);
                            if (updateXSL(nextActivities[1])) {
                                String xslt1 = source + "/services/lab_group2.xsl";
                                String dest11 = source + "/dok_2_2.xml";
                                gen(dest4, dest11, xslt1);
                                File fToDel = new File(source + "/dok_2_1.xml");
                                zip.delete(fToDel);
                                String tmpDir = dir + "/tmp2";
                                zip.mkDir(tmpDir);
                                zip.zip(source, tmpDir, filename, true);
                                mail(from, fromPass, participantsEmail[1], folder, "with best regards...", tmpDir+"/"+filename, folder, file);
                            }
                            else sendMessage(context);
                            if (updateXSL(nextActivities[2])) {
                                String xslt1 = source + "/services/lab_group3.xsl";
                                String dest11 = source + "/dok_2_3.xml";
                                gen(dest4, dest11, xslt1);
                                File fToDel = new File(source + "/dok_2_2.xml");
                                zip.delete(fToDel);
                                String tmpDir = dir + "/tmp3";
                                zip.mkDir(tmpDir);
                                zip.zip(source, tmpDir, filename, true);
                                mail(from, fromPass, participantsEmail[2], folder, "with best regards...", tmpDir+"/"+filename, folder, file);
                            }
                            else sendMessage(context);
                            if (updateXSL(nextActivities[3])) {
                                String xslt1 = source + "/services/lab_group4.xsl";
                                String dest11 = source + "/dok_2_4.xml";
                                gen(dest4, dest11, xslt1);
                                File fToDel = new File(source + "/dok_2_3.xml");
                                zip.delete(fToDel);
                                String tmpDir = dir + "/tmp4";
                                zip.mkDir(tmpDir);
                                zip.zip(source, tmpDir, filename, true);
                                mail(from, fromPass, participantsEmail[3], folder, "with best regards...", tmpDir+"/"+filename, folder, file);
                            }
                            else sendMessage(context);
                            setSeen(folder,host,port,from,fromPass);
                            break;
                        case "Update List":
                            if (updateXSL(nextActivities[0])) {
                                zip.zip(source, dir, filename, true);
                                mail(from, fromPass, participantsEmail[0], folder, "with best regards...", path, folder, file);
                            }
                            else sendMessage(context);
                            setSeen(folder,host,port,from,fromPass);
                            break;
                        case "Join4":
                            //String tmpDir = dir+"/tmp";
                            SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("JoinNo","4");
                            editor.apply();
                            //setSeen(folder,host,port,from,fromPass);
                            startMergeAct();
                            break;
                        case "Join5":
                            SharedPreferences prefsJ5 = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editorJ5 = prefsJ5.edit();
                            editorJ5.putString("JoinNo","5");
                            editorJ5.apply();
                            //setSeen(folder,host,port,from,fromPass);
                            startMergeAct();
                            break;
                        case "Join2":
                            SharedPreferences prefsJ2 = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editorJ2 = prefsJ2.edit();
                            editorJ2.putString("JoinNo","2");
                            editorJ2.apply();
                            //setSeen(folder,host,port,from,fromPass);
                            startMergeAct();
                            break;
                        case "Sequency2":
                            if (updateXSL(nextActivities[0])) {
                                zip.zip(source, dir, filename, true);
                                mail(from, fromPass, participantsEmail[0], folder, "with best regards...", path, folder, file);
                                setSeen(folder,host,port,from,fromPass);
                            }
                            else sendMessage(context);
                            break;
                    }

            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFolder();
            }
        });

    }

    private void deleteFolder(){
        SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String dir = prefs.getString("DIR","");
        File fdir = new File(dir);
        zip.delete(fdir);
        Toast.makeText(this,"Folder deleted...",Toast.LENGTH_SHORT).show();
    }

    public void startMergeAct(){
        Intent intent = new Intent(this, MergeActivity.class);
        startActivity(intent);
    }

    public String getActivityId(){
        myDOMParser myParser = new myDOMParser();
        SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String dir = prefs.getString("DIR","");
        String folder = prefs.getString("DIRNAME","");

        File dir1 = new File(dir+"/tmp");
        File[] dirList = dir1.listFiles();
        String folder1 = dirList[0].getName();

        myParser.set_directory(dir+"/tmp/"+folder1);
        myParser.set_File("path.xml");

        myParser.set_tagName("xpdl:ExtendedAttribute");
        myParser.set_nameAttributeToFind("Name");
        myParser.set_valueAttributeToFind("currentActivity");
        myParser.set_nameAttributeToRead("Value");
        return  myParser.compareAndRead_xmlValue();
    }

    public String getTransition(String activityId){
        myDOMParser myParser = new myDOMParser();
        SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String dir = prefs.getString("DIR","");
        String folder = prefs.getString("DIRNAME","");

        File dir1 = new File(dir+"/tmp");
        File[] dirList = dir1.listFiles();
        String folder1 = dirList[0].getName();

        myParser.set_directory(dir+"/tmp/"+folder1);
        myParser.set_File("path.xml");

        myParser.set_tagName("xpdl:Activity");
        myParser.set_nameAttributeToFind("Id");
        myParser.set_valueAttributeToFind(activityId);
        myParser.set_nameAttributeToRead("Name");
        return myParser.compareAndRead_xmlValue();
    }

    public String getNextActivity(String currentActivity){
        myDOMParser myParser = new myDOMParser();
        SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String dir = prefs.getString("DIR","");
        String folder = prefs.getString("DIRNAME","");

        File dir1 = new File(dir+"/tmp");
        File[] dirList = dir1.listFiles();
        String folder1 = dirList[0].getName();

        myParser.set_directory(dir+"/tmp/"+folder1);
        myParser.set_File("path.xml");

        myParser.set_tagName("xpdl:Transition");
        myParser.set_nameAttributeToFind("From");
        myParser.set_valueAttributeToFind(currentActivity);
        myParser.set_nameAttributeToRead("To");
        return myParser.compareAndRead_xmlValue();
    }

    public String[] getNextActivitis(String currentActivity){
        myDOMParser myParser = new myDOMParser();
        SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String dir = prefs.getString("DIR","");
        String folder = prefs.getString("DIRNAME","");

        File dir1 = new File(dir+"/tmp");
        File[] dirList = dir1.listFiles();
        String folder1 = dirList[0].getName();

        myParser.set_directory(dir+"/tmp/"+folder1);
        myParser.set_File("path.xml");

        myParser.set_tagName("xpdl:Transition");
        myParser.set_nameAttributeToFind("From");
        myParser.set_valueAttributeToFind(currentActivity);
        myParser.set_nameAttributeToRead("To");
        return myParser.compareAndRead_xmlValues();
    }

    public  String getReceiverEmail(String nextActivity) {
        myDOMParser myParser = new myDOMParser();
        SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String dir = prefs.getString("DIR","");
        String folder = prefs.getString("DIRNAME","");

        File dir1 = new File(dir+"/tmp");
        File[] dirList = dir1.listFiles();
        String folder1 = dirList[0].getName();

        myParser.set_directory(dir+"/tmp/"+folder1);
        myParser.set_File("path.xml");

        myParser.set_tagName("xpdl:Activity");
        myParser.set_nameAttributeToFind("Id");
        myParser.set_valueAttributeToFind(nextActivity);
        String performerId=myParser.get_PerformerId();
        myParser.set_tagName("xpdl:Participant");
        myParser.set_valueAttributeToFind(performerId);
        return myParser.get_ParticipantEmail(performerId);
    }

    public Boolean updateXSL(String nextAct){
                                                            //Warnnig!!! update in first folder in tmp
        myDOMParser myParser = new myDOMParser();
        SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String dir = prefs.getString("DIR","");
        String folder = prefs.getString("DIRNAME","");

        dir+="/tmp";                                        // merge patch
        myParser.set_directory(dir+"/"+folder);
        myParser.set_File("path.xml");

        myParser.set_tagName("xpdl:ExtendedAttribute");
        myParser.set_nameAttributeToFind("Name");
        myParser.set_valueAttributeToFind("currentActivity");
        if (myParser.update_xpdlValue(nextAct)) return true;
        else return false;
    }

    public String getAttachmentFilename(){
        SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String attach = preferences.getString("DIRNAME","");
        return attach+".zip";
    }

    private void wait3() throws InterruptedException {
        Thread.sleep(3000);
    }

    private void mail(String from, String psd, String to, String subject, String message, String path,String folder,File file) {
        SendEmail sendEmail = new SendEmail(this, from, psd, to, subject, message, path, folder, file);
        sendEmail.execute();
        activityTransition.setText(sendEmail.getStatus().toString());
    }

    public void setSeen(String folder, String host, String port, String from, String fromPass){
        SeenClass seenClass = new SeenClass(this,folder,host,port,from,fromPass);
        seenClass.execute();
    }


    public void gen(String source, String dest, String xslt){
        xslt1.generate(this,source,dest,xslt);
    }

    public void sendMessage(Context context){
        Toast.makeText(context,"Something went wrong...",Toast.LENGTH_SHORT).show();
    }

    public void buttonCloseOnClick(View v){
        System.exit(0);
    }
}
