package shop.mtcoding.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import shop.mtcoding.blog.model.BoardRepository;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    public int 게시글쓰기(String title, String content, int id) {
        int result = boardRepository.insert(title, content, id);
        if (result != 1) {
            return 0;
        }
        return 1;
    }
}
