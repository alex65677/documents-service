package ru.volovnik.documents.documents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.volovnik.documents.documents.entity.Inbox;

import java.util.Optional;
import java.util.UUID;

public interface InboxRepository extends JpaRepository<Inbox, UUID> {

    Optional<Inbox> findFirstByIsCompletedFalse();
}
