package org.fitri.accounting.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Lob;
import lombok.Data;

@Entity
@Data
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer phone;
    private String address;
    @ManyToOne
    private Login email;
    @ManyToOne
    private Login password;
    @ManyToOne
    private Login companyName;
    @Lob
    private byte[] profileImage;
}
