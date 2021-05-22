package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.entities.Schedule;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @Autowired
    ScheduleService scheduleService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule(scheduleDTO.getDate(), scheduleDTO.getActivities());
        ScheduleDTO scheduleDtoResponse;
        try {
            Schedule savedSchedule = scheduleService.saveSchedule(schedule, scheduleDTO.getEmployeeIds(), scheduleDTO.getPetIds());
            scheduleDtoResponse = convertScheduleEntityToScheduleDto(savedSchedule);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Problem in saving the schedule", e);
        }
        return scheduleDtoResponse;
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = scheduleService.getAllSchedules();
        return schedules.stream().map(this::convertScheduleEntityToScheduleDto).collect(Collectors.toList());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> schedules;
        try {
            schedules = scheduleService.getPetSchedule(petId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Problem finding schedule for the pet", e);
        }
        List<ScheduleDTO> responseDTOs = schedules.stream().map(this::convertScheduleEntityToScheduleDto).collect(Collectors.toList());
        return responseDTOs ;
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<Schedule> schedules;
        try {
            schedules = scheduleService.getEmployeeSchedule(employeeId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Problem finding schedule for the employee", e);
        }
        List<ScheduleDTO> responseDTOs = schedules.stream().map(this::convertScheduleEntityToScheduleDto).collect(Collectors.toList());
        return responseDTOs ;
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Schedule> schedules;
        try {
            schedules = scheduleService.getCustomerSchedule(customerId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Problem finding schedule for the customer", e);
        }
        List<ScheduleDTO> responseDTOs = schedules.stream().map(this::convertScheduleEntityToScheduleDto).collect(Collectors.toList());
        return responseDTOs ;
    }
    private ScheduleDTO convertScheduleEntityToScheduleDto(Schedule schedule){
        List<Long> petIds = new ArrayList<>();//to copy manually
        List<Long> employeeIds = new ArrayList<>();//to copy manually

        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);//copy the rest automatically

        schedule.getPets().forEach(pet -> petIds.add(pet.getId()));
        schedule.getEmployee().forEach(employee -> employeeIds.add(employee.getId()));

        scheduleDTO.setPetIds(petIds);
        scheduleDTO.setEmployeeIds(employeeIds);

        return scheduleDTO;
    }
}
