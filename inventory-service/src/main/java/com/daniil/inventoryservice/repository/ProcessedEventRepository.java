package com.daniil.inventoryservice.repository;

import com.daniil.inventoryservice.model.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedEventRepository
        extends JpaRepository<ProcessedEvent, ProcessedEvent.ProcessedEventId> {

    boolean existsById(ProcessedEvent.ProcessedEventId id);
}
