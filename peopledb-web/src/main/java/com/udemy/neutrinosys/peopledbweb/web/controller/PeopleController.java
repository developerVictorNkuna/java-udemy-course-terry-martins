package com.udemy.neutrinosys.peopledbweb.web.controller;


import com.udemy.neutrinosys.peopledbweb.bizmodel.Person;
import com.udemy.neutrinosys.peopledbweb.repository.PeopleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value="/people")
public class PeopleController {
    private PeopleRepository personRepository;

    // public PeopleController(PeopleRepository personRepository) {
    //     personRepository.findAll();
    // }


    @ModelAttribute("people")
    public Iterable<Person> getPeople(){
        return personRepository.findAll();
    }

    @ModelAttribute
    public Person getPerson(){
        return new Person(0,"","",null, BigDecimal.valueOf(0));

    }



    @GetMapping
    public  String showPeoplePage(Model model) {

        return "people";

    }
    @PostMapping("save")
    public String savePerson(@Valid Person person, Errors errors) throws SQLException {
        System.out.println(person);
        if (!errors.hasErrors()) {
            personRepository.save(person);
            return "redirect:people";
        }
        return "people";

    }
    @PostMapping(params = "delete=true")
    public String deletePeople(@RequestParam Optional<List<Long>> selections){

        System.out.println(selections);
        if(selections.isPresent()){
            personRepository.deleteAllById(selections.get());
        }
        return  "redirect:people";
    }

    @PostMapping
    public String editPerson(@RequestParam Optional<List<Long>> selections, Model model) throws SQLException {
        System.out.println(selections.isPresent());{
            if (selections.isPresent()) {
                Optional<Person> person =personRepository.findById(selections.get().get(0));
                model.addAttribute("person",person);
            }
            //populate the form with whatever person we have selected

            return "people";
        }
    }

    private void findById(Long aLong) {

    }


}
