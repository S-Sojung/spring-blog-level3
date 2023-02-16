package shop.mtcoding.blog.model;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import shop.mtcoding.blog.dto.love.LoveResp.LoveCountRespDto;

@Mapper
public interface LoveRepository {

        public List<Love> findAll();

        public Love findById(int id);

        public int insert(@Param("love") String love, @Param("userId") int userId,
                        @Param("boardId") int boardId);

        public Love findByUserIdAndBoardId(@Param("userId") int userId,
                        @Param("boardId") int boardId);

        public LoveCountRespDto findByBoardIdCount(@Param("boardId") int boardId);

        public int updateById(@Param("id") int id, @Param("love") String love);

        public int deleteById(int id);
}
