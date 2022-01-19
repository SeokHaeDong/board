package kr.co.dong.board.repository;

import kr.co.dong.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    //Member 와 Board Entity의 join을 수행하는 메서드 생성
    // Board Entity 에는 Member Entity 와 연관 관계를 갖는 writer 가 존재

    // bno 에 해당하는 Board 를 가져올떄 Member 에 대한 정보도 가져오기
    @Query("select b, w from Board b left join b.writer w where b.bno = :bno")
    Object getBoardWithWriter(@Param("bno") Long bno);


    // Reply 와 Board Entity 의 JOIN을 수행하는 메서드 생성
    // Board Entitu 에서는 Reply Entity 와 연관 관계를 가지고 있지 않음
    // 양쪽의 공통 속성을 찾아야 함
    // Reply 가 Board 정보를 board 라는 속성을 가지고 있음

    @Query("select b, r from Board b left join Reply r ON r.board = b where b.bno = :bno")
    // 하나의 게시글에 댓글이 여러 개일 수 있기 때문에 리턴 타입은 List
    List<Object []> getBoardWithReply(@Param("bno") Long bno);

    // 목록보기를 위한 메서드 선언
    // JPQL 에서는 Page 단위로 리턴 할 떄 countQuery 가 필수
    @Query(value = "select b, w, count(r) from Board b LEFT join b.writer w LEFT join Reply r ON r.board = b GROUP BY b", countQuery = "select count(b) from Board b")
    Page<Object []> getBoardWithReplyCount(Pageable pageable);

    // 게시글 상세보기를 위한 메서드
    @Query("select b, w, count(r) from Board b left join b.writer w left  join  Reply r on r.board = b where b.bno = :bno")
    Object getBoardByBno(@Param("bno") Long bno);


}
