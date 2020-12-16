package com.wholesale.hackathon.demo.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(path = "/upload")
	public void upload(@RequestParam("files") List<MultipartFile> files, @RequestParam("depName") String depName,
			@RequestParam("clientId") String clientId, @RequestParam("email") String email,  @RequestParam("uploadId") String uploadId) {
		service.doUpload(files, depName, clientId, email, uploadId);
		
	}
	

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(path = "/download")
	public void download() {
		service.doDownload();
		
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
