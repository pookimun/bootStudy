package org.zerock.board.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.board.entity.Memo;
import org.zerock.board.repository.MemoRepository;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class repositoryTests {

    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testClass() {
        //객체 주입 테스트 (MemoRepository는 인터페이스!)
        //인터페이스는 구현 객체가 있어야
        System.out.println(memoRepository.getClass().getName());
        //memoRepository에 생성된 객체의 클래스명과 이름을 알아보자
    }

    @Test
    public void testInsertDummies() {
        //memo 테이블에 더미데이터 추가
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Memo memo = Memo.builder()
                    .memoText("샘플 메모를..." + i)
                    .build(); //Memo클래스에 memoText를 1 ~ 100까지 생성
            memoRepository.save(memo); //save는 없으면 insert, 있으면 update하는 메서드
        });
    }

    @Test
    public void testSelect() {
        //mno를 이용해 정보 가져오기
        Long mno = 100L;

        Optional<Memo> result = memoRepository.findById(mno);
        //import java.util.Optional;
        //select * from 표 where mno = 100;
        System.out.println("=========================== mno = 100 ===========================");
        if (result.isPresent()) {
            Memo memo = result.get();
            System.out.println(memo); //entity는 toString되어 잇음
        }
    }

    @Transactional //변수가 호출될 때 쿼리 실행
    @Test
    public void testSelect2() {
        long mno = 100L;
        Memo memo = memoRepository.getOne(mno); //현재 차단된 메서드(보안 문제)

        System.out.println("=================================== mno = 100L; .getone(mno) ===================================");
        System.out.println(memo);
    }

    @Test
    public void updateTest() {
        Memo memo = Memo.builder()
                .mno(300L)
                .memoText("수정된 텍스트 테스트...")
                .build();

        System.out.println(memoRepository.save(memo));
    }

    @Test
    public void deleteTest() {
        Long mno = 300L;
        memoRepository.deleteById(mno);
    }

    @Test
    public void testPageDefaults() {

        Pageable pageable = PageRequest.of(0, 10); //내장된 페이징 처리
        Page<Memo> result = memoRepository.findAll(pageable);

        System.out.println(result);
    }

    @Test
    public void testPageDefault() {
        // jpa에 내장된 페이징, 정렬 기법 활용

        Sort sort1 = Sort.by("mno").descending();
        Sort sort2 = Sort.by("memoText").ascending();
        Sort sortAll = sort1.and(sort2); //내림차순 번호 & 메모 텍스트 오름차순

        Pageable pageable = PageRequest.of(0, 10, sortAll);

        Page<Memo> result = memoRepository.findAll(pageable);
        System.out.println(result);


        //Hibernate:
        //    select
        //        m1_0.mno,
        //        m1_0.memo_text
        //    from
        //        tbl_memo m1_0
        //    limit
        //        ?, ?
        //Hibernate:
        //    select
        //        count(m1_0.mno)
        //    from
        //        tbl_memo m1_0
        //Page 1 of 10 containing org.zerock.boardboot.entity.Memo instances

        System.out.println("---------------------------------------");

        System.out.println("Total Pages: "+result.getTotalPages()); // 총 몇 페이지

        System.out.println("Total Count: "+result.getTotalElements()); // 전체 개수

        System.out.println("Page Number: "+result.getNumber()); // 현재 페이지 번호

        System.out.println("Page Size: "+result.getSize()); // 페이지당 데이터 개수

        System.out.println("has next page?: "+result.hasNext());    // 다음 페이지 존재 여부

        System.out.println("first page?: "+result.isFirst());  //  시작페이지 여부

        //Page 1 of 10 containing org.zerock.boardboot.entity.Memo instances
        //---------------------------------------
        //Total Pages: 10
        //Total Count: 99
        //Page Number: 0
        //Page Size: 10
        //has next page?: true
        //first page?: true

        System.out.println("-----------------------------------");

        for(Memo memo : result.getContent()) {
            System.out.println(memo);
            //-----------------------------------
            //Memo(mno=1, memoText=Sample....1)
            //Memo(mno=2, memoText=Sample....2)
            //Memo(mno=3, memoText=Sample....3)
            //Memo(mno=4, memoText=Sample....4)
            //Memo(mno=5, memoText=Sample....5)
            //Memo(mno=6, memoText=Sample....6)
            //Memo(mno=7, memoText=Sample....7)
            //Memo(mno=8, memoText=Sample....8)
            //Memo(mno=9, memoText=Sample....9)
            //Memo(mno=10, memoText=Sample....10)
        }

    }
}
