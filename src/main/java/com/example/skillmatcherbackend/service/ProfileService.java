package com.example.skillmatcherbackend.service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.skillmatcherbackend.exception.DefaultException;
import com.example.skillmatcherbackend.model.CandidateType;
import com.example.skillmatcherbackend.model.ProfileResponse;
import com.example.skillmatcherbackend.model.document.Mobile;
import com.example.skillmatcherbackend.model.document.UserDocument;
import com.example.skillmatcherbackend.model.dto.ProfileDTO;
import com.example.skillmatcherbackend.model.dto.ProfilesRes;
import com.example.skillmatcherbackend.model.dto.SignUpDTO;
import com.example.skillmatcherbackend.repository.CustomJDBCTemplate;
import com.example.skillmatcherbackend.repository.UserRepository;
import com.example.skillmatcherbackend.utils.ImageCompressor;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService implements UserDetailsService {
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private static final int PROFILES_SAMPLE_COUNT = 10;
	
	private final CustomJDBCTemplate customJDBCTemplate;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return userRepository.findByEmail(email);
	}

	private UserDocument getUserByIdIfExists(final String id) throws DefaultException {
		if (userRepository.findById(id).isEmpty()) {
			throw new DefaultException("User does not exist");
		} else {
			return userRepository.findById(id).get();
		}
	}

	public ProfileResponse getProfileById(final String id) throws DefaultException, ParseException {
		final UserDocument userDocument = userRepository.findById(id)
				.orElseThrow(() -> new DefaultException("User does not exist"));
		return getProfileResponseByUser(userDocument);
	}

	private List<String> stringToList(String data) throws ParseException {
		List<String> returnValue = new ArrayList<>();
		
		if(data != null) {
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(data);
			JSONArray array = (JSONArray) obj;
			
			array.iterator().forEachRemaining(skill -> {
				returnValue.add(skill.toString());
			});
		}

		return returnValue;
	}

	private String listToString(List<String> data) {
		JSONArray jarr = new JSONArray();
		data.forEach(sill -> {
			jarr.add(sill.toString());
		});
		return jarr.toJSONString();
	}

	private String mobileToJSON(Mobile mobile) {
		JSONObject obj = new JSONObject();
		obj.put("country_code", mobile.getCountry_code());
		obj.put("phoneNumber", mobile.getPhoneNumber());
		return obj.toJSONString();
	}
	
	private Mobile jsonToMobile(String mobileStr) throws ParseException {
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(mobileStr);
		JSONObject jObj = (JSONObject) obj;
		
		Mobile mob = new Mobile();
		try {
			mob.setCountry_code(jObj.get("country_code").toString());
		} catch(NullPointerException e) {
			mob.setCountry_code("");
		}
		mob.setPhoneNumber(jObj.get("phoneNumber").toString());
		
		return mob;
	}

	private boolean photoNotUpdated(final String id, final MultipartFile multipartFile) {
		return id == null || multipartFile == null;
	}

	private UserDocument getUserByProfileDTO(final ProfileDTO profileDTO) {
		final UserDocument userDocument = new UserDocument();
		userDocument.setId(profileDTO.getId());
		userDocument.setCity(profileDTO.getCity());
		userDocument.setResume(profileDTO.getResume());
		userDocument.setFullName(profileDTO.getFullName());
		userDocument.setResumeFileName(profileDTO.getResumeFileName());
		userDocument.setCountry(profileDTO.getCountry());
		userDocument.setOpenToTravel(profileDTO.getOpenToTravel());
		userDocument.setEducation(listToString(profileDTO.getEducation()));
		userDocument.setCertifications(listToString(profileDTO.getCertifications()));
		userDocument.setContentCreation(listToString(profileDTO.getContentCreation()));
		userDocument.setSkills(listToString(profileDTO.getSkills()));
		userDocument.setFts(listToString(profileDTO.getSkills()));
		userDocument.setIsInActive(profileDTO.getIsInActive());
		
		userDocument.setMobile(mobileToJSON(profileDTO.getMobile()));
		
		userDocument.setExpectedCommercials(profileDTO.getExpectedCommercials());
		userDocument.setAboutMe(profileDTO.getAboutMe());
		Integer careerStartYear = profileDTO.getExperience() != null
				? Year.now().getValue() - profileDTO.getExperience()
				: null;
		userDocument.setCareerStartYear(careerStartYear);
		userDocument.setLinkedIn(profileDTO.getLinkedIn());
		userDocument.setClients(listToString(profileDTO.getClients()));
		userDocument.setCandidateType(profileDTO.getCandidateType());
		return userDocument;
	}

	private ProfileResponse getProfileResponseByUser(final UserDocument userDocument) throws ParseException {
		final ProfileResponse profileResponse = new ProfileResponse();
		profileResponse.setId(userDocument.getId());
		profileResponse.setEmail(userDocument.getEmail());
		profileResponse.setFullName(userDocument.getFullName());
		profileResponse.setResume(userDocument.getResume());
		profileResponse.setResumeFileName(userDocument.getResumeFileName());
		profileResponse.setCountry(userDocument.getCountry());
		profileResponse.setOpenToTravel(userDocument.getOpenToTravel());
		profileResponse.setCity(userDocument.getCity());
		profileResponse.setPhoto(userDocument.getPhoto());
		profileResponse.setRole(userDocument.getRole());
		profileResponse.setIsInActive(userDocument.getIsInActive());

		if (userDocument.getCertifications() != null)
			profileResponse.setCertifications(stringToList(userDocument.getCertifications()));
		if (userDocument.getEducation() != null)
			profileResponse.setEducation(stringToList(userDocument.getEducation()));
		if (userDocument.getContentCreation() != null)
			profileResponse.setContentCreation(stringToList(userDocument.getContentCreation()));
		if (userDocument.getSkills() != null)
			profileResponse.setSkills(stringToList(userDocument.getSkills()));
		if (userDocument.getClients() != null)
			profileResponse.setClients(stringToList(userDocument.getClients()));

		if (userDocument.getMobile() != null) {
			String mobile = userDocument.getMobile();
			profileResponse.setMobile(jsonToMobile(mobile));
		}
		profileResponse.setAboutMe(userDocument.getAboutMe());
		profileResponse.setCandidateType(userDocument.getCandidateType());

		profileResponse.setIsInActive(userDocument.getIsInActive());
		profileResponse.setExpectedCommercials(userDocument.getExpectedCommercials());
		if (userDocument.getCareerStartYear() != null) {
			profileResponse.setExperience(Year.now().getValue() - userDocument.getCareerStartYear());
		}
		profileResponse.setLinkedIn(userDocument.getLinkedIn());
		return profileResponse;
	}

	private boolean hasNoFiltersEnabled(final String skills, final Integer experienceFrom, final Integer experienceTo,
			final CandidateType candidateType, final Boolean openToTravel, final String city) {
		return skills == null && experienceFrom == null && experienceTo == null && candidateType == null
				&& openToTravel == null && city == null;
	}

	private static boolean isSkillsFilterEmpty(final String skills) {
		return skills == null || skills.equals("") || skills.equals(" ");
	}

	private ProfilesRes getAllProfilesSampleWithoutFilters(Long page) throws ParseException {
		final List<ProfileResponse> profileResponseList = new ArrayList<>();
//		Pageable pageConditon = PageRequest.of(Integer.valueOf(page.toString()), PROFILES_SAMPLE_COUNT);
		List<UserDocument> users = userRepository.findAll();
		for (final UserDocument userDocument : users) {
			profileResponseList.add(getProfileResponseByUser(userDocument));
		}
		return new ProfilesRes(profileResponseList, profileResponseList.size());
	}

	public ProfilesRes getAllProfiles(final String skills, final Integer experienceFrom, final Integer experienceTo,
			final CandidateType candidateType, final Boolean openToTravel, final String city, Long page) throws ParseException {
				
		if (hasNoFiltersEnabled(skills, experienceFrom, experienceTo, candidateType, openToTravel, city)
				|| isSkillsFilterEmpty(skills)) {
			return getAllProfilesSampleWithoutFilters(page);
		}

		String sql = "select * from user_document where role='Candidate' and is_in_active=false and";
		MapSqlParameterSource querryParams = new MapSqlParameterSource();

		final int currentYear = Year.now().getValue();

		if (experienceFrom != null && experienceTo != null) {
			int from = currentYear - experienceTo;
			int to = currentYear - experienceFrom;
			
			sql += " career_start_year<:from and career_start_year>:to and";
			querryParams.addValue("from", from);
			querryParams.addValue("to", to);
		}
		if (candidateType != null) {
			sql +=  " candidate_type=:candidateType and"; 
			querryParams.addValue("candidateType", candidateType.toString());
		}
		if (openToTravel != null) {
			sql += " open_to_travel=:openToTravel and";
			querryParams.addValue("openToTravel", openToTravel);
		}
		if (city != null) {
			sql += " city like :city and";
			querryParams.addValue("city", "%"+city+"%");
		}
		
		sql += " skills like :skills;";
		querryParams.addValue("skills", "%"+skills+"%");
		
		ProfilesRes response = new ProfilesRes();
		List<ProfileResponse> users = new ArrayList<>();
		customJDBCTemplate.execute(sql, querryParams).forEach(user -> {
			try {
				users.add(getProfileResponseByUser(user));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		});
		response.setProfileResponseList(users);
		response.setSize(users.size());
		return response;
	}

	public UserDocument saveUser(final SignUpDTO signUpDTO) throws DefaultException {
		if (userRepository.findByEmail(signUpDTO.getEmail()) != null) {
			throw new DefaultException("User already exists");
		}
		return userRepository.save(getUserBySignUpForm(signUpDTO));
	}

	private UserDocument getUserBySignUpForm(final SignUpDTO signUpDTO) {
		final UserDocument userDocument = new UserDocument();
		userDocument.setFullName(signUpDTO.getName());
		userDocument.setEmail(signUpDTO.getEmail());
		userDocument.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
		userDocument.setRole(signUpDTO.getRole());
		userDocument.setIsInActive(false);
		return userDocument;
	}

	public UserDocument updateProfile(final ProfileDTO profileDTO) throws DefaultException {
		final UserDocument updatedUser = getUserByProfileDTO(profileDTO);
		final UserDocument existingUser = getUserByIdIfExists(updatedUser.getId());
		updatedUser.setEmail(existingUser.getEmail());
		updatedUser.setPassword(existingUser.getPassword());
		updatedUser.setRole(existingUser.getRole());
		updatedUser.setPhoto(existingUser.getPhoto());
		return userRepository.save(updatedUser);
	}

	public void compressAndUpdateProfilePhoto(final String id, final MultipartFile multipartFile)
			throws DefaultException {
		if (photoNotUpdated(id, multipartFile)) {
			return;
		}
		final UserDocument existingUser = getUserByIdIfExists(id);
		existingUser.setPhoto(ImageCompressor.compressImage(multipartFile));
		userRepository.save(existingUser);
	}

	public ProfileResponse deleteUserById(String id) throws DefaultException {
		userRepository.delete(getUserByIdIfExists(id));
		return null;
	}

}
