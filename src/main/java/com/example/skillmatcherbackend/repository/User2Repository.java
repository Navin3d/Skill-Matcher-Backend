package com.example.skillmatcherbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.skillmatcherbackend.model.UserRole;
import com.example.skillmatcherbackend.model.document.UserDocument;

public interface User2Repository extends PagingAndSortingRepository<UserDocument, String> {
    UserDocument findByEmail(String email);

    Page<UserDocument> findByRole(UserRole role, Pageable pageable);

}
