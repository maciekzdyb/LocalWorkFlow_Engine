package inz.lwe_v103;
/**
 * Created by Maciek on 09.11.2016.
 */
import android.content.Context;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Header;
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


public class readEmail {
    private String saveDirectory;
    private String exception;
    private String attachment_filename;


    public String getException(){
        return exception;
    }

    public String getAtttachment_filename(){
        return attachment_filename;
    }

    public void setSaveDirectory(String dir){
        this.saveDirectory = dir;
    }

    public String cutExp(String str){
        if (str== null) return null;
        int pos = str.lastIndexOf(".");
        if (pos == -1) return  str;
        return str.substring(0,pos);
    }

    public void downloadEmailAttachments(final String host, final String port, final String userName, final String password) {

        Properties properties = new Properties();
        //server:
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        //SSL:
        properties.setProperty("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.auth","true");
        properties.setProperty("mail.smtp.socketFactory.port", String.valueOf(port));

        Session session = Session.getDefaultInstance(properties);

        try {
            //connects:
            Store store = session.getStore("imaps");
            store.connect(host,userName,password);
            //open the inbox folder
            Folder folderInbox = store.getFolder("inbox");
            folderInbox.open(Folder.READ_ONLY);
            //folderInbox.open(Folder.READ_WRITE);
            //fetch new messages:

//            Message[] arrayMessages = folderInbox.getMessages();       // for check all messages

            Flags seen = new Flags(Flags.Flag.SEEN);
            FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
            Message arrayMessages[] = folderInbox.search(unseenFlagTerm); // for check only unseen messages

            for (int i =0; i < arrayMessages.length; i++) {
                Message message = arrayMessages[i];
                MimeMessage message1 = (MimeMessage) arrayMessages[i];
                Address[] fromAddress = message.getFrom();
                String from = fromAddress[0].toString();
                String subject = message.getSubject();
                String sentDate = message.getSentDate().toString();
                String messageId = message1.getMessageID();

                String contentType = message.getContentType();
                String messageContent ="";

                //store attachment filename, separated by comma:
                String attachFiles = "";

                if (subject.startsWith("MIND00010") && contentType.contains("multipart")){          //change MIND when tests ends!!!
                    Multipart multiPart = (Multipart) message.getContent();
                    int numberOfParts = multiPart.getCount();
                    for (int partCount = 0; partCount < numberOfParts; partCount++) {
                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                            String fileName = part.getFileName();
                            attachFiles+= fileName + ", ";

                            String name = cutExp(fileName);
                            name+="_"+sentDate;
                            String fileName_date = name+".zip";
                            //String messageDir = saveDirectory+"/"+messageId;
                            //zip.mkDir(messageDir);
                            part.saveFile(saveDirectory + File.separator + fileName);
                            zip.renameFile(saveDirectory + File.separator + fileName,saveDirectory + File.separator + fileName_date);
                            //part.saveFile(messageDir + File.separator + fileName);
                        }
                        else {
                            messageContent = part.getContent().toString();
                        }
                    }

                    if (attachFiles.length() >1) {
                        attachFiles = attachFiles.substring(0,attachFiles.length() -2);
                        attachment_filename = attachFiles;
                    }
                    //message.setFlag(Flags.Flag.SEEN,true);
                } else if (contentType.contains("text/plain") || contentType.contains("text/html")){
                    Object content = message.getContent();
                    if (content !=null) {
                        messageContent = content.toString();
                    }
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
        catch (IOException ex) {
            exception = "3: " + ex.toString();
        }
        catch (Exception ex) {
            exception = "4"+ ex.toString();
        }
    }
}

