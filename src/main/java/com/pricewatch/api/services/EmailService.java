package com.pricewatch.api.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String remetenteAutenticado;
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarEmailSimples(String para, String assunto, String texto) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(remetenteAutenticado);

            message.setTo(para);
            message.setSubject(assunto);
            message.setText(texto);

            mailSender.send(message);
            System.out.println("[Email] Notificação enviada com sucesso para: " + para);
        } catch (Exception e) {
            System.err.println("[Email Erro] Falha ao enviar e-mail para " + para + " | Erro: " + e.getMessage());
            e.printStackTrace(); // Boa prática para ver o stacktrace completo se falhar
        }
    }
}