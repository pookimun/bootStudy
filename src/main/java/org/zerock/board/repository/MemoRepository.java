package org.zerock.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.board.entity.Memo;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    // JPA 레포지토리는 인터페이스 자체이고 JpaRepository 인터페이스를 상속하는 것 만드로 모든 작없이 끝남.
    // extends JpaRepository<엔티티, pk타입>

    // insert 작업 : save(엔티티 객체)
    // select 작업 : findById(키 타입), getOne(키 타입)
    // update 작업 : save(엔티티 객체)
    // delete 작업 : deleteById(키 타입),  delete(엔티티 객체)

    //쿼리 메서드는 메서드명 자체가 쿼리문으로 동작
    //List<Memo> 리턴타입 -> 리스트 타입에 객체는 memo
    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);
        //매개값으로 받은 from ~ to까지 select를 진행, 리스트로 리턴하는 쿼리 메서드
    
    //Page<Memo> 리턴타입 -> 페이징 타입에 객체는 memo 
    Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);
    //매개값으로 받은 from ~ to까지 select를 진행, 페이징 타입으로 리턴하는 쿼리 메서드

    //10보다 작은 데이터 삭제
    void deleteMemoByMnoLessThan(Long num);

    //@Query는 순수한 sql 쿼리문으로 작성, 테이블명이 아닌 엔티티명으로 작성해야
    @Query("SELECT m FROM Memo m ORDER BY m.mno DESC")
    List<Memo> getListDesc(); //내가 만든 메서드명

    //1. (매개값이 있는) @Query문 : 값 (타입으로 받음)
    @Query("UPDATE Memo m SET m.memoText = :memoText WHERE m.mno = :mno")
    int updateMemotext(@Param("mno") Long mno, @Param("memoText") String memoText);

    //2. 매개값이 객체(빈)로 들어올 경우
    @Query("UPDATE Memo m SET m.memoText =: #{memoBean.memoText} WHERE mno =: #{memoBean.mno}")
    int updateMemoBean(@Param("memoBean") Memo memo);

    //@Query 메서드로 페이징 처리 해보기 -> 리턴 타입이 Page<Memo>
    //countQuery: 페이징 처리 용도
    @Query(value = "SELECT m FROM Memo m WHERE m.mno >: mno", countQuery = "SELECT count (m) FROM Memo m WHERE m.mno>: mno ")
    Page<Memo> getListWithQuery(Long mno, Pageable pageable);

    //DB에 없는 값 처리 (e.g. 날짜)
    @Query(value = "SELECT m.mno, m.memoText, CURRENT_DATE FROM Memo m WHERE m.mno >: mno", countQuery = "SELECT count (m) FROM Memo m WHERE m.mno>: mno")
    //Object: 최상위 클래스로 모든 값 처리 가능
    Page<Object[]> getListWithQueryObject(Long mno, Pageable pageable);

    //Native Sql 처리: DB용 복잡한 쿼리, 엔티티명이 아닌 테이블명으로 작성해야
    @Query(value = "SELECT * FROM memo WHERE mno > 0", nativeQuery = true)
    List<Object[]> getNativeResult();
}