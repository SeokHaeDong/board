package kr.co.dong.board.service;

import com.fasterxml.jackson.databind.util.ArrayBuilders;
import kr.co.dong.board.dto.BoardDTO;
import kr.co.dong.board.dto.PageRequestDTO;
import kr.co.dong.board.dto.PageResultDTO;
import kr.co.dong.board.entity.Board;
import kr.co.dong.board.entity.Member;

public interface BoardService {
    // 이 메서드를 인터페이스에서 선언하고 클래스에서 구현해도 되고 인터페이스에 만든 이유는 클래스에는 실제 비지니스 로직에 관련된 메서드만 존재하고 하고 싶어서 임
    // 이러한 메서드를 별도의 클래스에 static 메서드로 만들어 두어도 되는데 이러한 경우 클래스 이름에는 Wrapper 를 붙이는 것이 좋음

    // Board DTO 를 Board Entity 로 변환해주는 메서드
    default Board dtoToEntity(BoardDTO dto){
        Member member = Member.builder().email(dto.getWriterEmail()).build();

        Board board = Board.builder().bno(dto.getBno()).title(dto.getTitle()).content(dto.getContent()).writer(member).build();

        return board;
    }
    // Board Entity 를 Board DTO 로 변환해주는 메서드
    default BoardDTO entityToDTO(Board board, Member member, Long replyCount){
        BoardDTO boardDTO = BoardDTO.builder().bno(board.getBno()).title(board.getTitle()).content(board.getContent()).regDate(board.getRegDate()).modDate(board.getModDate())
                .writerEmail(member.getEmail()).writerName(member.getName()).replyCount(replyCount.intValue()).build();

        return boardDTO;
    }
    // 게시물 등록을 위한 메서드
    Long register(BoardDTO dto);

    // 게시물 목록 보기를 위한 메서드
    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO);

    // 상세보기 메서드
    public BoardDTO get(Long bno);

    // 글번호를 이용해서 게시글을 삭제하는 메서드
    public void removeWithReplies(Long bno);

    // 수정을 위한 메서드
    public void modify(BoardDTO boardDTO);




}
