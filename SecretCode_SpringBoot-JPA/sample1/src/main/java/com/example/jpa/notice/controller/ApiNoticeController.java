package com.example.jpa.notice.controller;

import com.example.jpa.notice.model.NoticeModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class ApiNoticeController {

/*    @GetMapping("/api/notice")
    public String noticeString(){

        return "공지사항입니다.";
    }*/


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


}
