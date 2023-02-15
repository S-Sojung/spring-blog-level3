package shop.mtcoding.blog.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import shop.mtcoding.blog.dto.user.UserReq.JoinReqDto;
import shop.mtcoding.blog.dto.user.UserReq.LoginReqDto;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.model.UserRepository;
import shop.mtcoding.blog.util.PathUtil;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HttpSession session;

    @Transactional
    public void 회원가입(JoinReqDto joinReqDto) {
        // int result = userRepository.insert(joinReqDto.getUsername(),
        // joinReqDto.getPassword(), joinReqDto.getEmail());

        // if (result != 1) {
        // return -1;
        // }
        // return 1;

        // 여기는 중복으로 들어올 수 있지만
        User sameUser = userRepository.findByUsername(joinReqDto.getUsername());
        if (sameUser != null) {
            throw new CustomException("동일한 username이 존재합니다");
        }
        // 여기서는 lock 걸린다. 그냥 트랜잭션에 동시접근 못한다고 생각가능하다. (하지만 메서드 자체는 접근 가능하다는걸 알고 있자)
        int result = userRepository.insert(joinReqDto.getUsername(), joinReqDto.getPassword(), joinReqDto.getEmail());
        if (result != 1) {
            throw new CustomException("회원가입실패");
        }
        // return result;

    }

    // @Transation 안함. select이기 때문에
    // 하지만 데이터가 꼬일 수 있는 상황 (select 두개..) 이면 한 번 봤던 데이터를 계속 보기 위해서
    @Transactional(readOnly = true) // 로 걸어준다. 하지만 상황에 따라 달라질 수 있다
    public User 로그인(LoginReqDto loginReqDto) {
        User principal = userRepository.findByUsernameAndPassword(loginReqDto.getUsername(), loginReqDto.getPassword());
        // User user = new User(); //임시 더미데이터
        // user.setId(1);
        // user.setUsername("ssar");
        // user.setPassword("1234");
        // user.setEmail("ssar@nate.com");
        // user.setCreatedAt(null);
        if (principal == null) {
            throw new CustomException("유저네임 혹은 패스워드가 잘못입력되었습니다.");
        }
        return principal;
    }

    @Transactional
    public User 프로필사진수정(MultipartFile profile, int principalId) {

        // Path imageFilePath = PathUtil.getImagePath(profile.getOriginalFilename());

        // try {
        // // nio, 경로는 path 타입, 프로필은 바이트 타입
        // Files.write(imageFilePath, profile.getBytes());
        // } catch (Exception e) {
        // throw new CustomException("사진을 웹서버에 저장하지 못하엿습니다.",
        // HttpStatus.INTERNAL_SERVER_ERROR);
        // }

        // String path = imageFilePath.toString();

        // 1. 사진을 /static/images에 UUID로 변경해서 하드디스크에 저장하고, 그 이름을 변수로 저장해줌
        String uuidImageName = PathUtil.writeImageFile(profile);

        // 업데이트 문을 사용할 때는 무조건 전체 변경을 하는 것이 좋다.
        // 2. 저장된 파일의 경로를 DB에 저장
        User userPS = userRepository.findById(principalId);
        userPS.setProfile(uuidImageName);

        int result = userRepository.updateById(principalId, userPS.getUsername(), userPS.getPassword(),
                userPS.getEmail(), userPS.getProfile(), userPS.getCreatedAt());
        if (result != 1) {
            throw new CustomException("프로필 업로드 실패");
        }
        // 세션 값은 컨트롤러에서 바꿔줌
        // User user = userRepository.findById(userPS.getId());
        // session.setAttribute("principal", user);
        return userPS;
    }

    @Transactional
    public User 프로필사진아작스수정(MultipartFile profile, int principalId) {

        String uuidImageName = PathUtil.writeImageFile(profile);

        // 2. 저장된 파일의 경로를 DB에 저장
        User userPS = userRepository.findById(principalId);
        userPS.setProfile(uuidImageName);

        int result = userRepository.updateById(principalId, userPS.getUsername(), userPS.getPassword(),
                userPS.getEmail(), userPS.getProfile(), userPS.getCreatedAt());
        if (result != 1) {
            throw new CustomApiException("프로필 업로드 실패");
        }
        return userPS;
    }

    @Transactional
    public void 유저삭제(int id, User principal) {
        User userPS = userRepository.findById(id);
        if (userPS == null) {
            throw new CustomApiException("없는 사용자를 삭제할 수 없습니다."); // 401
        }
        if (!principal.getRole().equals("ADMIN")) {
            if (principal.getId() != userPS.getId()) {
                throw new CustomApiException("권한이 아닙니다.", HttpStatus.FORBIDDEN); // 401
            }
        }
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomApiException("서버가 일시적인 문제가 생겼습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
