package com.clane.walletservice.model;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Table(name = "users", indexes = @Index(columnList = "id, email_address"))
@Entity
@Data
public class User extends AuditEntity implements Serializable {

    @NotNull
    @NotBlank
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @NotBlank
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @NotBlank
    @Column(name = "email_address", unique = true)
    private String emailAddress;

    @NotNull
    @NotBlank
    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "house_address")
    private String houseAddress;

    @Column(name ="is_email_confirmed", columnDefinition = "boolean default false")
    private boolean emailConfirmed;

    @Column(name = "bvn")
    private String bvn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kyc_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Kyc kyc;

    @Column(name = "identity_doc_url")
    private String identityDocUrl;

}
