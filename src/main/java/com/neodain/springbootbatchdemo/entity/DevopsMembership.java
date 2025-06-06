package com.neodain.springbootbatchdemo.entity;

import java.io.Serializable;
import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "devops_membership")
@IdClass(DevopsMembership.DevopsMembershipId.class)
public class DevopsMembership {

  @Id
  @Column(name = "devops_id", length = 36)
  private String devopsId;

  @Id
  @Column(name = "member_id", length = 36)
  private String memberId;

  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false)
  private Role role;

  @Column(name = "join_date", nullable = false)
  private LocalDate joinDate;

  public enum Role {
    beginner, maintainer, manager, leader
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class DevopsMembershipId implements Serializable {
    private String devopsId;
    private String memberId;
  }

}