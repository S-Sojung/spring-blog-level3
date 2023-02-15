package shop.mtcoding.blog.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.blog.dto.ResponseDto;
import shop.mtcoding.blog.dto.board.BoardResp.BoardAllRespDto;
import shop.mtcoding.blog.dto.reply.ReplyResp.ReplyAllRespDto;
import shop.mtcoding.blog.dto.user.UserReq.LoginReqDto;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.BoardRepository;
import shop.mtcoding.blog.model.ReplyRepository;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.model.UserRepository;
import shop.mtcoding.blog.service.UserService;

@Controller
public class AdminController {

    @Autowired
    HttpSession session;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    ReplyRepository replyRepository;

    @GetMapping("/admin")
    public String admin() {
        User userPS = (User) session.getAttribute("principal");
        if (userPS == null) {
            // throw new CustomException("인증이 되지 않았습니다.");
            return "redirect:/admin/loginForm";
        }
        if (!userPS.getRole().equals("ADMIN")) {
            throw new CustomException("관리자 권한이 없습니다.");
        }

        return "admin/admin";
    }

    @GetMapping("/admin/loginForm")
    public String adminLoginForm() {

        return "admin/loginForm";
    }

    @PostMapping("/admin/login")
    public String adminLogin(LoginReqDto loginReqDto) {
        if (loginReqDto.getUsername() == null || loginReqDto.getUsername().isEmpty()) {
            throw new CustomException("username을 작성해주세요");
        }
        if (loginReqDto.getPassword() == null || loginReqDto.getPassword().isEmpty()) {
            throw new CustomException("password를 작성해주세요");
        }
        User principal = userService.로그인(loginReqDto);
        if (!principal.getRole().equals("ADMIN")) {
            throw new CustomException("관리자 권한이 없습니다.");
        }

        session.setAttribute("principal", principal);

        return "redirect:/admin";
    }

    // @GetMapping("/admin/user")
    // public String user(Model model) {
    // List<User> users = (List<User>) userRepository.findAll();
    // model.addAttribute("datas", users);
    // return "/admin/user";
    // }

    // @GetMapping("/admin/board")
    // public String board(Model model) {
    // List<BoardAllRespDto> boards = (List<BoardAllRespDto>)
    // boardRepository.findAllContentWithUser();
    // model.addAttribute("datas", boards);
    // return "/admin/board";
    // }

    // @GetMapping("/admin/reply")
    // public String reply(Model model) {
    // List<ReplyAllRespDto> replies = (List<ReplyAllRespDto>)
    // replyRepository.findAllWithUser();
    // model.addAttribute("datas", replies);
    // return "/admin/reply";
    // }

    @GetMapping("/admin/user")
    public @ResponseBody ResponseEntity<?> user() {
        List<User> users = (List<User>) userRepository.findAll();

        return new ResponseEntity<>(new ResponseDto<>(1, "회원 정보 확인", users),
                HttpStatus.CREATED);
    }

    @GetMapping("/admin/board")
    public @ResponseBody ResponseEntity<?> board() {
        List<BoardAllRespDto> boards = (List<BoardAllRespDto>) boardRepository.findAllContentWithUser();

        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 정보 확인", boards),
                HttpStatus.CREATED);
    }

    @GetMapping("/admin/reply")
    public @ResponseBody ResponseEntity<?> reply() {
        List<ReplyAllRespDto> replies = (List<ReplyAllRespDto>) replyRepository.findAllWithUser();
        return new ResponseEntity<>(new ResponseDto<>(1, "댓글 정보 확인", replies),
                HttpStatus.CREATED);
    }

}
