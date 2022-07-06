package ee.geckosolutions.demo.web.rest;

import ee.geckosolutions.demo.domain.Person;
import ee.geckosolutions.demo.service.PersonService;
import ee.geckosolutions.demo.web.rest.vm.PageableVM;
import ee.geckosolutions.demo.web.rest.vm.PersonVM;
import io.github.jhipster.web.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PersonResource {

    private final PersonService personService;

    @PostMapping(path = {"/person/find/name", "/person/find/name/{name}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<PersonVM>> findAll(@PathVariable(value = "name", required = false) String name, @RequestBody PageableVM pageableVM) {
        Page<PersonVM> page = personService.findAllByPartialName(Optional.ofNullable(name).orElse(""), PageRequest.of(pageableVM.getPageNumber(), pageableVM.getPageSize())).map(this::map);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page, headers, HttpStatus.OK);
    }

    private PersonVM map(Person person) {
        String title = Optional.ofNullable(person.getTitle()).orElse("");
        String name = (person.getName() + " " + title).trim();
        return PersonVM.builder().id(person.getId()).name(name).imageUrl(person.getImageUrl()).build();
    }

}
