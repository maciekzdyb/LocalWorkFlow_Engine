package inz.lwe_v103;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import static android.R.attr.autoAdvanceViewId;
import static android.R.attr.id;

public class TableActivity extends AppCompatActivity {

    TextView test;
    TextView name, surname;
    EditText grade;
    TableLayout tableLayout;
    TableRow tableRow;
    Student[] students;
    String filename;
    String dir;
    Student[] studentsList = new Student[20];
    int studentsNo, listSize;
    List<EditText> allEdits = new ArrayList<EditText>();
    Button update;
    String[] editTextLists = new String[allEdits.size()];


    private void getList(String path){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(path));
            NodeList studentNodeList = doc.getElementsByTagName("student");
            studentsNo=studentNodeList.getLength();
            for (int i=0; i<studentNodeList.getLength(); i++){
                Node el =  studentNodeList.item(i);
                NodeList nodeData = el.getChildNodes();
                for (int j=0; j<nodeData.getLength();j++){
                    Node data = nodeData.item(j);
                    if (data.getNodeName().equals("name")){
                        studentsList[i]=new Student();
                        studentsList[i].setName(data.getTextContent());
                    }
                    if (data.getNodeName().equals("surname")){
                        studentsList[i].setSurname(data.getTextContent());
                    }
                }
            }
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            e.toString();
        }
    }

    private void updateList(String path){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(path));
            NodeList studentNodeList = doc.getElementsByTagName("student");
            studentsNo=studentNodeList.getLength();
            for (int i=0; i<studentNodeList.getLength(); i++){
                Node el =  studentNodeList.item(i);
                NodeList nodeData = el.getChildNodes();
                for (int j=0; j<nodeData.getLength();j++){
                    Node data = nodeData.item(j);
                    if (data.getNodeName().equals("grade"))
                    {
                        data.setTextContent(allEdits.get(i).getText().toString());
                    }
                }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(path));
            transformer.transform(source, result);
        }
        catch (ParserConfigurationException | SAXException | IOException |
                TransformerException e) {
            e.toString();
        }
    }

    public void updateFile(){
        SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        dir = preferences.getString("DIR","");
        dir+="/tmp";
        String folder = preferences.getString("DIRNAME","");
        String path = dir+"/"+folder;
        File directory = new File(path);
        File[] files = directory.listFiles();
        for (int i=0; i<files.length; i++){
            if (files[i].getName().startsWith("dok")){
                filename = files[i].getName();
            }
        }
        test = (TextView) findViewById(R.id.textViewTable);
        test.setText(filename);
        updateList(path+"/"+filename);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        dir = preferences.getString("DIR","");
        dir+="/tmp";

        String folder = preferences.getString("DIRNAME","");
        String path = dir+"/"+folder;
        File directory = new File(path);
        File[] files = directory.listFiles();
        for (int i=0; i<files.length; i++){
            if (files[i].getName().startsWith("dok")){
                filename = files[i].getName();
            }
        }
        test = (TextView) findViewById(R.id.textViewTable);
        test.setText(filename);
        getList(path+"/"+filename);

        update = (Button) findViewById(R.id.buttonUpdate);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFile();
                update.setVisibility(View.GONE);
            }
        });

        tableLayout = (TableLayout) findViewById(R.id.table1);
        tableLayout.setColumnStretchable(0,true);
        tableLayout.setColumnStretchable(1,true);
        tableLayout.setColumnStretchable(2,true);

        for (int a=0; a<studentsNo; a++){
            tableRow = new TableRow(this);
            name = new TextView(this);
            name.setText(studentsList[a].getName());
            name.setTextSize(12);
            name.setGravity(Gravity.CENTER);

            surname = new TextView((this));
            surname.setText(studentsList[a].getSurname());
            surname.setTextSize(12);
            surname.setGravity(Gravity.CENTER);

            grade = new EditText(this);
            grade.setTextSize(12);
            grade.setGravity(Gravity.CENTER);
            //grade.setId(a);
            allEdits.add(grade);

            tableRow.addView(name);
            tableRow.addView(surname);
            tableRow.addView(grade);
            tableLayout.addView(tableRow);
        }
    }
}
