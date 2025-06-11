package com.neodain.springbootbatchdemo.store.jpo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "address")
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private DevopsMember member;

  @Enumerated(EnumType.STRING)
  @Column(name = "address_type", nullable = false)
  private AddressType type;

  @Column(name = "street", nullable = false, length = 200)
  private String street;

  @Column(name = "city", nullable = false, length = 200)
  private String city;

  @Column(name = "address_line", length = 200)
  private String addressLine;

  @Column(name = "zip_code", length = 20)
  private String zipCode;

  /**
   * Enum for AddressType with utility methods for safe conversion.
   */
  public enum AddressType {
    home, work, billing, shipping;

    /**
     * Converts a string to AddressType, ignoring case.
     * Throws IllegalArgumentException if the input is invalid.
     *
     * @param type the string representation of the AddressType
     * @return the corresponding AddressType
     */
    public static AddressType fromString(String type) {
      for (AddressType addressType : AddressType.values()) {
        if (addressType.name().equalsIgnoreCase(type)) {
          return addressType;
        }
      }
      throw new IllegalArgumentException("Invalid addressType: " + type);
    }
  }

}
