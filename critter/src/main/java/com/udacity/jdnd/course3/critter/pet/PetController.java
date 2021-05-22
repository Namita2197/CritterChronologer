package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.repositories.CustomerRepository;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {
    @Autowired
    PetService petService;
    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerRepository customerRepository;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Customer customer = customerService.findById(petDTO.getOwnerId());
        Pet pet = new Pet(petDTO.getType(), petDTO.getName(), customer, petDTO.getBirthDate(), petDTO.getNotes());
        PetDTO petDTOResponse;
        try{
            Pet savedPet = petService.savePet(pet);
            petDTOResponse = convertPetEntityToPetDto(savedPet);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Problem in saving the Pet", e);
        }

        return petDTOResponse;
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet = petService.getPetById(petId);
        PetDTO petDTO = convertPetEntityToPetDto(pet);
        return petDTO;
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<Pet> pets = petService.getAllPets();
        return pets.stream().map(this::convertPetEntityToPetDto).collect(Collectors.toList());
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> pets;
        try {
            pets = petService.getPetsByCustomerId(ownerId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Problem in finding pet", e);
        }
        return pets.stream().map(this::convertPetEntityToPetDto).collect(Collectors.toList());
    }

//    private Pet convertPetDtoToPetEntity(PetDTO petDTO){
//        Pet pet = new Pet();
//        BeanUtils.copyProperties(petDTO, pet);
//
//        return pet;
//    }

    private PetDTO convertPetEntityToPetDto(Pet pet){
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);

        petDTO.setOwnerId(pet.getCustomer().getId());
        return petDTO;
    }
}
