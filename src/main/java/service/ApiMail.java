package service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class ApiMail {

    public static void main(String[] args) throws IOException, UnknownHostException, MessagingException {
        // TODO code application logic here
        String destino = "carlos.sanchez@vallegrande.edu.pe";
        String asunto = "Registro exitoso al sistema";
        String cuerpo = "Felicitaciones, usted se ha registrado exitosamente a nuestro servicio. Agradecemos su espera.";

        notificar(destino, asunto, cuerpo);

    }

    public static void notificar(String destino, String asunto, String cuerpo) throws UnknownHostException, IOException, MessagingException {
        String remitente = "skyfoodventas@gmail.com";
        String clave = "oyqbxyqobpynekrg";
        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.user", remitente);
        props.put("mail.smtp.clave", clave);
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            String nombreHost = InetAddress.getLocalHost().getHostName();
            Date dateTime = new Date();
            SimpleDateFormat f = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
            String fechaHora = f.format(dateTime);

            message.setFrom(new InternetAddress(remitente));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destino));
            message.setSubject(asunto);
            BodyPart texto = new MimeBodyPart();
            message.setContent("<h1>SkyFood: Sistema de Ventas y Compras</h1>", "text/html");
            texto.setContent(message.getContent() + "<h2>Saludos: Carlos" + " " + "<h2 style=\"color: red\" >" + " Nueva actividad en tu cuenta:<h2/> </h2>"
                    + "<h3> Fecha y Hora:  <h3 style=\"color: red\">" + fechaHora + "<h3/> </h3>"
                    + "<h3> Dirección IP:  <h3 style=\"color: red\">" + ip + "<h3/> </h3>"
                    + "<h3> localhost  <h3 style=\"color: red\">" + nombreHost + "<h3/> </h3>",
                    "text/html");
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto);
            message.setContent(multiParte);
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", remitente, clave);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        } catch (MessagingException me) {
            me.printStackTrace();   //Si se produce un error
            System.out.println("Errorcito en: " + me.getMessage());
        }

    }

}
