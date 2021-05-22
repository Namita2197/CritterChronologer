package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Employee;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.repositories.CustomerRepository;
import com.udacity.jdnd.course3.critter.repositories.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class PetService {

    @Autowired
    PetRepository petRepository;
    @Autowired
    CustomerRepository customerRepository;

    public Pet savePet(Pet pet) {
        Pet addedPet = petRepository.save(pet);
        Customer customer = addedPet.getCustomer();
        List<Pet> allPets= customer.getPets();
        allPets.add(addedPet);
        customer.setPets(allPets);
        customerRepository.save(customer);
        return addedPet;
    }

    public Pet getPetById(Long petId) {
        Optional<Pet> optional = petRepository.findById(petId);
        Pet pet;
        if(optional.isPresent()){
            pet = optional.get();
        }else{
            throw new PetNotFoundException();
        }
        return pet;
    }

    public List<Pet> getAllPets() {
        List<Pet> pets = petRepository.findAll();
        return pets;
    }

    public List<Pet> getPetsByCustomerId(long customerId) {
        List<Pet> pets = petRepository.findAllByCustomerId(customerId);
        return pets;
    }
}
