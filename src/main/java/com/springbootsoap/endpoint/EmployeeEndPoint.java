package com.springbootsoap.endpoint;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.springbootsoap.model.Employee;
import com.springbootsoap.services.EmployeeService;

import allapis.springbootsoap.com.AddEmployeeRequest;
import allapis.springbootsoap.com.AddEmployeeResponse;
import allapis.springbootsoap.com.ServiceStatus;
import allapis.springbootsoap.com.UpdateEmployeeRequest;
import allapis.springbootsoap.com.UpdateEmployeeResponse;

@Endpoint
public class EmployeeEndPoint {

    private static final String NAMESPACE_URI = "http://com.springbootsoap.allapis";

    @Autowired
    private EmployeeService employeeService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addEmployeeRequest")
    @ResponsePayload
    public AddEmployeeResponse addEmployee(@RequestPayload AddEmployeeRequest request) {
        AddEmployeeResponse response = new AddEmployeeResponse();
        ServiceStatus serviceStatus = new ServiceStatus();
        try {
            Employee employee = new Employee();
            BeanUtils.copyProperties(request.getEmployeeInfo(), employee);
            employeeService.addEmployee(employee);
            serviceStatus.setStatus("SUCCESS");
            serviceStatus.setMessage("Content Added Successfully");
        } catch (Exception e) {
            serviceStatus.setStatus("FAILURE");
            serviceStatus.setMessage("Error occurred: " + e.getMessage());
        }
        response.setServiceStatus(serviceStatus);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateEmployeeRequest")
    @ResponsePayload
    public UpdateEmployeeResponse updateEmployee(@RequestPayload UpdateEmployeeRequest request) {
        UpdateEmployeeResponse response = new UpdateEmployeeResponse();
        ServiceStatus serviceStatus = new ServiceStatus();
        try {
            Employee employee = new Employee();
            BeanUtils.copyProperties(request.getEmployeeInfo(), employee);
            employeeService.updateEmployee(employee);
            serviceStatus.setStatus("SUCCESS");
            serviceStatus.setMessage("Content Updated Successfully");
        } catch (Exception e) {
            serviceStatus.setStatus("FAILURE");
            serviceStatus.setMessage("Error occurred: " + e.getMessage());
        }
        response.setServiceStatus(serviceStatus);
        return response;
    }
}
