package shop.mtcoding.blog.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import shop.mtcoding.blog.dto.user.UserReq.JoinReqDto;
import shop.mtcoding.blog.dto.user.UserReq.LoginReqDto;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.model.UserRepository;
import shop.mtcoding.blog.service.UserService;
import shop.mtcoding.blog.util.PathUtil;

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
        // x-www-url-encoded form 으로 들어오는데 옆에 사진이랑 같이 들어온다.
        // 둘이 째져서 들어오는걸 알아서 파싱함!!! -> MultipartFile 또한 폼태그의 name값을 변수명으로 받음

        // 통신이라는건 클라이언트가 서버에 데이터를 요청하면 결국엔 버퍼에 담겨서 가는데
        // 헤더와 바디로 나뉘는 이유는 아파치톰캣이 먼저 그냥 버퍼로 다 읽는다.
        // 아파치 톰캣이 리캐스트 객체를 만들어서 구분지어준다.

        // System.out.println(profile.getContentType());
        // System.out.println(profile.getSize());
        // System.out.println(profile.getOriginalFilename());
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            return "redirect:/loginForm";
        }
        if (profile.isEmpty()) { // null과 공백을 다 비교해줌
            throw new CustomException("사진이 전송되지 않았습니다.");
        }
        // if (profile.getContentType().matches("image.*")) { // null과 공백을 다 비교해줌
        // throw new CustomException("사진이 전송되지 않았습니다.");
        // } 사진이 아니면 터트리기

        // 1. 파일은 하드디스크에 저장
        // 만약 절대 경로를 저장하려면 ? "c:\\workspace\\upload" -> 외부 경로? 근데 이걸 웹서버로 등록해줘야한다.
        // 하지만 하드코드로 경로를 다 적으면 배포하는 순간 터지게 되니 주의.

        // String savePath = System.getProperty("user.dir") +
        // "\\src\\main\\resources\\static\\images\\";
        // System.out.println(savePath);
        // Path imageFilePath = Paths.get(savePath + "\\" +
        // profile.getOriginalFilename());
        // // ------------------------------------------------
        // String imagePath = PathUtil.getAppImagePath();
        // String imageName = profile.getOriginalFilename();
        // UUID uuid = UUID.randomUUID();
        // Path imageFilePath = Paths.get(imagePath + "\\" + uuid + "_" + imageName);
        // 이때 imageName은 중복 될 수 있기 때문에 UUID를 사용할 것
        // --------------------------------------------------
        // > 메서드로 이용 (/static/images 이미지경로)

        // //컨트롤러의 역할 이 아님
        // Path imageFilePath = PathUtil.getImagePath(profile.getOriginalFilename());

        // Path imageFilePath = Paths.get("" + profile.getOriginalFilename());
        // // java.nio.file : none blocking IO - 스레드 자동 지원해줌
        // System.out.println(imageFilePath);

        // try {
        // // nio, 경로는 path 타입, 프로필은 바이트 타입
        // Files.write(imageFilePath, profile.getBytes());
        // } catch (Exception e) {
        // e.printStackTrace();
        // }

        // 2. 저장된 파일의 경로를 DB에 저장
        User userPS = userService.프로필사진수정(profile, principal.getId());
        session.setAttribute("principal", userPS); // 세션 값 동기화 (유저정보가 바뀌면 무조건 바꿔줘야함.)
        return "redirect:/";
    }
}