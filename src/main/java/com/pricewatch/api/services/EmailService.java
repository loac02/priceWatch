package com.pricewatch.api.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarEmailSimples(String para, String assunto, String texto) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("nao-responda@pricewatch.com");
            message.setTo(para);
            message.setSubject(assunto);
            message.setText(texto);

            mailSender.send(message);
            System.out.println("[Email] Notificação enviada com sucesso para: " + para);
        } catch (Exception e) {
            System.err.println("[Email Erro] Falha ao enviar e-mail para " + para + " | Erro: " + e.getMessage());
        }
    }
}