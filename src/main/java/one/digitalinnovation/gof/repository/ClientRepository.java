package one.digitalinnovation.gof.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import one.digitalinnovation.gof.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}