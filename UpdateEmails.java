package inz.lwe_v103;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;


/**
 * Created by Maciek on 12.12.2016.
 */
//Context final String host, final String port, final String userName, final String password
public class UpdateEmails extends AsyncTask<Void,Void,Void> {

    private Session session;
    private Context context;
    private String folder;
    private String saveDirectory;
    private String host;
    private String port;
    private String from;
    private String fromPass;
    private ProgressDialog progressDialog;
    private String exception;
    private String attachment_filename;

    public static void update(){

    }

    public UpdateEmails(Context context, String path, String folder, String host,String port,String from,String fromPass){
        this.context = context;
        this.saveDirectory = path+"/ZIP";
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
        //Toast.makeText(context,"Messages updated",Toast.LENGTH_LONG).show();

    }

    @Override
    protected Void doInBackground(Void... params) {
        Properties properties = new Properties();
        //server:
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        //SSL:
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.socketFactory.port", String.valueOf(port));

        Session session = Session.getDefaultInstance(properties);

        try {
            //connects:
            Store store = session.getStore("imaps");
            store.connect(host, from, fromPass);
            //open the inbox folder
            Folder folderInbox = store.getFolder("inbox");
            folderInbox.open(Folder.READ_ONLY);

            Flags seen = new Flags(Flags.Flag.SEEN);
            FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
            Message arrayMessages[] = folderInbox.search(unseenFlagTerm); // for check only unseen messages

            for (int i = 0; i < arrayMessages.length; i++) {
                Message message = arrayMessages[i];
                MimeMessage message1 = (MimeMessage) arrayMessages[i];
                Address[] fromAddress = message.getFrom();
                String from = fromAddress[0].toString();
                String subject = message.getSubject();
                String sentDate = message.getSentDate().toString();
                String messageId = message1.getMessageID();

                String contentType = message.getContentType();
                String messageContent = "";

                //store attachment filename, separated by comma:
                String attachFiles = "";

                if (subject.equals(folder) && contentType.contains("multipart")) {          //change MIND when tests ends!!!
                    Multipart multiPart = (Multipart) message.getContent();
                    int numberOfParts = multiPart.getCount();
                    for (int partCount = 0; partCount < numberOfParts; partCount++) {
                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                            String fileName = part.getFileName();
                            attachFiles += fileName + ", ";
                            int pos = fileName.lastIndexOf(".");
                            String name = fileName.substring(0, pos);
                            name += "_" + sentDate;
                            String fileName_date = name + ".zip";
                            part.saveFile(saveDirectory + File.separator + fileName);
                            zip.renameFile(saveDirectory + File.separator + fileName, saveDirectory + File.separator + fileName_date);
                            //part.saveFile(messageDir + File.separator + fileName);
                        } else {
                            messageContent = part.getContent().toString();
                        }
                    }

                    if (attachFiles.length() > 1) {
                        attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
                        attachment_filename = attachFiles;
                    }
                    //message.setFlag(Flags.Flag.SEEN,true);
                } else if (contentType.contains("text/plain") || contentType.contains("text/html")) {
                    Object content = message.getContent();
                    if (content != null) {
                        messageContent = content.toString();
                    }
                }
            }
            folderInbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            exception = "1" + ex.toString();
        } catch (MessagingException ex) {
            exception = "2: " + ex.toString();
        } catch (IOException ex) {
            exception = "3: " + ex.toString();
        } catch (Exception ex) {
            exception = "4" + ex.toString();
        }
        return null;
    }
}
