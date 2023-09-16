package wertexpro.fvds.svaroggraphs;

import java.io.File;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Sender2 {

    private String username;
    private String password;
    private Properties props;

    public Sender2(String username, String password) {
        this.username = username;
        this.password = password;


        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "srv.scryp.ru");
        props.put("mail.smtp.ssl.trust", "srv.scryp.ru");
        props.put("mail.smtp.port", "587");
    }

    public void send(String subject, String text, String fromEmail, String toEmail){
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            //от кого
            message.setFrom(new InternetAddress(username));
            //кому
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            //Заголовок письма
            message.setSubject(subject);

           /* BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            messageBodyPart.setText("This is message body");

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            String filename = android.os.Environment.getExternalStorageDirectory().toString() + "/" + "number1";
            System.out.println("File exists : " + new File(filename).exists());
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);*/
            //Содержимое
            MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822");
            message.setText(text);
            /*FileDataSource fds = new FileDataSource(android.os.Environment.getExternalStorageDirectory().toString() + "/" + "number1");
            MimeBodyPart attachment = new MimeBodyPart();
            MimeBodyPart messagePart = new MimeBodyPart();
            attachment.setDataHandler(new DataHandler(fds));
            attachment.setFileName("data.txt");
            messagePart.setText("Hello, NETER");
            Multipart hotmailMP = new MimeMultipart();
            hotmailMP.addBodyPart(attachment);
            hotmailMP.addBodyPart( messagePart );
            message.setContent( hotmailMP );*/

            //Отправляем сообщение
            Transport.send(message);
        } catch (MessagingException e) {
            System.out.println(e);
            System.out.println("ERRRRRROR");
            throw new RuntimeException(e);
        }
    }
}
