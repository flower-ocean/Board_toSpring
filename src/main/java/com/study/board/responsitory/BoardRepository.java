package com.study.board.responsitory;

import com.study.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {

    // 인터페이스에 메소드 추가
    Page<Board> findByTitleContaining(String searchKeyword, Pageable pageable);
    // findBy(컬럼이름) => 정확히 입력해야 검색가능
    // findBy(칼럼이름)Containing => 정확히 입력 안하고 한 글자만 같아도 검색 가능
}
