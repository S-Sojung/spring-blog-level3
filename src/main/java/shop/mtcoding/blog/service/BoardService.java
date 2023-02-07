package shop.mtcoding.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.board.BoardReq.BoardSaveReqDto;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.Board;
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

    @Transactional // AOP ?
    public void 게시글삭제(int id, int userId) {
        Board boardPS = boardRepository.findById(id);
        if (boardPS == null) {
            throw new CustomApiException("없는 게시글을 삭제할 수 없습니다."); // bad request
        }
        if (boardPS.getUserId() != userId) {
            throw new CustomApiException("해당 게시글을 삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        try {
            boardRepository.deleteById(id);
            // 내가 try catch로 제어하고 싶으면 이걸 다시 try catch로 묶어주는 것
        } catch (Exception e) {
            throw new CustomApiException("서버가 일시적인 문제가 생겼습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            // 로그를 남겨야함 (DB or File) -> 서버쪽 심각한 오류가 생긴거니까 꼭 수정해줘야함.
            // 이 로그에는 e.getMassage() 와 시간, 유저 정보들을 넘겨줘야함 .(새로운 Exception... 을 만들면됨 )
        }
    }

    public void 게시글수정(int id, int userId) {

    }
}
