package br.edu.atitus.api_sample.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.atitus.api_sample.entities.EventEntity;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
}
