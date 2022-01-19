package kr.co.dong.board;

import kr.co.dong.board.dto.BoardDTO;
import kr.co.dong.board.dto.PageRequestDTO;
import kr.co.dong.board.dto.PageResultDTO;
import kr.co.dong.board.service.BoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceTest {
    @Autowired
    private BoardService boardService;

    //@Test
    public void testInsert(){
        BoardDTO boardDTO = BoardDTO.builder().title("삽입").content("삽입을 테스트").writerEmail("user1@naver.com").build();
        Long bno = boardService.register(boardDTO);

        System.out.println("삽입한 글 번호 : " + bno);

    }

    //@Test
    public void testList(){
        PageRequestDTO dto = new PageRequestDTO();
        PageResultDTO<BoardDTO, Object[]> result = boardService.getList(dto);
        for(BoardDTO boardDTO : result.getDtoList()){
            System.out.println(boardDTO);
        }
        System.out.println(result.getPageList());

    }
    //@Test
    public void getTest(){
        BoardDTO boardDTO = boardService.get(102L);
        System.out.println(boardDTO);
    }

    //@Test
    public void deleteTest(){
        boardService.removeWithReplies(7L);
    }

    @Test
    public void modify(){
        BoardDTO dto = BoardDTO.builder().bno(1L).title("수정 테스트").content("수정 테스트 content").build();

        boardService.modify(dto);
    }




}
