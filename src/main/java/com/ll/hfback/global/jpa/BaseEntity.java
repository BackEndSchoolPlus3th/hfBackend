package com.ll.hfback.global.jpa;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@MappedSuperclass
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Getter
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BaseEntity {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Setter(AccessLevel.PROTECTED)
  @EqualsAndHashCode.Include
  private Long id;

  @CreatedDate
  @Setter(AccessLevel.PRIVATE)
  private LocalDateTime createDate;
}
