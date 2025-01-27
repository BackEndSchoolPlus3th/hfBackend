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

    public static final QMember member = new QMember("member1");

    public final com.ll.hfback.global.jpa.QBaseEntity _super = new com.ll.hfback.global.jpa.QBaseEntity(this);

    public final ListPath<com.ll.hfback.domain.member.alert.entity.Alert, com.ll.hfback.domain.member.alert.entity.QAlert> alerts = this.<com.ll.hfback.domain.member.alert.entity.Alert, com.ll.hfback.domain.member.alert.entity.QAlert>createList("alerts", com.ll.hfback.domain.member.alert.entity.Alert.class, com.ll.hfback.domain.member.alert.entity.QAlert.class, PathInits.DIRECT2);

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

    public final StringPath refreshToken = createString("refreshToken");

    public final ListPath<com.ll.hfback.domain.member.report.entity.Report, com.ll.hfback.domain.member.report.entity.QReport> reports = this.<com.ll.hfback.domain.member.report.entity.Report, com.ll.hfback.domain.member.report.entity.QReport>createList("reports", com.ll.hfback.domain.member.report.entity.Report.class, com.ll.hfback.domain.member.report.entity.QReport.class, PathInits.DIRECT2);

    public final EnumPath<Member.MemberRole> role = createEnum("role", Member.MemberRole.class);

    public final EnumPath<Member.MemberState> state = createEnum("state", Member.MemberState.class);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}
