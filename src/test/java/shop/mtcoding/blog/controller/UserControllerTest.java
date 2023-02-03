package shop.mtcoding.blog.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

//junit test 하는 법... 나중에 알아보자. 
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    // 이때 UserController에 autowired 해준 것들도 다 띄워줘야한다. 안그럼 오류뜸!!!!!!
    @Test
    public void join_test() throws Exception {
        // given (데이터)
        String requestBody = "username=aaa&password=1234&email=aaa@nate.com";

        // when
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/join").content(requestBody)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE));

        // then
        resultActions.andExpect(status().is3xxRedirection());
    }
}