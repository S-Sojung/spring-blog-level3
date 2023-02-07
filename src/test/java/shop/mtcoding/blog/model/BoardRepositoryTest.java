package shop.mtcoding.blog.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.blog.dto.board.BoardResp.BoardMainRespDto;

//Filter - Dispatcher Servlet - Contoller - Service - Repository - MyBatis - DB
//SOLID 중 S (SRP) : 단일책임의 원칙
@MybatisTest
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void findAllWithUser_test() throws Exception {
        // given
        ObjectMapper om = new ObjectMapper();
        // Jackson 라이브러리를 들고 있는데, (json을 자바 오브젝트로 변환시킨다.)
        // -> 디스패쳐 서블릿이 컨트롤러로 넘겨주기 직전에 응답 (Controller에서는 Autowired로 띄울 수 있다. )
        // 여기서는 R - MyBatis - DB 만 테스트 하기 때문에 강제로 띄워준다.

        // when
        List<BoardMainRespDto> boardMainRespDto = boardRepository.findAllWithUser();
        System.out.println("테스트 : size : " + boardMainRespDto.size());
        String responseBody = om.writeValueAsString(boardMainRespDto);
        System.out.println("테스트 : " + responseBody);
        // om을 쓰지 않는다면??
        /*
         * boardMainRespDto.forEach((dto)->
         * System.out.println("테스트 : "+ dto.getId());
         * });
         */
        // 혹은 dto 파일에서 @toString 을 사용 .

        // then
        assertThat(boardMainRespDto.get(5).getUsername()).isEqualTo("love");
    }
}
