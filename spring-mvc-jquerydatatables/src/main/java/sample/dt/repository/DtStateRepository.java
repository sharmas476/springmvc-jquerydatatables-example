package sample.dt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sample.dt.entity.DtState;

@Repository
public interface DtStateRepository extends JpaRepository<DtState, Long> {

	DtState findByTableId(String tableId);
}
