package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entities.Employee;
import com.udacity.jdnd.course3.critter.repositories.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee) {
        Employee addedEmployee = employeeRepository.save(employee);
        return addedEmployee;
    }
    public Employee getEmployeeById(Long employeeId) {
        Optional<Employee> optional = employeeRepository.findById(employeeId);
        Employee employee;
        if(optional.isPresent()){
             employee = optional.get();
        }else{
            throw new EmployeeNotFoundException();
        }
        return employee;
    }
    public void setEmployeeAvailability(Set<DayOfWeek> days, Long employeeId) {
        Optional<Employee> optional = employeeRepository.findById(employeeId);
        Employee employee;
        if(optional.isPresent()){
            employee = optional.get();
            employee.setAvailability(days);
            employeeRepository.save(employee);
        }else{
            throw new EmployeeNotFoundException();
        }

    }

    public List<Employee> getEmployeesByService(LocalDate date, Set<EmployeeSkill> employeeSkillSet){
        List<Employee> employees = employeeRepository.findEmployeeByAvailability(date.getDayOfWeek())
                .stream()
                .filter(employee -> employee.getSkills().containsAll(employeeSkillSet))
                .collect(Collectors.toList());
        return employees;
    }




}
