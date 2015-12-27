package sample.dt.repository;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import sample.dt.entity.CompanyUser;

@Repository
public interface CompanyUserRepository extends JpaRepository<CompanyUser, Long>, QueryDslPredicateExecutor<CompanyUser> {
	
	@Query("select distinct u.urole from sample.dt.entity.CompanyUser u order by u.urole")
	List<String> findDistinctUserRole();
	
	@Query("select distinct u.city from sample.dt.entity.CompanyUser u order by u.city")
	List<String> findDistinctUserCity();
	
	@Query("select new org.apache.commons.lang3.tuple.ImmutablePair( min(u.age), max(u.age) ) from sample.dt.entity.CompanyUser u")
	Pair<Integer, Integer> findMinMaxAge();

}
