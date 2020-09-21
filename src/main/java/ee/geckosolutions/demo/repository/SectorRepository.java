package ee.geckosolutions.demo.repository;

import ee.geckosolutions.demo.domain.Sector;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SectorRepository extends JpaRepository<Sector, Long> {

}
