package one.digitalinnovation.gof.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import one.digitalinnovation.gof.model.Address;

public interface AddressRepository extends JpaRepository<Address, String> {
}