package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Employee;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    CustomerService customerService;

    @Autowired
    EmployeeService employeeService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = new Customer(customerDTO.getId(), customerDTO.getName(), customerDTO.getPhoneNumber(), customerDTO.getNotes());
        Customer savedCustomer;
        try{
            savedCustomer = customerService.saveCustomer(customer, customerDTO.getPetIds());
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Problem saving the Customer", e);
        }
        CustomerDTO customerDTOResponse = convertCustomerEntityToCustomerDTO(savedCustomer);
        return customerDTOResponse;

    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<Customer> customers = customerService.getAllCustomers();
        return customers.stream().map(this::convertCustomerEntityToCustomerDTO).collect(Collectors.toList());
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Customer customer;
        try {
            customer = customerService.findByPetId(petId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Problem finding the owner of this pet", e);
        }
        CustomerDTO customerDTOResponse = convertCustomerEntityToCustomerDTO(customer);
        return customerDTOResponse;
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employeeToBeSaved = new Employee(employeeDTO.getId(), employeeDTO.getName(), employeeDTO.getSkills(), employeeDTO.getDaysAvailable());
        Employee savedEmployee;

        try{
            savedEmployee = employeeService.saveEmployee(employeeToBeSaved);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Problem while saving the Employee", e);
        }
        EmployeeDTO employeeDTOResponse = convertEmployeeEntityToEmployeeDTO(savedEmployee);
        return employeeDTOResponse;
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        return convertEmployeeEntityToEmployeeDTO(employeeService.getEmployeeById(employeeId));
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        employeeService.setEmployeeAvailability(daysAvailable, employeeId);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> employees = employeeService.getEmployeesByService(employeeDTO.getDate(), employeeDTO.getSkills());
        List<EmployeeDTO> suitableEmployees = employees.stream().map(this::convertEmployeeEntityToEmployeeDTO).collect(Collectors.toList());
        return suitableEmployees;
    }

    private CustomerDTO convertCustomerEntityToCustomerDTO(Customer customer) {
        List<Long> petIds = customer.getPets().stream().map(Pet::getId).collect(Collectors.toList());
        CustomerDTO customerDTO = new CustomerDTO(customer.getId(), customer.getName(), customer.getPhoneNumber(), customer.getNotes(), petIds);
        return customerDTO;
    }
    private EmployeeDTO convertEmployeeEntityToEmployeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO(employee.getId(), employee.getName(), employee.getSkills(), employee.getAvailability());
        return employeeDTO;
    }

}
