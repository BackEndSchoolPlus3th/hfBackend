package com.ll.hfback.domain.member.auth.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSocialAccount is a Querydsl query type for SocialAccount
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSocialAccount extends EntityPathBase<SocialAccount> {

    private static final long serialVersionUID = -1575982551L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSocialAccount socialAccount = new QSocialAccount("socialAccount");

    public final BooleanPath googleActive = createBoolean("googleActive");

    public final DateTimePath<java.time.LocalDateTime> googleCreateDate = createDateTime("googleCreateDate", java.time.LocalDateTime.class);

    public final StringPath googleEmail = createString("googleEmail");

    public final DateTimePath<java.time.LocalDateTime> googleModifyDate = createDateTime("googleModifyDate", java.time.LocalDateTime.class);

    public final StringPath googleProviderId = createString("googleProviderId");

    public final BooleanPath kakaoActive = createBoolean("kakaoActive");

    public final DateTimePath<java.time.LocalDateTime> kakaoCreateDate = createDateTime("kakaoCreateDate", java.time.LocalDateTime.class);

    public final StringPath kakaoEmail = createString("kakaoEmail");

    public final DateTimePath<java.time.LocalDateTime> kakaoModifyDate = createDateTime("kakaoModifyDate", java.time.LocalDateTime.class);

    public final StringPath kakaoProviderId = createString("kakaoProviderId");

    public final com.ll.hfback.domain.member.member.entity.QMember member;

    public QSocialAccount(String variable) {
        this(SocialAccount.class, forVariable(variable), INITS);
    }

    public QSocialAccount(Path<? extends SocialAccount> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSocialAccount(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSocialAccount(PathMetadata metadata, PathInits inits) {
        this(SocialAccount.class, metadata, inits);
    }

    public QSocialAccount(Class<? extends SocialAccount> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.ll.hfback.domain.member.member.entity.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

