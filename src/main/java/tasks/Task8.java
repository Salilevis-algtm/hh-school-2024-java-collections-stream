package tasks;

import common.Person;
import common.PersonService;
import common.PersonWithResumes;
import common.Resume;

import java.util.*;
import java.util.stream.Collectors;

/*
  Еще один вариант задачи обогащения
  На вход имеем коллекцию персон
  Сервис умеет по personId искать их резюме (у каждой персоны может быть несколько резюме)
  На выходе хотим получить объекты с персоной и ее списком резюме
 */
public class Task8 {
  private final PersonService personService;

  public Task8(PersonService personService) {
    this.personService = personService;
  }

  public Set<PersonWithResumes> enrichPersonsWithResumes(Collection<Person> persons) {
    //Получение резюме сотрудников
    Set<Resume> personResumes = personService.findResumes(persons.stream().map(Person::id).collect(Collectors.toSet()));
    //Создание map с полями personId и PersonWithResumes
    Map<Integer, PersonWithResumes> mapPersonWithResumes = new HashMap<>();
    persons.forEach(person -> {
      mapPersonWithResumes.put(person.id(), new PersonWithResumes(person, new HashSet<>()));
    });
    //Заполнение PersonWithResumes.resumes();
    for(Resume resume : personResumes) {
      Integer resumeId = resume.personId();
      Set<Resume> resumes = mapPersonWithResumes.get(resumeId).resumes();
      resumes.add(resume);
    }
    return new HashSet<>(mapPersonWithResumes.values());
  }
}
