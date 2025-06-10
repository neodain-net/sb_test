package com.neodain.springbootbatchdemo.store.jpo;

import java.time.LocalDateTime;
import jakarta.persistence.FetchType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity representing the association between a {@link Devops} and a
 * {@link DevopsMember}.
 * <p>
 * This class maps the join table {@code devops_membership}. The composite
 * primary key is defined by {@link DevopsMembershipId} which combines
 * {@code devopsId} and {@code memberId}. These identifiers correspond to the
 * primary keys of the related {@link Devops} and {@link DevopsMember}
 * entities. The {@link Role} enum captures the member's role in the
 * Devops organization.
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "devops_membership")
public class DevopsMembership {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "devops_id", nullable = false)
  private Devops devops;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private DevopsMember member;

  @Enumerated(EnumType.STRING)
  @Column(name = "role_in_devops", nullable = false)
  private Role role;

  @Column(name = "join_date", nullable = false)
  private LocalDateTime joinDate;

  public DevopsMember getMember() {
    return this.member;
  }

  public enum Role {
    beginner, maintainer, manager, leader;

    public static Role fromString(String role) {
      for (Role r : Role.values()) {
        if (r.name().equalsIgnoreCase(role)) {
          return r;
        }
      }
      throw new IllegalArgumentException("Invalid role: " + role);
    }
  }
}