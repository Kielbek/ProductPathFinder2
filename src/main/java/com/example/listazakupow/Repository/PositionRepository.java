package com.example.listazakupow.Repository;

import com.example.listazakupow.Entity.Position;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends CrudRepository<Position, Long> {
}
