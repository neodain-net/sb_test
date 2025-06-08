package com.neodain.springbootbatchdemo.store.jpo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "devops_member")
public class DevopsMember {

  @Id
  @Column(name = "member_id", length = 36)
  private String memberId;

  @Column(nullable = false, length = 50)
  private String name;

  @Column(length = 50)
  private String nickname;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Gender gender;

  @Column(name = "birth_day")
  private java.sql.Date birthday;

  @Column(name = "phone_num", nullable = false, length = 20, unique = true)
  private String phoneNum;

  @Column(nullable = false, length = 50, unique = true)
  private String email;

  public enum Gender {
    male, female
  }

}
