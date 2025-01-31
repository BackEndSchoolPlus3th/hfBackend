package com.ll.hfback.domain.member.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -182293629L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final com.ll.hfback.global.jpa.QBaseEntity _super = new com.ll.hfback.global.jpa.QBaseEntity(this);

    public final ListPath<com.ll.hfback.domain.member.alert.entity.Alert, com.ll.hfback.domain.member.alert.entity.QAlert> alerts = this.<com.ll.hfback.domain.member.alert.entity.Alert, com.ll.hfback.domain.member.alert.entity.QAlert>createList("alerts", com.ll.hfback.domain.member.alert.entity.Alert.class, com.ll.hfback.domain.member.alert.entity.QAlert.class, PathInits.DIRECT2);

    public final StringPath apiKey = createString("apiKey");

    public final DatePath<java.time.LocalDate> birthday = createDate("birthday", java.time.LocalDate.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final StringPath email = createString("email");

    public final EnumPath<Member.Gender> gender = createEnum("gender", Member.Gender.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath location = createString("location");

    public final StringPath loginType = createString("loginType");

    public final BooleanPath mkAlarm = createBoolean("mkAlarm");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final StringPath profilePath = createString("profilePath");

    public final ListPath<com.ll.hfback.domain.member.report.entity.Report, com.ll.hfback.domain.member.report.entity.QReport> reports = this.<com.ll.hfback.domain.member.report.entity.Report, com.ll.hfback.domain.member.report.entity.QReport>createList("reports", com.ll.hfback.domain.member.report.entity.Report.class, com.ll.hfback.domain.member.report.entity.QReport.class, PathInits.DIRECT2);

    public final EnumPath<Member.MemberRole> role = createEnum("role", Member.MemberRole.class);

    public final com.ll.hfback.domain.member.auth.entity.QSocialAccount socialAccount;

    public final EnumPath<Member.MemberState> state = createEnum("state", Member.MemberState.class);

    public QMember(String variable) {
        this(Member.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends Member> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(Member.class, metadata, inits);
    }

    public QMember(Class<? extends Member> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.socialAccount = inits.isInitialized("socialAccount") ? new com.ll.hfback.domain.member.auth.entity.QSocialAccount(forProperty("socialAccount"), inits.get("socialAccount")) : null;
    }

}

