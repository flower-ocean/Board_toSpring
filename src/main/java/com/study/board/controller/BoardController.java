package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller // 스프링이 재실행을 할 때 여기가 컨트롤러인 걸 인지함

public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/board/write") // 어떤 url로의 접근 지정 => localhost:8080/board/write
    public String boardWriteform() {
        return "boardwrite";
    }

    @PostMapping("/board/writepro")
    public String boardWritePro(Board board, Model model, MultipartFile file) throws Exception{
        boardService.write(board, file);

        model.addAttribute("message", "글 작성이 완료 되었습니다.");
        model.addAttribute("searchUrl", "/board/list");
        return "message";
    }

    @GetMapping("/board/list") // localhost:8080/board/list?page=0&size10
    // 데이터를 담아서 우리가 보는 페이지로 전송
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            String searchKeyword) {
                            // 페이징 처리 설정 (페이지 시작, 한 페이지에 몇 개 보여줄지, 페이지 처리 기준, 페이지 정렬)


        // 검색 키워드가 들어왔을 때랑 안들어왔을 때 구분
        Page<Board> list = null;
        if (searchKeyword == null) {
            list = boardService.boardList(pageable);
        } else {
            list = boardService.boardSearchList(searchKeyword, pageable);
        }

        int nowPage = list.getPageable().getPageNumber() +1; // '0'부터 시작하기 때문에 '1'을 더 해줌
        int startPage =Math.max(nowPage -4, 1); // 둘 중에 큰 쪽 반환, 1보다 작으면 1을 반환
        int endPage = Math.min(nowPage +5, list.getTotalPages());

        // 변수 다 넘겨주기
        model.addAttribute("list", list); // list라는 이름으로 보냄, 뒤에거를 담아서
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "boardlist";
    }

    @GetMapping("/board/view") //localhost:8080/board/view?id=1 =>쿼리스트링
    public String boardView(Model model, Integer id) {

        model.addAttribute("board", boardService.boardView(id));
        return "boardview";
    }

    @GetMapping("/board/delete") //localhost:8080/board/delete?id=1
    public String boardDelete(Integer id) {
        boardService.boardDelete(id);
        return "redirect:/board/list";
    }

    @GetMapping("/board/modify/{id}") // 아이디를 인식해서 인티저 형식으로 들어감
    public String boardModify(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("board", boardService.boardView(id));
        return "boardmodify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Board board, Model model, MultipartFile file) throws Exception{
        // 기존에 있던 게시글을 검색
        Board boardTemp = boardService.boardView(id); // 기존에 잇던 게시글 내용 가저오기
        // 기존에 잇던 내용에 새로 덮어씌우기
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());
        // 내용 저장
        boardService.write(boardTemp, file);

        model.addAttribute("message", "글 수정이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");
        return "message";
    }
}
