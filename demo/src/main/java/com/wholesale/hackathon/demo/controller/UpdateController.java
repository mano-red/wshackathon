package com.wholesale.hackathon.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wholesale.hackathon.demo.dto.User;
import com.wholesale.hackathon.demo.service.UpdateService;

@RestController
public class UpdateController {
	
	@Autowired
	public UpdateService service;
	
	
	@GetMapping(path = "/update")
	public String update() {
		return "Hello";
		
	}
	
	@PostMapping(value= "/user/save")
    public void save(final @RequestBody User user) {
        service.save(user);
    }
	
	@GetMapping(value= "/user")
    public List<User> getUser() {
        return service.getData();
    }


}
