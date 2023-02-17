package shop.mtcoding.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.love.LoveReq.LoveInsertReqDto;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.model.LoveRepository;
import shop.mtcoding.blog.model.User;

@Transactional(readOnly = true) // 여기 붙이면 모든 메서드에 다 붙음
@Service
public class LoveService {
    @Autowired
    private LoveRepository loveRepository;

    @Transactional
    public void 사랑하기(LoveInsertReqDto loveInsertReqDto, int principal) {
        int result = loveRepository.insert(loveInsertReqDto.getLove(), principal,
                loveInsertReqDto.getBoardId());

        if (result != 1) {
            throw new CustomApiException("사랑하기 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void 사랑하기삭제(int id) {
        try {
            loveRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomApiException("서버가 일시적인 문제가 생겼습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
