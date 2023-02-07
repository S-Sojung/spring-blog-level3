package shop.mtcoding.blog.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import shop.mtcoding.blog.dto.board.BoardReq.BoardSaveReqDto;
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
    public String detail(@PathVariable int id) {
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
}
