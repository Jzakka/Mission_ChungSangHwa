package com.ll.gramgram.boundedContext.notification.controller;

import com.ll.gramgram.base.rq.Rq;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.notification.entity.Notification;
import com.ll.gramgram.boundedContext.notification.service.NotificationService;
import jakarta.persistence.ManyToOne;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/usr/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final Rq rq;
    private final NotificationService notificationService;

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public String showList(Model model) {
        if (!rq.getMember().hasConnectedInstaMember()) {
            return rq.redirectWithMsg("/usr/instaMember/connect", "먼저 본인의 인스타그램 아이디를 입력해주세요.");
        }

        List<Notification> notifications = notificationService.findByToInstaMember(rq.getMember().getInstaMember());

        model.addAttribute("notifications", notifications);

        return "usr/notification/list";
    }

    @GetMapping("/read/{id}")
    @PreAuthorize("isAuthenticated()")
    public String readNotification(@PathVariable("id") Long id) {
        Member actor = rq.getMember();
        Notification notification = notificationService.findById(id);
        if (actor.hasConnectedInstaMember() && actor.getInstaMember().equals(notification.getToInstaMember())) {
            notificationService.read(notification);
            return rq.redirectWithMsg("/usr/notification/list", "메시지를 읽음처리하였습니다.");
        }
        return rq.historyBack("권한이 없습니다.");
    }

    @GetMapping("/readAll")
    @PreAuthorize("isAuthenticated()")
    public String readAllNotifications() {
        Member actor = rq.getMember();
        if (actor.hasConnectedInstaMember()) {
            notificationService.readAll(actor.getInstaMember());
            return rq.redirectWithMsg("/usr/notification/list", "메시지를 모두 읽음차리하였습니다.");
        }
        return rq.redirectWithMsg("/usr/instaMember/connect", "먼저 본인의 인스타그램 아이디를 입력해주세요.");
    }
}