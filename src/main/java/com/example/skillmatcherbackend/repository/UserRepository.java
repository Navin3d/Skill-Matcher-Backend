package com.example.skillmatcherbackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import com.example.skillmatcherbackend.model.document.UserDocument;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRepository {
	
	private final CustomJDBCTemplate customJDBCTemplate;
	
	public UserDocument save(UserDocument userDocument) {
		String querry = "INSERT INTO user_document(id, about_me, candidate_type, career_start_year, certifications, city, clients, content_creation, country, education, email, expected_commercials, full_name, is_in_active, linked_in, mobile, open_to_travel, password, photo, resume, resume_file_name, role, skills, tsvector) VALUES (:id, :about_me, :candidate_type, :career_start_year, :certifications, :city, :content_creation, :country, :education, :email, :expected_commercials, :full_name, :is_in_active, :linked_in, :mobile, :open_to_travel, :password, :photo, :resume, :resume_file_name, :role, :skills, :tsvector);";
		MapSqlParameterSource querryParams = new MapSqlParameterSource();
		querryParams.addValue("id", userDocument.getId());
		querryParams.addValue("email", userDocument.getEmail());jhyfc
		
		List<UserDocument> users = customJDBCTemplate.execute(querry, querryParams);
		if(users.isEmpty())
			throw new RuntimeException("User not found with id: " + userDocument.getId());
		
		UserDocument returnValue = users.get(0);
		
		return returnValue;
	}
	
	public List<UserDocument> findAll() {
		return customJDBCTemplate.execute("SELECT * FROM user_document;");
	}
 	
	public Optional<UserDocument> findById(String id) {
		String querry = "SELECT * FROM user_document WHERE id = ':id';";
		MapSqlParameterSource querryParams = new MapSqlParameterSource();
		querryParams.addValue("id", id);
		
		List<UserDocument> users = customJDBCTemplate.execute(querry, querryParams);
		if(users.isEmpty())
			throw new RuntimeException("User not found with id: " + id);
		
		Optional<UserDocument> returnValue = Optional.of(users.get(0));
		
		return returnValue;
	}
	
	public UserDocument findByEmail(String email) {
		String querry = "SELECT * FROM user_document WHERE email = ':email';";
		MapSqlParameterSource querryParams = new MapSqlParameterSource();
		querryParams.addValue("email", email);
		
		List<UserDocument> users = customJDBCTemplate.execute(querry, querryParams);
		if(users.isEmpty())
			throw new RuntimeException("User not found with email: " + email);
				
		return users.get(0);
	}
	
	public void delete(UserDocument userDocument) {
		String querry = "DELETE FROM user_document WHERE id = ':id';";
		MapSqlParameterSource querryParams = new MapSqlParameterSource();
		querryParams.addValue("id", userDocument.getId());
		customJDBCTemplate.execute(querry, querryParams);
	}

}
