package com.neodain.springbootbatchdemo.store.jpo;

import java.time.LocalDateTime;

// import org.springframework.beans.BeanUtils;

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

  // // BeanUtils.copyProperties(this, devops); // 생성자 복사 대신 사용 가능
  // public Devops copy() {
  //   // Devops devops = new Devops();
  //   // BeanUtils.copyProperties(this, devops); // source와 target 객체의 필드 이름이 동일하지 않으면
  //   // 복사되지 않습니다.
  //   // return devops;

  //   ModelMapper modelMapper = new ModelMapper();
  //   Devops devops = modelMapper.map(this, Devops.class);
  //   return devops;
  // }
}
