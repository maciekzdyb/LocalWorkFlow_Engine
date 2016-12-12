package inz.lwe_v103;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Created by Maciek on 29.11.2016.
 */

public class xslt1 {

    public static void generate(Context con, String source, String destination, String xslt){

        String xsltPath = xslt;
        String sourcePath=source;
        String resultPath= destination;

        TransformerFactory tFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = tFactory.newTransformer(new StreamSource(
                    new File(xsltPath)));
            transformer.transform(new StreamSource(new File(sourcePath)),
                    new StreamResult(new File(resultPath)));
        } catch (Exception e) {
            Toast.makeText(con,e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
}

