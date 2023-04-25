package com.example.skillmatcherbackend.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.skillmatcherbackend.model.CandidateType;
import com.example.skillmatcherbackend.model.UserRole;
import com.example.skillmatcherbackend.model.UserTableColumns;
import com.example.skillmatcherbackend.model.document.UserDocument;

@Repository
public class CustomJDBCTemplate {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<UserDocument> execute(String sql) {
		List<UserDocument> checking = jdbcTemplate.query(sql,
				new ResultSetExtractor<List<UserDocument>>() {
					@Override
					public List<UserDocument> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<UserDocument> returnValue = new ArrayList<>();

						while (rs.next()) {
							UserDocument user = new UserDocument();
							user.setId(rs.getString(UserTableColumns.ID));
							user.setFullName(rs.getString(UserTableColumns.FULLNAME));
							user.setEmail(rs.getString(UserTableColumns.EMAIL));
							user.setIsInActive(rs.getBoolean(UserTableColumns.ISINACTIVE));
							user.setPassword(rs.getString(UserTableColumns.PASSWORD));
							user.setPhoto(rs.getString(UserTableColumns.PHOTO));
							user.setResume(rs.getString(UserTableColumns.RESUME));
							user.setResumeFileName(rs.getString(UserTableColumns.RESUMEFILENAME));
							user.setCity(rs.getString(UserTableColumns.CITY));
							user.setCountry(rs.getString(UserTableColumns.COUNTRY));
							user.setMobile(rs.getString(UserTableColumns.MOBILE));
							user.setLinkedIn(rs.getString(UserTableColumns.LINKEDIN));
							user.setOpenToTravel(rs.getBoolean(UserTableColumns.OPENTOTRAVEL));
							user.setCareerStartYear(rs.getInt(UserTableColumns.CAREERSTARTYEAR));
							user.setExpectedCommercials(rs.getString(UserTableColumns.EXPECTEDCOMMERCIAL));
							user.setAboutMe(rs.getString(UserTableColumns.ABOUTME));
							user.setRole(UserRole.valueOf(rs.getString(UserTableColumns.ROLE)));
							user.setCandidateType(CandidateType.valueOf(rs.getString(UserTableColumns.CANDIDATETYPE)));
							user.setClients(rs.getString(UserTableColumns.CLIENTS));
							user.setCertifications(rs.getString(UserTableColumns.CERTIFICATIONS));
							user.setEducation(rs.getString(UserTableColumns.EDUCATION));
							user.setContentCreation(rs.getString(UserTableColumns.CONTENTCREATION));
							user.setSkills(rs.getString(UserTableColumns.SKILLS));
							user.setFts(rs.getString(UserTableColumns.FTS));
							returnValue.add(user);
						}

						return returnValue;
					}
				});
		return checking;
	}
	
	public List<UserDocument> execute(String sql, MapSqlParameterSource querryParams) {
		List<UserDocument> checking = jdbcTemplate.query(sql, querryParams,
				new ResultSetExtractor<List<UserDocument>>() {
					@Override
					public List<UserDocument> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<UserDocument> returnValue = new ArrayList<>();

						while (rs.next()) {
							UserDocument user = new UserDocument();
							user.setId(rs.getString(UserTableColumns.ID));
							user.setFullName(rs.getString(UserTableColumns.FULLNAME));
							user.setEmail(rs.getString(UserTableColumns.EMAIL));
							user.setIsInActive(rs.getBoolean(UserTableColumns.ISINACTIVE));
							user.setPassword(rs.getString(UserTableColumns.PASSWORD));
							user.setPhoto(rs.getString(UserTableColumns.PHOTO));
							user.setResume(rs.getString(UserTableColumns.RESUME));
							user.setResumeFileName(rs.getString(UserTableColumns.RESUMEFILENAME));
							user.setCity(rs.getString(UserTableColumns.CITY));
							user.setCountry(rs.getString(UserTableColumns.COUNTRY));
							user.setMobile(rs.getString(UserTableColumns.MOBILE));
							user.setLinkedIn(rs.getString(UserTableColumns.LINKEDIN));
							user.setOpenToTravel(rs.getBoolean(UserTableColumns.OPENTOTRAVEL));
							user.setCareerStartYear(rs.getInt(UserTableColumns.CAREERSTARTYEAR));
							user.setExpectedCommercials(rs.getString(UserTableColumns.EXPECTEDCOMMERCIAL));
							user.setAboutMe(rs.getString(UserTableColumns.ABOUTME));
							user.setRole(UserRole.valueOf(rs.getString(UserTableColumns.ROLE)));
							user.setCandidateType(CandidateType.valueOf(rs.getString(UserTableColumns.CANDIDATETYPE)));
							user.setClients(rs.getString(UserTableColumns.CLIENTS));
							user.setCertifications(rs.getString(UserTableColumns.CERTIFICATIONS));
							user.setEducation(rs.getString(UserTableColumns.EDUCATION));
							user.setContentCreation(rs.getString(UserTableColumns.CONTENTCREATION));
							user.setSkills(rs.getString(UserTableColumns.SKILLS));
							user.setFts(rs.getString(UserTableColumns.FTS));
							returnValue.add(user);
						}

						return returnValue;
					}
				});
		return checking;
	}

}
