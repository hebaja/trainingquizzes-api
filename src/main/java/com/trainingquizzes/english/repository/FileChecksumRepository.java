package com.trainingquizzes.english.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trainingquizzes.english.model.FileChecksum;

public interface FileChecksumRepository extends JpaRepository<FileChecksum, String> {

}
