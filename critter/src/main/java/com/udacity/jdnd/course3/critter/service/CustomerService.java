package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Employee;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.repositories.CustomerRepository;
import com.udacity.jdnd.course3.critter.repositories.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    PetRepository petRepository;

    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

//    public Customer findByPet(Pet pet){
//        return customerRepository.findByPet(pet);
//    }

    public Customer saveCustomer(Customer customer){
        return customerRepository.save(customer);
    }

    public Customer saveCustomer(Customer customer, List<Long> petIds){
        List<Pet> pets = new ArrayList<>();
        if (petIds != null) {
            pets = petIds.stream().map((petId) -> petRepository.getOne(petId)).collect(Collectors.toList());
        }
        customer.setPets(pets);
        return customerRepository.save(customer);
    }

    public Customer findByPetId(Long petId){
        Customer customer = petRepository.getOne(petId).getCustomer();
        return customer;
    }
    public Customer findById(Long customerId){
        Optional<Customer> optional  = customerRepository.findById(customerId);
        Customer customer;
        if(optional.isPresent()){
            customer = optional.get();
        }else{
            throw new CustomerNotFoundException();
        }
        return customer;
    }
}
