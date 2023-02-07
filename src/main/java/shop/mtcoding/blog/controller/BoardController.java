package shop.mtcoding.blog.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.blog.dto.ResponseDto;
import shop.mtcoding.blog.dto.board.BoardReq.BoardSaveReqDto;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.BoardRepository;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.service.BoardService;

@Controller
public class BoardController {

    @Autowired
    private HttpSession session;
    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardRepository boardRepository;

    private void mockSession() {
        User user = new User();
        user.setId(1);
        user.setUsername("ssar");
        session.setAttribute("principal", user);
    }

    @GetMapping({ "/", "/board" })
    public String main(Model model) {
        // 지금 서비스는 만들 필요는 없다. 하지만 통일성을 위해 서비스를 만들어 줌
        /*
         * List<BoardMainRespDto> dtos = boardService.게시글목록();
         * // List<BoardMainRespDto> dto2 = boardService.게시글 한 건(); 이런식으로 서비스 두번 호출하면 안됨
         * //이처럼 할 거면 차라리 여기다 Repository 를 사용해라
         * 
         * model.addAttribute("dtos", dtos);
         * // model.addAttribute("dto2", dtos); 이렇게 하면 안됨
         */
        model.addAttribute("dtos", boardRepository.findAllWithUser());
        return "board/main";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable int id, Model model) {
        model.addAttribute("dto", boardRepository.findByIdWithUser(id));
        return "board/detail";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        return "board/saveForm";
    }

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable int id) {
        return "board/updateForm";
    }

    @PostMapping("/board")
    public String save(BoardSaveReqDto boardSaveReqDto) {
        // mockSession();//인증
        // 인증
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomException("인증이 되지 않았습니다.", HttpStatus.UNAUTHORIZED); // 401
        }
        // 유효성 검사 : 길이 검사도 해야한다
        if (boardSaveReqDto.getTitle() == null || boardSaveReqDto.getTitle().isEmpty()) {
            throw new CustomException("title 작성해주세요");
        }
        if (boardSaveReqDto.getContent() == null || boardSaveReqDto.getContent().isEmpty()) {
            throw new CustomException("content 작성해주세요");
        }
        if (boardSaveReqDto.getTitle().length() > 100) {
            throw new CustomException("title의 길이가 100자 이하여야 합니다");
        }

        boardService.글쓰기(boardSaveReqDto, principal.getId());

        return "redirect:/";
    }

    @DeleteMapping("/board/{id}")
    public @ResponseBody ResponseEntity<?> delete(@PathVariable int id) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            // throw new CustomException("인증이 되지 않았습니다.", HttpStatus.UNAUTHORIZED); // 401
            // 자바스크립트로 JSON을 응답 해주어야하기 때문에 새로운 Exception을 만들어줌
            throw new CustomApiException("인증이 되지 않았습니다.", HttpStatus.UNAUTHORIZED); // 401

        }
        // 권한 검사는 DB를 열어봐야하기 때문에 서비스에서 함.
        boardService.게시글삭제(id, principal.getId());

        // form태그는 수행이 완료했을 때 page를 돌려주고 다시그리게도미
        // get 요청하면 거기에 대한 page를 만들어내서 서버사이드 랜더링을함.

        // return "redirect:/";
        // 자바스크립트 ajax로 삭제할거니까 ok만 받고, json으로 받음!!!!
        // return "ok"; // 응답의 dto 도 만들어줘야함 (1 , -1) 로 응답할 것 . // 클라이언트 사이드 랜더링. 모델에 담을
        // 땐 안쓴다
        return new ResponseEntity<>(new ResponseDto<>(1, "삭제성공", null), HttpStatus.OK);
    }
}
