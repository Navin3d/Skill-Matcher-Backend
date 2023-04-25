package com.example.skillmatcherbackend.controller;

import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.skillmatcherbackend.exception.DefaultException;
import com.example.skillmatcherbackend.model.CandidateType;
import com.example.skillmatcherbackend.model.IdResponse;
import com.example.skillmatcherbackend.model.ProfileResponse;
import com.example.skillmatcherbackend.model.document.UserDocument;
import com.example.skillmatcherbackend.model.dto.ProfileDTO;
import com.example.skillmatcherbackend.model.dto.ProfilesRes;
import com.example.skillmatcherbackend.repository.UserRepository;
import com.example.skillmatcherbackend.service.ProfileService;
import com.example.skillmatcherbackend.service.UserSession;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profiles")
public class ProfileController {
    private final ProfileService profileService;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepo;

    private final UserSession userSession;

    @GetMapping
    public ProfilesRes getAllProfiles(@RequestParam(required = false) final String skills,
                                      @RequestParam(required = false) final Integer experienceFrom,
                                      @RequestParam(required = false) final Integer experienceTo,
                                      @RequestParam(required = false) final CandidateType candidateType,
                                      @RequestParam(required = false) final Boolean openToTravel,
                                      @RequestParam(required = false) final String city,
                                      @RequestParam(required = false, defaultValue = "0") final Long page) throws ParseException {
        return profileService.getAllProfiles(skills, experienceFrom, experienceTo, candidateType, openToTravel, city, page);
    }

    @GetMapping("/{id}")
    public ProfileResponse getProfileById(@PathVariable final String id) throws DefaultException, ParseException {
        return profileService.getProfileById(id);
    }

    @PostMapping("/reset")
    public void resetPassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        UserDocument user = userRepo.findById(userSession.getUser().getId()).get();
        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepo.save(user);
        } else {
            return;
        }
    }

    @PatchMapping
    public IdResponse updateProfile(@RequestBody final ProfileDTO profileDTO) throws DefaultException {
        return new IdResponse(profileService.updateProfile(profileDTO).getId());
    }

    @DeleteMapping("/{id}")
    public ProfileResponse deleteUserById(@PathVariable final String id) throws DefaultException {
        return profileService.deleteUserById(id);
    }

    @PostMapping("/photo")
    public ResponseEntity<?> updateProfilePhoto(@RequestPart(required = false) final String id,
                                                @RequestPart(required = false) final MultipartFile photo) throws DefaultException {
        profileService.compressAndUpdateProfilePhoto(id, photo);
        return ResponseEntity.ok().build();
    }
}
