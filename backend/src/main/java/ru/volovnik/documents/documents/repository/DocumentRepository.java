package ru.volovnik.documents.documents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.volovnik.documents.documents.entity.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
