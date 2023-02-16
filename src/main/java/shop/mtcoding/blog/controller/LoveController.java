package shop.mtcoding.blog.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.blog.dto.ResponseDto;
import shop.mtcoding.blog.dto.love.LoveReq.LoveInsertReqDto;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.model.Love;
import shop.mtcoding.blog.model.LoveRepository;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.service.LoveService;

@Controller
public class LoveController {
    @Autowired
    private LoveRepository loveRepository;
    @Autowired
    private LoveService loveService;
    @Autowired
    private HttpSession session;

    @PostMapping("/love")
    public @ResponseBody ResponseEntity<?> love(
            @RequestBody LoveInsertReqDto loveInsertReqDto) {
        // 인증
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("인증이 되지 않았습니다.", HttpStatus.UNAUTHORIZED); // 401
        }
        if (loveInsertReqDto.getBoardId() == null) {
            throw new CustomApiException("boardId가 필요합니다.");
        }

        loveService.사랑하기(loveInsertReqDto, principal.getId());
        Love love = loveRepository.findByUserIdAndBoardId(principal.getId(), loveInsertReqDto.getBoardId());
        return new ResponseEntity<>(new ResponseDto<>(1, "사랑성공", love.getId()), HttpStatus.CREATED);
    }

    @DeleteMapping("/love/{id}")
    public @ResponseBody ResponseEntity<?> delete(@PathVariable int id) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("인증이 되지 않았습니다.", HttpStatus.UNAUTHORIZED); // 401
        }

        loveService.사랑하기삭제(id, principal);

        return new ResponseEntity<>(new ResponseDto<>(1, "삭제성공", 0), HttpStatus.OK);
    }

}
