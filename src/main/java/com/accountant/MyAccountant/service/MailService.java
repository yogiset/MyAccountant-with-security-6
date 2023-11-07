package com.accountant.MyAccountant.service;

import com.accountant.MyAccountant.entity.User;
import com.accountant.MyAccountant.jwt.JwtUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender javaMailSender;
    private final JwtUtil jwtUtil;

    @Async
    public void sendActivationEmail(User user) {
        log.info("inside sendActivationEmail");
        String token = jwtUtil.generateToken(user.getEmail(),user.getRole(),user.getName());
        String verificationUrl = "https://myaccountant-backend-production.up.railway.app/user/register/accountVerification/" + token;
        String verificationUrl2 = "http:/localhost:8082/user/register/accountVerification/" + token;
        String loginUrl = "https://myaccounta-n.vercel.app/login";
        String loginUrl2 = "http://localhost:3000/login";
        String subject = "Please Activate your Account";

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom("MyAccountant@gmail.com");
            messageHelper.setTo(user.getEmail());
            messageHelper.setSubject(subject);
            String htmlMsg = "<p><b>Thank you for signing up to Myaccountant</b></p>" +
                    "<p>Please click on the below URL to activate your account:</p>" +
                    "<a href='" + verificationUrl2 +"'>Activate Account</a>" +
                    "<p>Please click on the below URL to redirect to login page:</p>" +
                    "<a href='" + loginUrl2 +"'>Go to Login Page</a>";
            mimeMessage.setContent(htmlMsg, "text/html");
        };

        try {
            javaMailSender.send(messagePreparator);
            log.info("Activation email sent!!");
        } catch (MailException e) {
            log.error("Exception occurred when sending mail", e);
            throw new IllegalStateException("Exception occurred when sending mail to " + user.getEmail() + ": " + e.getMessage());
        }
    }

    @Async
    public void forgotMail(String email, String subject, String newPassword) throws MessagingException {
        log.info("inside forgotMail");

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom("MyAccountant@gmail.com");
            helper.setTo(email);
            helper.setSubject(subject);
            String htmlMsg = "<p><b>Your Login details for MyAccountant Management System</b><br><b>Email: </b> " + email + " <br><b>Password: </b> " + newPassword + "<br><a href=\"http://localhost:3000/login\">Click here to login</a></p>" + "Don't forget to change your password, thank you for using our service";
           // String htmlMsg = "<p><b>Your Login details for MyAccountant Management System</b><br><b>Email: </b> " + email + " <br><b>Password: </b> " + newPassword + "<br><a href=\"https://myaccounta-n.vercel.app/login\">Click here to login</a></p>" + "Don't forget to change your password, thank you for using our service";
            mimeMessage.setContent(htmlMsg, "text/html");
        };

        try {
            javaMailSender.send(messagePreparator);
            log.info("Forgot password email sent!!");
        } catch (MailException e) {
            log.error("Exception occurred when sending mail", e);
            throw new IllegalStateException("Exception occurred when sending mail to " + email + ": " + e.getMessage());
        }
    }
}
