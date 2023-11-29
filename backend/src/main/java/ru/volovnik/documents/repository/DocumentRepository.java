package ru.volovnik.documents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.volovnik.documents.entity.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
