package com.neodain.springbootbatchdemo.store.jpo;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "devops")
public class Devops {
  @Id
  @Column(name = "devops_id", length = 36)
  private String devopsId;

  @Column(nullable = false, length = 50)
  private String name;

  @Column(nullable = false, length = 100)
  private String intro;

  @Column(name = "foundation_time")
  private LocalDateTime foundationTime;
}
