package com.neodain.springbootbatchdemo.store.jpo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "country")
public class Country {

    @Id
    @Column(name = "country_code", length = 2)
    private String countryCode;

    @Column(name = "country_name", nullable = false, length = 100)
    private String countryName;
}