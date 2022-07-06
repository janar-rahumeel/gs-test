package ee.geckosolutions.demo.repository;

import ee.geckosolutions.demo.domain.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("select p from Person p where lower(p.name) like lower(concat('%', concat(:nameContaining, '%')))")
    Page<Person> findAllByNameContaining(@Param("nameContaining") String nameContaining, Pageable pageable);

}
