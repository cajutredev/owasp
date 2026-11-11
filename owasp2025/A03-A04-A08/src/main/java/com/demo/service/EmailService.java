package com.demo.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.time.OffsetDateTime;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ApplicationScoped
public class EmailService {

    private static final Logger LOGGER = LogManager.getLogger(EmailService.class);
    private static final String SUBJECT =
            "Security Alert: Multiple Failed Authentication Attempts";
    private static final String TOKEN_MAILTRAP = "126980a5a538f7c77288c58324bb8949";


    public void sendFailedAuthenticationAlert(
            String accountEmail,
            long failureCount,
            OffsetDateTime eventTimestamp) throws MessagingException {
    
        SmtpConfiguration configuration = SmtpConfiguration.fromEnvironment();
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host", configuration.host());
        properties.setProperty("mail.smtp.port", configuration.port());
        properties.setProperty("mail.smtp.auth", Boolean.toString(configuration.username() != null));
        properties.setProperty("mail.smtp.ssl.enable", Boolean.toString(configuration.ssl()));
        properties.setProperty("mail.smtp.ssl.checkserveridentity", "true");
        properties.setProperty(
                "mail.smtp.starttls.enable",
                Boolean.toString(configuration.startTls()));
        properties.setProperty(
                "mail.smtp.starttls.required",
                Boolean.toString(configuration.startTls()));
        properties.setProperty("mail.smtp.connectiontimeout", "5000");
        properties.setProperty("mail.smtp.timeout", "5000");
        properties.setProperty("mail.smtp.writetimeout", "5000");

        Authenticator authenticator = configuration.username() == null
                ? null
                : new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                configuration.username(), configuration.password());
                    }
                };
        Session session = Session.getInstance(properties, authenticator);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(configuration.fromAddress(), true));
        message.setRecipients(
                Message.RecipientType.TO,
                new InternetAddress[] {new InternetAddress(configuration.adminAddress(), true)});
        message.setSubject(SUBJECT, "UTF-8");
        message.setText("""

                        """.formatted(accountEmail, failureCount, eventTimestamp),
                "UTF-8");
        System.out.println("se va a enviar correo");
        Transport.send(message);

        LOGGER.info("Security alert email delivered for account={}",accountEmail);
    }

    private record SmtpConfiguration(
            String host,
            String port,
            String username,
            String password,
            String fromAddress,
            String adminAddress,
            boolean startTls,
            boolean ssl) {

        private static SmtpConfiguration fromEnvironment() {
            String username = optionalEnvironment("SMTP_USERNAME");
            String password = optionalEnvironment("SMTP_PASSWORD");
            if ((username == null) != (password == null)) {
                throw new IllegalStateException(
                        "SMTP_USERNAME and SMTP_PASSWORD must both be configured or both be empty");
            }

            String port = requiredEnvironment("SMTP_PORT");
            try {
                int portNumber = Integer.parseInt(port);
                if (portNumber < 1 || portNumber > 65535) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException exception) {
                throw new IllegalStateException("SMTP_PORT must be between 1 and 65535", exception);
            }

            boolean startTls = booleanEnvironment("SMTP_STARTTLS", true);
            boolean ssl = booleanEnvironment("SMTP_SSL", false);
            if (startTls && ssl) {
                throw new IllegalStateException("SMTP_STARTTLS and SMTP_SSL cannot both be enabled");
            }

            return new SmtpConfiguration(
                    requiredEnvironment("SMTP_HOST"),
                    port,
                    username,
                    password,
                    requiredEnvironment("SMTP_FROM"),
                    requiredEnvironment("SECURITY_ADMIN_EMAIL"),
                    startTls,
                    ssl);
        }

        private static String requiredEnvironment(String name) {
            String value = optionalEnvironment(name);
            if (value == null) {
                throw new IllegalStateException(name + " is required for alert delivery");
            }
            return value;
        }

        private static String optionalEnvironment(String name) {
            String value = System.getenv(name);
            System.out.println("var env: " + name);
            System.out.println("var env result: " + value);
            return value == null || value.isBlank() ? null : value;
        }

        private static boolean booleanEnvironment(String name, boolean defaultValue) {
            String value = optionalEnvironment(name);
            if (value == null) {
                return defaultValue;
            }
            if ("true".equalsIgnoreCase(value)) {
                return true;
            }
            if ("false".equalsIgnoreCase(value)) {
                return false;
            }
            throw new IllegalStateException(name + " must be true or false");
        }
    }
}
