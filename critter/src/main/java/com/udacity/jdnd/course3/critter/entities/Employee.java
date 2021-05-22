package com.udacity.jdnd.course3.critter.entities;

import com.udacity.jdnd.course3.critter.user.EmployeeSkill;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.DayOfWeek;
import java.util.Set;

@Entity
public class Employee {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ElementCollection
    private Set<EmployeeSkill> skills;

    @ElementCollection
    private Set<DayOfWeek> availability;

    public Employee(Long id, String name, Set<EmployeeSkill> skills, Set<DayOfWeek> availability) {
        this.id = id;
        this.name = name;
        this.skills = skills;
        this.availability = availability;
    }

    public Employee() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<EmployeeSkill> getSkills() {
        return skills;
    }

    public void setSkills(Set<EmployeeSkill> skills) {
        this.skills = skills;
    }

    public Set<DayOfWeek> getAvailability() {
        return availability;
    }

    public void setAvailability(Set<DayOfWeek> availability) {
        this.availability = availability;
    }
}
