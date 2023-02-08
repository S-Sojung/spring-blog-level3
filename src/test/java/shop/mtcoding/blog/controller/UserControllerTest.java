package shop.mtcoding.blog.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.model.User;

//test는 순서에 대한 보장이 없다. 

/*
 * SpringBootTest는 통합테스트(실제 환경과 동일하게 Bean이 생성됨)
 * WebEnvironment.MOCK : 가짜 환경에 IoC컨테이너가 있는 것 => 그래서 서버 실행중에도 사용이 가능하다 
 * AutoConfigureMockMvc는 mock 환경의 IoC컨테이너에 MockMvc Bean이 생성된다. 
 */
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void join_test() throws Exception {
        System.out.print("테스트 : join_test()");
        // given
        String requestBody = "username=ssar&password=1234&email=ssar@nate.com";

        // when
        ResultActions resultActions = mvc.perform(post("/join").content(requestBody)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE));

        // then
        resultActions.andExpect(status().is3xxRedirection());
    }

    @Test
    public void login_test() throws Exception {
        // given
        // String requestBody = "username=cos&password=1234";
        String requestBody = "username=ssar&password=1234";

        // when
        ResultActions resultActions = mvc.perform(post("/login").content(requestBody)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)); // mime type
        // .contentType(MediaType.APPLICATION_JSON)); //기본이 x-www 인데 json으로 바꾸면 터진다.

        HttpSession session = resultActions.andReturn().getRequest().getSession();
        User pricipal = (User) session.getAttribute("principal");
        // System.out.println(pricipal.getUsername());

        // then
        assertThat(pricipal.getUsername()).isEqualTo("ssar");
        resultActions.andExpect(status().is3xxRedirection());
    }
}

// package shop.mtcoding.blog.controller;

// import static
// org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.ResultActions;
// import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

// // junit test 하는 법... 나중에 알아보자.
// 얘가 AutoConfigureMockMvc를
// 가지고 있어서
// 굳이 어노테이션 할 필요없음 @WebMvcTest(UserController.class)

// public class UserControllerTest {

// @Autowired
// private MockMvc mvc;

// // 이때 UserController에 autowired 해준 것들도 다 띄워줘야한다. 안그럼 오류뜸!!!!!!
// @Test
// public void join_test() throws Exception {
// // given (데이터)
// String requestBody = "username=aaa&password=1234&email=aaa@nate.com";

// // when
// ResultActions resultActions =
// mvc.perform(MockMvcRequestBuilders.post("/join").content(requestBody)
// .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE));

// // then
// resultActions.andExpect(status().is3xxRedirection());
// }
// }