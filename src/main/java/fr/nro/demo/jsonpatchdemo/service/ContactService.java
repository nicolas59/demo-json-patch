package fr.nro.demo.jsonpatchdemo.service;

import fr.nro.demo.jsonpatchdemo.dto.ContactDto;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ContactService {

    private Set<ContactDto> contacts = new HashSet<>();

    public ContactService(){
        contacts.add(new ContactDto(1L, "Dupond", "Jean"));
    }

    public Optional<ContactDto> findContact(Long id){
        return this.contacts.stream().filter(c -> Objects.equals(c.getId(), id)).findFirst();

    }

    public void updateContact(ContactDto contactDto){
        contacts.remove(contactDto);
        contacts.add(contactDto);
    }

    public List<ContactDto> findAll(){
        return List.copyOf(this.contacts);
    }
}
