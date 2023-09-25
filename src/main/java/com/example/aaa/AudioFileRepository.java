package com.example.aaa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AudioFileRepository extends JpaRepository<AudioFile, Long> {
    Optional<AudioFile> findByFileName(String fileName);
}

