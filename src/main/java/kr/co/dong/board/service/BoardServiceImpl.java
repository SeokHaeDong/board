package kr.co.dong.board.service;

import kr.co.dong.board.dto.BoardDTO;
import kr.co.dong.board.dto.PageRequestDTO;
import kr.co.dong.board.dto.PageResultDTO;
import kr.co.dong.board.entity.Board;
import kr.co.dong.board.entity.Member;
import kr.co.dong.board.repository.BoardRepository;
import kr.co.dong.board.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService {

    private final BoardRepository repository;
    private final ReplyRepository replyRepository;

    @Override
    public Long register(BoardDTO dto) {
        log.info(dto);
        Board board = dtoToEntity(dto);
        repository.save(board);
        return board.getBno();
    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

        // Repository 메서드를 호출해서 결과 가져오기
        log.info(pageRequestDTO);
        Page<Object[]> result = repository.getBoardWithReplyCount(pageRequestDTO.getPageable(Sort.by("bno").descending()));

        Function<Object[], BoardDTO> fn = (en -> entityToDTO((Board)en[0],(Member)en[1], (Long)en[2]));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public BoardDTO get(Long bno) {
        Object result = repository.getBoardByBno(bno);
        Object[] ar = (Object[]) result;

        return entityToDTO((Board)ar[0], (Member) ar[1], (Long) ar[2]);
    }

    @Transactional
    @Override
    public void removeWithReplies(Long bno) {
        replyRepository.deleteByBno(bno);
        replyRepository.deleteById(bno);

    }

    @Transactional
    @Override
    public void modify(BoardDTO boardDTO) {
        Optional<Board> board = repository.findById(boardDTO.getBno());

        if(board.isPresent()){
            board.get().changeTitle(boardDTO.getTitle());
            board.get().changeContent(boardDTO.getContent());

            repository.save(board.get());
        }

    }
}
