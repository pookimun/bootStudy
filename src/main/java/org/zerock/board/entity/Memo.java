package org.zerock.board.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity //JPA를 통해 관리하게 되는 객체
@Table(name = "tbl_memo") //entity를 어떠한 클래스로 생성할 것인지, 이 경우에는 DB명 지정
@ToString //DB에 객체가 아닌 문자로 들어가므로
@Getter //VO와 DB가 합쳐진 형태?
@Builder //메서드.필드(값).필드(값).builder; (빌더 패턴), 아래 두 개의 생성자 필수
@AllArgsConstructor
@NoArgsConstructor
public class Memo {
    //entity: 데이터베이스에 테이블과 필드를 생성시켜 관리하는 객체
    // application.properties에 필수 항목 추가 -> 엔티티를 이용하여 JPA 활성화
    @Id //primary key 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) //
    private Long mno;
    @Column(length = 200, nullable = false)
    private String memoText;
}
