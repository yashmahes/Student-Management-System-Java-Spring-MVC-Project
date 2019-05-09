/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ci6110.ci6110.repositories;

import com.ci6110.ci6110.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Khaled
 */
public interface EmployeeRepository extends JpaRepository<Employee, Integer>{


    
}
