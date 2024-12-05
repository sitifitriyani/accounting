package org.fitri.accounting.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long phone;
    private String address;
@OneToOne(cascade = CascadeType.ALL)
@JoinColumn(name = "login_id", referencedColumnName = "id")    
private Login login;
    private String profileImage;
}
