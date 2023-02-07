package shop.mtcoding.blog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.board.BoardReq.BoardSaveReqDto;
import shop.mtcoding.blog.dto.board.BoardResp.BoardMainRespDto;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.BoardRepository;

@Transactional(readOnly = true) // 여기 붙이면 모든 메서드에 다 붙음
@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Transactional // where절에 들어갈 id는 앞에 넣는게 좋다 (이건 where절에 들어가는건 아니니까 마음대로)
    public void 글쓰기(BoardSaveReqDto boardSaveReqDto, int userId) {

        int result = boardRepository.insert(boardSaveReqDto.getTitle(), boardSaveReqDto.getContent(), userId);

        if (result != 1) {
            throw new CustomException("글쓰기 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // public List<BoardMainRespDto> 게시글목록() {
    // // 만약 user.password를 같이 하고싶다면? 새로운 dto를 만들면된다.
    // // 두 개를 리턴하지 못하기 때문.
    // return boardRepository.findAllWithUser();
    // }
}
