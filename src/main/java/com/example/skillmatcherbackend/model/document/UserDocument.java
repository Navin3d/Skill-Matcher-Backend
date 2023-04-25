package com.example.skillmatcherbackend.model.document;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.skillmatcherbackend.model.CandidateType;
import com.example.skillmatcherbackend.model.UserRole;

import io.hypersistence.utils.hibernate.type.search.PostgreSQLTSVectorType;
import lombok.Data;

@Entity
@Table(name = "userDocument")
@Data
@TypeDef(name = "tsvector", typeClass = PostgreSQLTSVectorType.class)
public class UserDocument implements UserDetails {
	
    private static final long serialVersionUID = 2794308703823451204L;
    
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
	
	@Column(columnDefinition="TEXT")
    private String fullName;
    
	@Column(columnDefinition="TEXT")
    private String email;

    @Column(columnDefinition="BOOLEAN")
    private Boolean isInActive;
    
    @Column(columnDefinition="TEXT")
    private String password;

    @Column(columnDefinition="TEXT")
    private String photo;

    @Column(columnDefinition="TEXT")
    private String resume;

    @Column(columnDefinition="TEXT")
    private String resumeFileName;
    
    @Column(columnDefinition="TEXT")
    private String city;
    
    @Column(columnDefinition="TEXT")
    private String country;
    
    @Column(columnDefinition="TEXT")
    private String mobile;
    
    @Column(columnDefinition="TEXT")
    private String linkedIn;
    
    @Column(columnDefinition="BOOLEAN")
    private Boolean openToTravel;
    
    @Column(columnDefinition="INTEGER")
    private Integer careerStartYear;
    
    @Column(columnDefinition="TEXT")
    private String expectedCommercials;
    
    @Column(columnDefinition="TEXT")
    private String aboutMe;
    
    @Enumerated(value = EnumType.STRING)
    private UserRole role;
    
    @Enumerated(value = EnumType.STRING)
    private CandidateType candidateType;
    
    @Column(columnDefinition="TEXT")
    private String clients;
    
    @Column(columnDefinition="TEXT")
    private String certifications;
    
    @Column(columnDefinition="TEXT")
    private String education;
    
    @Column(columnDefinition="TEXT")
    private String contentCreation;
    
    @Column(columnDefinition="TEXT")
    private String skills;
    
    @Type(type = "tsvector")
    @Column(columnDefinition = "tsvector")
    private String fts;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
