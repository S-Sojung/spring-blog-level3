package shop.mtcoding.blog.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import shop.mtcoding.blog.dto.ResponseDto;
import shop.mtcoding.blog.dto.user.UserReq.JoinReqDto;
import shop.mtcoding.blog.dto.user.UserReq.LoginReqDto;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.model.UserRepository;
import shop.mtcoding.blog.service.UserService;

@Controller
public class UserController {

    @Autowired
    private HttpSession session;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }

    @PostMapping("/login")
    // public String login(String username, String password) {
    public String login(LoginReqDto loginReqDto) {
        // 확실히 여기서 다 통과하는지 확인해야함. (길이라던가 한글이라던가...)
        if (loginReqDto.getUsername() == null || loginReqDto.getUsername().isEmpty()) {
            throw new CustomException("username을 작성해주세요");
        }
        if (loginReqDto.getPassword() == null || loginReqDto.getPassword().isEmpty()) {
            throw new CustomException("password를 작성해주세요");
        }
        // controller는 validation 체크하고 , 서비스 요청하고 값전달하면 끝.
        User principal = userService.로그인(loginReqDto);

        session.setAttribute("principal", principal);

        return "redirect:/";

    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "user/joinForm";
    }

    @PostMapping("/join")
    public String join(JoinReqDto joinReqDto) {
        // System.out.println(joinReqDto.getUsername());
        // System.out.println(joinReqDto.getPassword());
        // System.out.println(joinReqDto.getEmail());

        if (joinReqDto.getUsername() == null || joinReqDto.getUsername().isEmpty()) {
            throw new CustomException("username을 작성해주세요");
        }
        if (joinReqDto.getPassword() == null || joinReqDto.getPassword().isEmpty()) {
            throw new CustomException("password를 작성해주세요");
        }
        if (joinReqDto.getEmail() == null || joinReqDto.getEmail().isEmpty()) {
            throw new CustomException("email을 작성해주세요");
        }

        userService.회원가입(joinReqDto);

        return "redirect:/loginForm";
    }

    @GetMapping("/user/updateForm")
    public String updateForm() {
        return "user/updateForm";
    }

    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/user/profileUpdateForm")
    public String profileUpdateForm(Model model) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            return "redirect:/loginForm";
        }
        // 영구히 저장되어 잇는걸 꺼냄을 의미 PS
        User userPS = userRepository.findById(principal.getId());
        model.addAttribute("user", userPS);
        return "user/profileUpdateForm";
    }

    @PostMapping("user/profileUpdate")
    public String profileUpdate(MultipartFile profile) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            return "redirect:/loginForm";
        }
        if (profile.isEmpty()) { // null과 공백을 다 비교해줌
            throw new CustomException("사진이 전송되지 않았습니다.");
        }

        // 2. 저장된 파일의 경로를 DB에 저장
        User userPS = userService.프로필사진수정(profile, principal.getId());
        session.setAttribute("principal", userPS); // 세션 값 동기화 (유저정보가 바뀌면 무조건 바꿔줘야함.)
        return "redirect:/";
    }

    @PutMapping("user/profileUpdate")
    public @ResponseBody ResponseEntity<?> profileAjaxUpdate(MultipartFile profile) {
        User principal = (User) session.getAttribute("principal");

        if (principal == null) {
            throw new CustomApiException("인증되지 않았습니다.");
        }

        if (profile.isEmpty()) { // null과 공백을 다 비교해줌
            throw new CustomApiException("사진이 전송되지 않았습니다.");
        }

        // 2. 저장된 파일의 경로를 DB에 저장
        User userPS = userService.프로필사진아작스수정(profile, principal.getId());
        session.setAttribute("principal", userPS);
        return new ResponseEntity<>(new ResponseDto<>(1, "프로필 사진 수정 성공", null), HttpStatus.CREATED);
    }
}