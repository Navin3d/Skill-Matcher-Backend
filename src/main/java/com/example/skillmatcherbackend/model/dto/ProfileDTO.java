package com.example.skillmatcherbackend.model.dto;

import java.util.List;

import com.example.skillmatcherbackend.model.CandidateType;
import com.example.skillmatcherbackend.model.document.Mobile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTO {
    private String id;
    private List<String> skills;
    private List<String> certifications;
    private List<String> education;
    private List<String> contentCreation;
    private String resume;
    private String resumeFileName;
    private String city;
    private String country;
    private Boolean openToTravel;
    private Integer experience;
    private CandidateType candidateType;
    private List<String> clients;
    private String linkedIn;
    private Mobile mobile;
    private String expectedCommercials;
    private String aboutMe;
    private Boolean isInActive;
    private String fullName;
}
