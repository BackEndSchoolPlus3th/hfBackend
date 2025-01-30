package com.ll.hfback.domain.group.chat.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMessageReadStatus is a Querydsl query type for MessageReadStatus
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMessageReadStatus extends EntityPathBase<MessageReadStatus> {

    private static final long serialVersionUID = 1766137257L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMessageReadStatus messageReadStatus = new QMessageReadStatus("messageReadStatus");

    public final com.ll.hfback.global.jpa.QBaseEntity _super = new com.ll.hfback.global.jpa.QBaseEntity(this);

    public final com.ll.hfback.domain.group.chatRoom.entity.QChatRoom chatRoom;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Long> lastReadMessageId = createNumber("lastReadMessageId", Long.class);

    public final com.ll.hfback.domain.member.member.entity.QMember member;

    public QMessageReadStatus(String variable) {
        this(MessageReadStatus.class, forVariable(variable), INITS);
    }

    public QMessageReadStatus(Path<? extends MessageReadStatus> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMessageReadStatus(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMessageReadStatus(PathMetadata metadata, PathInits inits) {
        this(MessageReadStatus.class, metadata, inits);
    }

    public QMessageReadStatus(Class<? extends MessageReadStatus> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.chatRoom = inits.isInitialized("chatRoom") ? new com.ll.hfback.domain.group.chatRoom.entity.QChatRoom(forProperty("chatRoom"), inits.get("chatRoom")) : null;
        this.member = inits.isInitialized("member") ? new com.ll.hfback.domain.member.member.entity.QMember(forProperty("member")) : null;
    }

}

