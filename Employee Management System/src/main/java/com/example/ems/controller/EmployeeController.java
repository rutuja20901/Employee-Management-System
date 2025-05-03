package com.example.ems.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.ems.model.EmployeeModel;
import com.example.ems.repository.EmployeeRepo;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepo employeeRepository;

    // Get all employees
    @GetMapping
    public List<EmployeeModel> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Get employee by ID
    @GetMapping("/{id}")
    public Optional<EmployeeModel> getEmployeeById(@PathVariable Long id) {
        return employeeRepository.findById(id);
    }

    // Create new employee
    @PostMapping
    public EmployeeModel createEmployee(@RequestBody EmployeeModel employee) {
        return employeeRepository.save(employee);
    }

    // Update existing employee
    @PutMapping("/{id}")
    public EmployeeModel updateEmployee(@PathVariable Long id, @RequestBody EmployeeModel employeeDetails) {
        EmployeeModel emp = employeeRepository.findById(id).orElseThrow();

        emp.setName(employeeDetails.getName());
        emp.setDepartment(employeeDetails.getDepartment());
        emp.setSalary(employeeDetails.getSalary());

        return employeeRepository.save(emp);
    }

    // Delete employee
    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeRepository.deleteById(id);
        return "Employee deleted";
    }
}
