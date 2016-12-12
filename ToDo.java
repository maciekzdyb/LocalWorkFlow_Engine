package inz.lwe_v103;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ToDo extends AppCompatActivity {

    private List<String> fileList = new ArrayList<String>();

    public void newAct(){
        Intent intent = new Intent(this, CurrentActivity.class);
        startActivity(intent);
    }

    public String CutExp(String str){
        if (str== null) return null;
        int pos = str.lastIndexOf(".");
        if (pos == -1) return  str;
        return str.substring(0,pos);
    }

    public boolean nameDoesntExist(String name){
        for (int a=0; a<fileList.size(); a++){
            if (fileList.get(a).equals(name)){
                return false;
            }
        }
        return true;
    }

    public static Activity td;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        MainActivity.ma.finish();
        td = this;
        final File  path = Environment.getExternalStoragePublicDirectory("documents/LWE");
        File zipPath = Environment.getExternalStoragePublicDirectory("documents/LWE/ZIP");
        final File[] files = zipPath.listFiles();                                                   // list of all zip files!!!

        fileList.clear();

        for (File file : files){
            String fileName=CutExp(file.getName());
            String[] name = fileName.split("_");
            if (nameDoesntExist(name[0])){
                fileList.add(name[0]);
            }
        }
        ListView listView = (ListView) findViewById(R.id.listview_todo);
        ArrayAdapter<String> dirList = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,fileList);
        listView.setAdapter(dirList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String activity_name = (String) adapterView.getItemAtPosition(i);

                //String activity_name = (String) adapterView.getItemAtPosition(i);
                Toast.makeText(view.getContext(),"wybrano: "+activity_name,Toast.LENGTH_SHORT).show();
                SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("DIRNAME",activity_name);
                editor.apply();
                String Dir = path.toString();
                zip.mkDir(Dir+"/tmp");
                int counter =0;
                for (File zipFile : files){
                    String dirName = CutExp(zipFile.getName());
                    if(zipFile.getName().startsWith(activity_name)){
                        if (counter==0){
                            String[] tab = dirName.split("_");
                            zip.mkDir(Dir+"/tmp/"+tab[0]);
                            zip.unzip(Dir+"/ZIP/"+zipFile.getName(),Dir+"/tmp/"+tab[0]);
                        }
                        else{
                            zip.mkDir(Dir+"/tmp/"+dirName);
                            zip.unzip(Dir+"/ZIP/"+zipFile.getName(),Dir+"/tmp/"+dirName);
                        }
                        counter++;
                    }
                }
                newAct();
                //System.exit(0);
            }
        });
    }
}
