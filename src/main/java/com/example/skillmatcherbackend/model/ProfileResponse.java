package com.example.skillmatcherbackend.model;

import java.util.List;

import com.example.skillmatcherbackend.model.document.Mobile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
    private String id;
    private String fullName;
    private String email;
    private UserRole role;
    private String photo;
    private List<String> skills;
    private String resume;
    private String resumeFileName;
    private String city;
    private String country;
    private List<String> education;
    private List<String> contentCreation;
    private List<String> certifications;
    private Boolean openToTravel;
    private Integer experience;
    private CandidateType candidateType;
    private List<String> clients;
    private String linkedIn;
    private Mobile mobile;
    private String expectedCommercials;
    private String aboutMe;

    private Boolean isInActive;
}
