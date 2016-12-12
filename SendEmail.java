package inz.lwe_v103;

/**
 * Created by Maciek on 18.11.2016.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendEmail extends AsyncTask<Void,Void,Void> {

    //Declaring Variables
    private Context context;
    private Session session;

    //Information to send email
    private String email_to;
    private String subject;
    private String message;
    private String filename;
    public String email_from;
    public String from_pass;
    public String folder;
    public File dir;
    //Progressdialog to show while sending email
    private ProgressDialog progressDialog;

    //Class Constructor
    public SendEmail(Context context, String from, String fromPass, String email, String subject, String message, String filename, String folder, File dir){
        //Initializing variables
        this.context = context;
        this.email_to = email;
        this.subject = subject;
        this.message = message;
        this.filename = filename;
        this.email_from = from;
        this.from_pass = fromPass;
        this.folder = folder;
        this.dir = dir;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Showing progress dialog while sending email
        progressDialog = ProgressDialog.show(context,"Sending message","Please wait...",false,false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismissing the progress dialog
        progressDialog.dismiss();
        //Showing a success message
        Toast.makeText(context,"Message Sent",Toast.LENGTH_LONG).show();
        // this.getStatus().toString();
        //zip.delete(dir);
    }

    @Override
    protected Void doInBackground(Void... params) {

        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

         session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email_from, from_pass);
                    }
                });
        try {
            //Creating MimeMessage object
            MimeMessage mm = new MimeMessage(session);
            //Setting sender address
            mm.setFrom(new InternetAddress(email_from));
            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email_to));
            //Adding subject
            mm.setSubject(subject);

            // Create the message part
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(message, "text/html");

            // Now set the actual message
            //messageBodyPart.setText(message);
            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);
            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            //messageBodyPart.setContent(filename,"application/zip");
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            //messageBodyPart.setFileName(filename);
            String fname = folder+".zip";
            messageBodyPart.setFileName(fname);

            multipart.addBodyPart(messageBodyPart);
            // Send the complete message parts
            mm.setContent(multipart);
            //Sending email
            Transport.send(mm);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
