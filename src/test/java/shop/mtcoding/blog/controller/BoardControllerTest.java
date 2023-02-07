package shop.mtcoding.blog.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.blog.dto.board.BoardResp.BoardDetailRespDto;
import shop.mtcoding.blog.dto.board.BoardResp.BoardMainRespDto;
import shop.mtcoding.blog.model.User;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class BoardControllerTest {

    @Autowired
    private MockMvc mvc;

    private MockHttpSession mockSession;

    @Autowired
    private ObjectMapper om;

    @Test
    public void update_test() throws Exception {
        // given
        int id = 8;
        String requestBody = "title=수정된 제목입니다&content=수정된 내용입니다";

        // when
        ResultActions resultActions = mvc.perform(
                put("/board/" + id)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .session(mockSession)); // session이 주입된 채로 요청

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.print("테스트: " + responseBody);

        // then
        // resultActions.andExpect(jsonPath("$.msg").value("수정 성공"));
        // resultActions.andExpect(status().isOk());
    }

    @Test
    public void delete_test() throws Exception {
        // given
        int id = 1;

        // when
        ResultActions resultActions = mvc.perform(
                delete("/board/" + id).session(mockSession)); // session이 주입된 채로 요청
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.print("테스트: " + responseBody);

        /**
         * jsonPath
         * 최상위를 찾을 때는 $ 를 이용
         * 객체 탐색 은 닷 (.) 을 이용
         * 배열 : [0] 인덱스 이용
         * {"code":1,"msg":"삭제성공","data":null}
         * => $.code, $.msg ,$.data 등으로 찾을 수 있다.
         */
        // then
        resultActions.andExpect(jsonPath("$.msg").value("삭제성공"));
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void datail_test() throws Exception {
        // given
        int id = 4;

        // when
        ResultActions resultActions = mvc.perform(
                get("/board/" + id));

        Map<String, Object> map = resultActions.andReturn().getModelAndView().getModel();
        BoardDetailRespDto dto = (BoardDetailRespDto) map.get("dto");

        String model = om.writeValueAsString(dto);
        System.out.println("테스트 : " + model);

        // then
        resultActions.andExpect(status().isOk());
        assertThat(dto.getId()).isEqualTo(4);
        assertThat(dto.getUsername()).isEqualTo("love");
        assertThat(dto.getTitle()).isEqualTo("4번째 제목");
        assertThat(dto.getContent()).isEqualTo("4번째 내용");
        assertThat(dto.getUserId()).isEqualTo(2);
    }

    @Test
    public void main_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(
                get("/"));

        Map<String, Object> map = resultActions.andReturn().getModelAndView().getModel();
        List<BoardMainRespDto> dtos = (List<BoardMainRespDto>) map.get("dtos");

        String model = om.writeValueAsString(dtos);
        System.out.println("테스트 : " + model);

        // then
        resultActions.andExpect(status().isOk());
        assertThat(dtos.size()).isEqualTo(6);
        assertThat(dtos.get(0).getUsername()).isEqualTo("ssar");
        assertThat(dtos.get(0).getTitle()).isEqualTo("1번째 제목");

    }

    @BeforeEach // Test메서드 실행 직전마다 호출된다
    public void setUp() {
        // 임시 세션 생성하기
        User user = new User();
        user.setId(1);
        user.setUsername("ssar");
        user.setPassword("1234");
        user.setEmail("ssar@nate.com");
        user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        mockSession = new MockHttpSession();
        mockSession.setAttribute("principal", user);
    }

    @Test
    public void save_test() throws Exception {
        // given
        String title = "";
        for (int i = 0; i < 200; i++) {
            title += "가";
        }
        String requestBody = "title=" + title + ".&content=내용1입니다.";

        // when
        ResultActions resultActions = mvc.perform(
                post("/board")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .session(mockSession)); // session이 주입된 채로 요청

        // then
        resultActions.andExpect(status().is3xxRedirection());

    }
}
