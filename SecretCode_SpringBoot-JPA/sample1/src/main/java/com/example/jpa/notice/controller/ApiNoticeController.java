package com.example.jpa.notice.controller;

import com.example.jpa.notice.entity.Notice;
import com.example.jpa.notice.model.NoticeInput;
import com.example.jpa.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ApiNoticeController {


    private final NoticeRepository noticeRepository;


    /*
    @GetMapping("/api/notice")
    public String noticeString(){

        return "공지사항입니다.";
    }


    @GetMapping("/api/notice")
    public NoticeModel notice(){

        LocalDateTime regDate = LocalDateTime.of(2022, 3, 15 , 0, 0);

        NoticeModel notice = new NoticeModel();

        notice.setId(1);
        notice.setTitle("공지사항입니다.");
        notice.setContents("공지사항 내용입니다.");
        notice.setRegDate(regDate);

        return notice;
    }


    @GetMapping("/api/notice")
    public List<NoticeModel> noticeList(){

        List<NoticeModel> noticeList = new ArrayList<>();

        noticeList.add(NoticeModel.builder()
                .id(1)
                .title("공지사항입니다.")
                .contents("공지사항내용입니다.")
                .regDate(LocalDateTime.of(2021,1,30,0,0))
                .build());

        noticeList.add(NoticeModel.builder()
                .id(2)
                .title("두번째 공지사항입니다.")
                .contents("두번째 공지사항내용입니다.")
                .regDate(LocalDateTime.of(2021,1,31,0,0))
                .build());

        return noticeList;

    }
    */
    @GetMapping("/api/notice")
    public List<NoticeInput> notice(){
        List<NoticeInput> noticeList = new ArrayList<>();

        return noticeList;
    }

    @GetMapping("/api/notice/count")
    public int noticeCount(){

        return 10;
    }
    /*
    @PostMapping("/api/notice")
    public NoticeModel addNotice(@RequestParam String title, @RequestParam String contents){
        NoticeModel notice = NoticeModel.builder()
                .id(1)
                .title(title)
                .contents(contents)
                .regDate(LocalDateTime.of(2022,03,17,1,1))
                .build();

        return notice;
    }
    */
    /*
    @PostMapping("/api/notice")
    public NoticeModel addNotice(NoticeModel noticeModel){
        noticeModel.setId(2);
        noticeModel.setRegDate(LocalDateTime.now());
        return noticeModel;
    }
    */

    /*
    @PostMapping("/api/notice")
    public NoticeModel addNotice(@RequestBody NoticeModel noticeModel){
        noticeModel.setId(2);
        noticeModel.setRegDate(LocalDateTime.now());
        return noticeModel;
    }
    */


    @PostMapping("/api/notice")
    public Notice addNotice(@RequestBody NoticeInput noticeInput){

        Notice notice = Notice.builder()
                .title(noticeInput.getTitle())
                .contents(noticeInput.getContents())
                .regDate(LocalDateTime.now())
                .build();

        return noticeRepository.save(notice);
    }

}
