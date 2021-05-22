package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Employee;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.entities.Schedule;
import com.udacity.jdnd.course3.critter.repositories.CustomerRepository;
import com.udacity.jdnd.course3.critter.repositories.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repositories.PetRepository;
import com.udacity.jdnd.course3.critter.repositories.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class ScheduleService {
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    PetRepository petRepository;
    @Autowired
    CustomerRepository customerRepository;

    public Schedule saveSchedule(Schedule schedule, List<Long> employeeIdList, List<Long> petIdList) {
        List<Employee> employees = employeeRepository.findAllById(employeeIdList);
        List<Pet> pets = petRepository.findAllById(petIdList);
        schedule.setEmployee(employees);
        schedule.setPets(pets);

        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedules() {
        List<Schedule> allSchedules = scheduleRepository.findAll();
        return allSchedules;
    }

    public List<Schedule> getEmployeeSchedule(Long employeeId) {
        Employee employee = employeeRepository.getOne(employeeId);
        List<Schedule> schedules = scheduleRepository.findByEmployee(employee);
        return schedules;
    }

    public List<Schedule> getPetSchedule(Long petId) {
        Pet pet = petRepository.getOne(petId);
        List<Schedule> schedules = scheduleRepository.findByPets(pet);
        return schedules;
    }

    public List<Schedule> getCustomerSchedule(Long customerId) {
        Customer customer = customerRepository.getOne(customerId);
        List<Pet> pets = customer.getPets();
        List<Schedule> scheduleForCustomer = new ArrayList<>();

        pets.forEach(pet -> {
            List<Schedule> scheduledPets = scheduleRepository.findByPets(pet);
            scheduleForCustomer.addAll(scheduledPets);
        });

        return scheduleForCustomer;
    }
}
