package fr.nro.demo.jsonpatchdemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.nro.demo.jsonpatchdemo.dto.ContactDto;
import fr.nro.demo.jsonpatchdemo.exception.ResourceNotFoundException;
import fr.nro.demo.jsonpatchdemo.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.json.JsonMergePatch;
import javax.json.JsonPatch;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import java.util.List;

@RestController
@RequestMapping("/contact")
public class ContactController {

    private ObjectMapper mapper;


    private ContactService contactService;

    @Autowired
    public ContactController(ObjectMapper mapper, ContactService contactService){
        this.mapper = mapper;
        this.contactService = contactService;
    }

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<Void> updateContact(@PathVariable Long id,
                                              @RequestBody JsonPatch patchDocument) {
        // Find the model that will be patched
        ContactDto contact = contactService.findContact(id).orElseThrow(ResourceNotFoundException::new);

        // Apply the patch
        ContactDto contactPatched = patch(patchDocument, contact, ContactDto.class);

        // Persist the changes
        contactService.updateContact(contactPatched);

        // Return 204 to indicate the request has succeeded
        return ResponseEntity.noContent().build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ContactDto> findAll(){
        return this.contactService.findAll();
    }



    public <T> T patch(JsonPatch patch, T targetBean, Class<T> beanClass) {

        // Convert the Java bean to a JSON document
        JsonStructure target = mapper.convertValue(targetBean, JsonStructure.class);

        // Apply the JSON Patch to the JSON document
        JsonValue patched = patch.apply(target);

        // Convert the JSON document to a Java bean and return it
        return mapper.convertValue(patched, beanClass);
    }

    public <T> T mergePatch(JsonMergePatch mergePatch, T targetBean, Class<T> beanClass) {

        // Convert the Java bean to a JSON document
        JsonValue target = mapper.convertValue(targetBean, JsonValue.class);

        // Apply the JSON Merge Patch to the JSON document
        JsonValue patched = mergePatch.apply(target);

        // Convert the JSON document to a Java bean and return it
        return mapper.convertValue(patched, beanClass);
    }
}
