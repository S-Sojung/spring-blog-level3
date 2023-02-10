package shop.mtcoding.blog.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.blog.dto.ResponseDto;
import shop.mtcoding.blog.dto.reply.ReplyReq.ReplySaveReqDto;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.service.ReplyService;

@Controller
public class ReplyController {

    @Autowired
    private HttpSession session;
    @Autowired
    private ReplyService replyService;

    // reply 안에 where 에 들어가지 않음 .. id를 작성 안해도 된다.
    // 하지만 id번호의 board에 reply 작성하겠다.. 라는 의미를 넣을 수 있다.
    @PostMapping("/reply")
    public String save(ReplySaveReqDto replySaveReqDto) {
        // Comment, userId, boardId.. user 는 세션
        // 관리자가 접근할 시... : 관리자 서버는 따로 만든다. 혹은 여기에 넣어서 세션을 가져와 검증해봐야한다

        // 인증
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomException("인증이 되지 않았습니다.", HttpStatus.UNAUTHORIZED);
        }

        // vaildation 체크
        if (replySaveReqDto.getComment() == null || replySaveReqDto.getComment().isEmpty()) {
            throw new CustomException("comment를 작성해주세요");
        }
        if (replySaveReqDto.getBoardId() == null) {
            throw new CustomException("boardId가 필요합니다.");
        }
        if (replySaveReqDto.getComment().length() > 100) {
            throw new CustomException("100자 이내로 작성해주세요");
        }

        replyService.댓글쓰기(replySaveReqDto, principal.getId());
        return "redirect:/board/" + replySaveReqDto.getBoardId();
    }

    @DeleteMapping("/reply/{id}")
    public @ResponseBody ResponseEntity<?> delete(@PathVariable int id) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("인증이 되지 않았습니다.", HttpStatus.UNAUTHORIZED); // 401

        }
        replyService.댓글삭제(id, principal.getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "삭제성공", null), HttpStatus.OK);
    }
}
