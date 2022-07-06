package ee.geckosolutions.demo.service;

import ee.geckosolutions.demo.domain.Person;
import ee.geckosolutions.demo.repository.PersonRepository;
import ee.geckosolutions.demo.service.dto.PersonDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public void saveAll(List<PersonDTO> personDtoList) {
        List<Person> personList = personDtoList.stream().map(this::map).collect(Collectors.toList());
        personRepository.saveAll(personList);
    }

    private Person map(PersonDTO personDTO) {
        return Person.builder().name(personDTO.getName()).title(personDTO.getTitle()).imageUrl(personDTO.getImageUrl()).build();
    }

    public Page<Person> findAllByPartialName(String nameContaining, Pageable pageable) {
        return personRepository.findAllByNameContaining(nameContaining, pageable);
    }

}
