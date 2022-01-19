package kr.co.dong.board.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class PageResultDTO<DTO, EN> {

    private List<DTO> dtoList;

    private int totalPage;

    private int page;

    private int size;

    private int start, end;

    private boolean prev, next;

    private List<Integer> pageList;

    private  Page<EN> result;

    // 생성자
    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn){
        this.result = result;
        // Entity 로 넘어온 결과를 DTO로 변환
        dtoList = result.stream().map(fn).collect(Collectors.toList());
        // 전체 페이지 개수 구하기
        totalPage = result.getTotalPages();
        // 페이지 번호 목록 구하기
        makePageList(result.getPageable());

    }

    // 페이지 번호 목록을 만들어주는 메서드
    private void makePageList(Pageable pageable){
        // 현제 페이지
        this.page = pageable.getPageNumber() + 1; // 0부터 시작하므로 1을 추가
        // 페이지 당 출력되는 데이터 개수
        this.size = pageable.getPageSize();

        // 시작 페이지 번호 와 종료 페이지 번호 계산
        // 여기서 10은 페이지 번호 출력 개수
        // 입력받아서 변경하고자 하면
        //int tempEnd = (int)(Math.ceil(page/(double)변수명)) * 변수명;
        int tempEnd = (int)(Math.ceil(page/10.0)) * 10;
        start = tempEnd - 9;
        prev = start > 1;




        end = totalPage > tempEnd ? tempEnd : totalPage;
        next = totalPage > tempEnd;
//        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
        pageList = new ArrayList<>();
        for( int i = start; i <= end; i=i+1){
            pageList.add(i);
        }

    }







}
