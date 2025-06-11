package com.neodain.springbootbatchdemo.store.jpo;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Gender gender;

  @Column(name = "birth_day")
  private Date birthday;

  @Column(name = "phone_num", nullable = false, length = 20, unique = true)
  private String phoneNum;

  @Column(nullable = false, length = 50, unique = true)
  private String email;

  @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
  private List<DevopsMembership> devops_membership;

  @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
  private List<Address> addresses;

  public enum Gender {
    male, female;

    public static Gender fromString(String gender) {
      for (Gender g : Gender.values()) {
        if (g.name().equalsIgnoreCase(gender)) {
          return g;
        }
      }
      throw new IllegalArgumentException("Invalid gender: " + gender);
    }
  }

}
