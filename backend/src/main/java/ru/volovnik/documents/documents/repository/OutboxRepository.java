package ru.volovnik.documents.documents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.volovnik.documents.documents.entity.Outbox;

import java.util.Optional;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<Outbox, UUID> {

    Optional<Outbox> findFirstByIsSentFalse();
}
