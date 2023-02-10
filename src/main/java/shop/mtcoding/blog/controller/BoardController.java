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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.blog.dto.ResponseDto;
import shop.mtcoding.blog.dto.board.BoardReq.BoardSaveReqDto;
import shop.mtcoding.blog.dto.board.BoardReq.BoardUpdateReqDto;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.BoardRepository;
import shop.mtcoding.blog.model.ReplyRepository;
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
    @Autowired
    private ReplyRepository replyRepository;

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
        Board boardPS = boardRepository.findById(id);
        if (boardPS == null) {
            throw new CustomException("없는 게시글에 접근 할 수 없습니다.", HttpStatus.UNAUTHORIZED); // 401
        }
        model.addAttribute("boardDto", boardRepository.findByIdWithUser(id));
        model.addAttribute("replyDtos", replyRepository.findByBoardIdWithUser(id));
        return "board/detail";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        return "board/saveForm";
    }

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable int id, Model model) {
        // 인증
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomException("인증이 되지 않았습니다.", HttpStatus.UNAUTHORIZED); // 401
        }
        Board boardPS = boardRepository.findById(id);
        if (boardPS == null) {
            throw new CustomException("없는 게시글을 수정할 수 없습니다.");
        }
        // 권한
        if (boardPS.getUserId() != principal.getId()) {
            throw new CustomException("게시글을 수정할 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        model.addAttribute("board", boardPS);
        return "board/updateForm";
    }

    @PutMapping("/board/{id}") // 인증과 권한이 필요함 //기본 파싱 전략이 x-www-form-urlencoded 이기 때문에 json으로 바꿔줘야함,
                               // @RequestBody를 사용해서 알아서 파싱해준다.
    public @ResponseBody ResponseEntity<?> update(@PathVariable int id,
            @RequestBody BoardUpdateReqDto boardUpdateReqDto) {
        // 인증
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("인증이 되지 않았습니다.", HttpStatus.UNAUTHORIZED); // 401
        }
        // 유효성검사
        // System.out.println("테스트 : " + boardUpdateReqDto.getTitle());
        // System.out.println("테스트 : " + boardUpdateReqDto.getContent());

        if (boardUpdateReqDto.getTitle() == null ||
                boardUpdateReqDto.getTitle().isEmpty()) {
            throw new CustomApiException("title 작성해주세요");
        }
        if (boardUpdateReqDto.getContent() == null ||
                boardUpdateReqDto.getContent().isEmpty()) {
            throw new CustomApiException("content 작성해주세요");
        }
        if (boardUpdateReqDto.getTitle().length() > 100) {
            throw new CustomApiException("title의 길이가 100자 이하여야 합니다");
        }

        // 서비스에서 권한 체크
        boardService.게시글수정(id, boardUpdateReqDto, principal.getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 수정 성공", null), HttpStatus.CREATED);
        // return "redirect:/board/" + id;
    }

    @PostMapping("/board")
    public @ResponseBody ResponseEntity<?> save(
            @RequestBody BoardSaveReqDto boardSaveReqDto) {
        // 인증
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("인증이 되지 않았습니다.", HttpStatus.UNAUTHORIZED); // 401
        }
        if (boardSaveReqDto.getTitle() == null || boardSaveReqDto.getTitle().isEmpty()) {
            throw new CustomApiException("title 작성해주세요");
        }
        if (boardSaveReqDto.getContent() == null || boardSaveReqDto.getContent().isEmpty()) {
            throw new CustomApiException("content 작성해주세요");
        }
        if (boardSaveReqDto.getTitle().length() > 100) {
            throw new CustomApiException("title의 길이가 100자 이하여야 합니다");
        }

        boardService.글쓰기(boardSaveReqDto, principal.getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 작성 성공", null), HttpStatus.CREATED);
    }

    // @PostMapping("/board")
    // public String save(HttpServletRequest request) {
    // BoardSaveReqDto boardSaveReqDto = new BoardSaveReqDto();

    // try {
    // BufferedReader br = request.getReader();
    // String data = "";
    // String decodeData = "";
    // while (true) {
    // String input = br.readLine();
    // if (input == null)
    // break;
    // data += input;
    // }

    // try {
    // decodeData = URLDecoder.decode(data, "UTF-8");
    // } catch (Exception e) {
    // e.printStackTrace();
    // }

    // String temp[] = decodeData.split("&");
    // boardSaveReqDto.setTitle(temp[0].split("=")[1]);
    // boardSaveReqDto.setContent(temp[1].substring(temp[1].indexOf("=") + 1));
    // System.out.print(boardSaveReqDto.getContent());

    // } catch (Exception e) {
    // e.printStackTrace();
    // }

    // // 인증
    // User principal = (User) session.getAttribute("principal");
    // if (principal == null) {
    // throw new CustomException("인증이 되지 않았습니다.", HttpStatus.UNAUTHORIZED); // 401
    // }
    // // 유효성 검사 : 길이 검사도 해야한다
    // // System.out.println("테스트 : " + boardSaveReqDto.getTitle());
    // // System.out.println("테스트 : " + boardSaveReqDto.getContent().substring(0,
    // 20));

    // if (boardSaveReqDto.getTitle() == null ||
    // boardSaveReqDto.getTitle().isEmpty()) {
    // throw new CustomException("title 작성해주세요");
    // }
    // if (boardSaveReqDto.getContent() == null ||
    // boardSaveReqDto.getContent().isEmpty()) {
    // throw new CustomException("content 작성해주세요");
    // }
    // if (boardSaveReqDto.getTitle().length() > 100) {
    // throw new CustomException("title의 길이가 100자 이하여야 합니다");
    // }

    // boardService.글쓰기(boardSaveReqDto, principal.getId());

    // return "redirect:/";
    // }

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
