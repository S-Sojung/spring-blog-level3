package shop.mtcoding.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import shop.mtcoding.blog.dto.reply.ReplyReq.ReplySaveReqDto;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.model.Reply;
import shop.mtcoding.blog.model.ReplyRepository;

@Slf4j
@Transactional(readOnly = true) // 여기 붙이면 모든 메서드에 다 붙음
@Service
public class ReplyService {

    @Autowired
    private ReplyRepository replyRepository;

    @Transactional
    public void 댓글쓰기(ReplySaveReqDto replySaveReqDto, int principalId) {

        // content내용을 Document로 받고, img 찾아내서 0번지 src를 찾아넣어줌
        // System.out.println(replySaveReqDto.getComment() + " " +
        // replySaveReqDto.getBoardId());
        int result = replyRepository.insert(replySaveReqDto.getComment(), replySaveReqDto.getBoardId(), principalId);

        if (result != 1) {
            throw new CustomApiException("댓글쓰기 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Transactional
    public void 댓글삭제(int id, int principalId) {
        // AOP 부가적인 코드들을 자동화 시킬 수 있다.
        Reply replyPS = replyRepository.findById(id);
        if (replyPS == null) {
            throw new CustomApiException("댓글이 존재하지 않습니다.");
        }
        if (replyPS.getUserId() != principalId) {
            throw new CustomApiException("해당 댓글을 삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        try {
            // 내부적으로 Mybatis가 하는데... delete 실패하면서 터지면? 자바까지 답을 안줘서 if로 제어를 할 수 없음.
            // try catch를 사용해서 제어한다.
            replyRepository.deleteById(id);
        } catch (Exception e) {
            log.error("서버에러 : " + e.getMessage()); // @Slf4j 로 이용
            // 버퍼 달고, 파일에 쓰기
            // System.out.println("서버에러 : "+e.getMessage()); //원래는 로그에 하거나 파일에 저장해야함
            throw new CustomApiException("댓글 삭제 실패 - 서버 에러", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
