package com.cjt.tuesday.config;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class DynamicEmailConfig {

    public JavaMailSender getJavaMailSender(String host, int port, String username, String password, boolean startTls, boolean ssl) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        if (startTls) {
            props.put("mail.smtp.starttls.enable", "true");
        }
        if (ssl) {
            props.put("mail.smtp.ssl.enable", "true");
        }

        return mailSender;
    }
}
