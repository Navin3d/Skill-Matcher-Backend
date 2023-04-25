package com.example.skillmatcherbackend.model.dto;

import java.util.List;

import com.example.skillmatcherbackend.model.ProfileResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfilesRes {
    private List<ProfileResponse> profileResponseList;
    private int size;
}
