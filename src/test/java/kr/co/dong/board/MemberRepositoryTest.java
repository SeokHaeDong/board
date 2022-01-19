package kr.co.dong.board;

import kr.co.dong.board.entity.Board;
import kr.co.dong.board.entity.Member;
import kr.co.dong.board.entity.Reply;
import kr.co.dong.board.repository.BoardRepository;
import kr.co.dong.board.repository.MemberRepository;
import kr.co.dong.board.repository.ReplyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@SpringBootTest
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    //@Test
    public void insertMember(){
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Member member = Member.builder().email("user" + i + "@naver.com").password("pw" + i).name("User" + i).build();

            memberRepository.save(member);
        });
    }

    //@Test
    public void insertBoard(){
        IntStream.rangeClosed(1, 100).forEach(i -> {
            //Member member = Member.builder().email("user" + i + "@naver.com").build();
            Member member = Member.builder().email("user" + i + "@naver.com").password("pw" + i).name("User" + i).build();
            Board board = Board.builder().title("제목..." + i).content("내용..." + i).writer(member).build();

            boardRepository.save(board);
        });
    }


    //@Test
    public void insertReply(){
        Random r = new Random();
        LongStream.rangeClosed(1, 300).forEach(i -> {
            Board board = Board.builder().bno((long)(r.nextInt(100) + 1)).build();
            Reply reply = Reply.builder().rno(i).text("댓글..." + i).board(board).replyer("guest").build();
            replyRepository.save(reply);
        });
    }

    //@Transactional
    /*@Transactional 없이 실행 시 에러
    no Session 에러 발생 -> ToString 은 기본적으로 자신이 참조하고 있는 데이터의 toString()을 호출하는데 지연 로딩을 사용하도록 설정 했기 때문에
    writer 속성의 값이 아직 호출되지 않은 상태에서 toString 을 호출해서 에러가 발생 --> 연관 관계가 있는 데이터의 toString 메서드 호출을 못하도록 설정*/
    /*다른 하나의 에러는 하이버네이트가 Board의 데이터를 가져오고 세션을 닫아버려서 Board의 데이터를 가져오고 세션을 닫아 버려서 Board가 참조고 있는 Member 데이터를 가져오기 못했기 때문 --> 해결하기 위해선 트랜잭션을 이용해서
    Board 데이터를 가져오고 세션을 닫지 말고 Member 데이터를 가져오도록 해주면 된다*/
    //@Test
    public void lazyLoading(){
        Optional<Board> board = boardRepository.findById(100L);
        if(board.isPresent()){
            System.out.println(board);
            System.out.println(board.get().getWriter());
        }
    }

    //@Transactional
    //@Test
    public void readReply(){
        Optional<Reply> reply = replyRepository.findById(1L);
        if(reply.isPresent()){
            System.out.println(reply);
            System.out.println(reply.get().getBoard());
        }
    }

    // Board 와 Member를 join 하는 메서드
    //@Test
    public void testJoin(){
        Object result = boardRepository.getBoardWithWriter(100L);
        Object[] arr = (Object[]) result;
        System.out.println(Arrays.toString(arr));
    }

    //@Test
    public void testJoin2(){
        List<Object[]> result = boardRepository.getBoardWithReply(37L);
        for(Object[] arr : result){
            System.out.println(Arrays.toString(arr));
        }
    }

    //@Test
    public void testBoardList(){
        // 페이징 조건 생성 - 0 페이지에 10개의 데이터를 bno 의 내림차순으로 가져오기
        Pageable pageable = PageRequest.of(1, 10, Sort.by("bno").descending());

        Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageable);
        result.get().forEach(row ->{
            Object[] arr = (Object[]) row;
            System.out.println(Arrays.toString(arr));
        });

    }


    //@Test
    public void getBoardBybnoTest(){
        Object result = boardRepository.getBoardByBno(5L);
        Object[] ar = (Object[]) result;
        System.out.println(Arrays.toString(ar));
    }



}
