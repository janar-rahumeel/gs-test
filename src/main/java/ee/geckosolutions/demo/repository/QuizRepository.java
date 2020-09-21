package ee.geckosolutions.demo.repository;

import ee.geckosolutions.demo.domain.Quiz;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, String> {

}
