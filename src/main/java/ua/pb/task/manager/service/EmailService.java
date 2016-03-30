package ua.pb.task.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ua.pb.task.manager.model.EmailMessage;
import ua.pb.task.manager.repository.UserRepository;

/**
 * Created by Mednikov on 30.03.2016.
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserRepository repository;

    @Autowired
    private Environment env;

    /*
     * Send email notification to admin
     * @email - new user email, admin need to define role for this user
     */
    public void approveUser(String email) {
        String adminEmail = repository.findMainAdmin().getMainEmail();
        String subject = env.getProperty("message.subject");
        String body = env.getProperty("message.body") + email;
        EmailMessage message = new EmailMessage(adminEmail, subject, body);
        sendMail(message);
    }

    public void sendMail(EmailMessage email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email.getTo());
        message.setSubject(email.getSubject());
        message.setText(email.getText());
        javaMailSender.send(message);
    }
}
