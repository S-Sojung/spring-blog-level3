package shop.mtcoding.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.blog.dto.admin.AdminReq.AdminMailReq;
import shop.mtcoding.blog.handler.ex.CustomApiException;

@RequiredArgsConstructor
@Service
public class AdminService {

    @Autowired
    private final JavaMailSender mailSender;

    public void mailSend(AdminMailReq adminMailReq) {
        // System.out.println("테스트 : " + to);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(adminMailReq.getEmail());
        message.setSubject(adminMailReq.getTitle());
        message.setText(adminMailReq.getMessage());
        try {
            mailSender.send(message);
            System.out.println("테스트 : 전달 완료");

        } catch (Exception e) {
            System.out.println("테스트 : 전달 실패");
            e.printStackTrace();
            throw new CustomApiException("메일 전달 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
