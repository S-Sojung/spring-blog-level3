package shop.mtcoding.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.user.UserReq.JoinReqDto;
import shop.mtcoding.blog.dto.user.UserReq.LoginReqDto;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.model.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public int 회원가입(JoinReqDto joinReqDto) {
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
        return result;

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

}
