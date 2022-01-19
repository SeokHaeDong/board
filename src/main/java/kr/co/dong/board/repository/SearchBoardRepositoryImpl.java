package kr.co.dong.board.repository;

import kr.co.dong.board.entity.Board;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {
    public SearchBoardRepositoryImpl(){
        super(Board.class);
    }

}
