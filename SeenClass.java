package inz.lwe_v103;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;

/**
 * Created by Maciek on 12.12.2016.
 */

public class SeenClass extends AsyncTask<Void,Void,Void> {

    private Session session;
    private Context context;
    private String folder;
    private String host;
    private String port;
    private String from;
    private String fromPass;
    private ProgressDialog progressDialog;
    private String exception;

    public SeenClass(Context context, String folder, String host,String port,String from,String fromPass){
        this.context = context;
        this.folder = folder;
        this.host = host;
        this.port = port;
        this.from = from;
        this.fromPass = fromPass;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Showing progress dialog while sending email
        progressDialog = ProgressDialog.show(context,"Reading messages","Please wait...",false,false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismissing the progress dialog
        progressDialog.dismiss();
        //Showing a success message
        Toast.makeText(context,"Message mark as seen",Toast.LENGTH_LONG).show();
    }

    @Override
    protected Void doInBackground(Void... params) {

        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.setProperty("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.auth","true");
        properties.setProperty("mail.smtp.socketFactory.port", String.valueOf(port));

        Session session = Session.getDefaultInstance(properties);

        try {
            Store store = session.getStore("imaps");
            store.connect(host,from,fromPass);
            Folder folderInbox = store.getFolder("inbox");
            folderInbox.open(Folder.READ_WRITE);
            //fetch new messages:

            Flags seen = new Flags(Flags.Flag.SEEN);
            FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
            Message arrayMessages[] = folderInbox.search(unseenFlagTerm);

            for (int i =0; i < arrayMessages.length; i++) {
                Message message = arrayMessages[i];
                //MimeMessage message1 = (MimeMessage) arrayMessages[i];
                //Address[] fromAddress = message.getFrom();
                //String from = fromAddress[0].toString();
                String subject = message.getSubject();
                //String sentDate = message.getSentDate().toString();
                //String messageId = message1.getMessageID();

                if (subject.equals(folder)){
                    message.setFlag(Flags.Flag.SEEN,true);
                }
            }
            folderInbox.close(false);
            store.close();
        }
        catch (NoSuchProviderException ex) {
            exception="1"+ ex.toString();
        }
        catch (MessagingException ex) {
            exception = "2: " + ex.toString();
        }

        catch (Exception ex) {
            exception = "4"+ ex.toString();
        }

        return null;
    }
}
