package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.responsitory.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;

    //글 작성 처리, 에외처리 해줌
    public void write(Board board, MultipartFile file) throws Exception {
        // 저장할 프로젝트 경로 담기
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
        UUID uuid = UUID.randomUUID(); // 식별자 랜덤 생성
        String fileName = uuid + "_" + file.getOriginalFilename(); // 식별자_파일이름, 파일이 안담긴 경우 write페이지에서 파일이름 설정
        File saveFile = new File(projectPath, fileName); // 파일을 생성해주는데 경로와 이름 설정 , 빈껍데기
        file.transferTo(saveFile);

        board.setFilename(fileName); // 파일이름 설정
        board.setFilepath("/files/"+fileName); //저장되는 경로


        boardRepository.save(board);
    }
    // 게시글 리스트 처리
    public Page<Board> boardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable) {
        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }

    // 특정 게시글 불러오기
    public Board boardView(Integer id) {
        return boardRepository.findById(id).get();
    }

    // 특정 게시글 삭제
    public void boardDelete(Integer id) {
        boardRepository.deleteById(id);
    }

}
