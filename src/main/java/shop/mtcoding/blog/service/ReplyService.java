package shop.mtcoding.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.reply.ReplyReq.ReplySaveReqDto;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.model.Reply;
import shop.mtcoding.blog.model.ReplyRepository;

@Transactional(readOnly = true) // 여기 붙이면 모든 메서드에 다 붙음
@Service
public class ReplyService {

    @Autowired
    private ReplyRepository replyRepository;

    @Transactional
    public void 댓글쓰기(ReplySaveReqDto replySaveReqDto, int principalId) {

        // content내용을 Document로 받고, img 찾아내서 0번지 src를 찾아넣어줌
        System.out.println(replySaveReqDto.getComment() + " " + replySaveReqDto.getBoardId());
        int result = replyRepository.insert(replySaveReqDto.getComment(), replySaveReqDto.getBoardId(), principalId);

        if (result != 1) {
            throw new CustomApiException("댓글쓰기 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Transactional
    public void 댓글삭제(int id, int principalId) {
        Reply replyPS = replyRepository.findById(id);
        if (replyPS == null) {
            throw new CustomApiException("없는 댓글을 삭제할 수 없습니다."); // bad request
        }
        if (replyPS.getUserId() != principalId) {
            throw new CustomApiException("해당 댓글을 삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        try {
            replyRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomApiException("서버가 일시적인 문제가 생겼습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
