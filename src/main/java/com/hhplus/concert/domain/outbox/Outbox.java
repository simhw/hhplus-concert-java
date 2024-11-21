package com.hhplus.concert.domain.outbox;

import com.hhplus.concert.domain.BaseTimeEntity;
import jakarta.persistence.*;
import jdk.jfr.Description;
import lombok.Getter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@Table(name = "outbox")
public class Outbox extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "outbox_id")
    private Long id;

    @Description("발신자, 도메인")
    private String publisher;

    @Description("발신자 아이디")
    private Long publisherId;

    @Description("수신자, 토픽")
    private String receiver;

    @Description("이벤트 타입")
    private String title;

    @Lob
    @Description("이벤트 내용")
    private String message; // {"id": id, "reservation_id" 1 .. }

    @Description("발신 완료 여부")
    private boolean isPublished;

    protected Outbox() {
    }

    public Outbox(String publisher, Long publisherId, String title, String message) {
        this.publisher = publisher;
        this.publisherId = publisherId;
        this.title = title;
        this.message = message;
        this.isPublished = false;
    }

    public void published() {
        this.isPublished = true;
    }

    public void received(String receiver) {
        this.receiver = receiver;
    }
}
