package com.wholesale.hackathon.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wholesale.hackathon.demo.dto.OcrPathDto;
import com.wholesale.hackathon.demo.service.DetectService;

@RequestMapping("/doXpert")
@RestController
public class OcrController {

	@Autowired
	private DetectService detectService;
	
	@PostMapping("/ocr")
	public ResponseEntity<String> detectText(@RequestBody OcrPathDto ocrPathDto)  {
		
		ResponseEntity<String> result=new ResponseEntity<>("Successfull",HttpStatus.OK);
		try {
			detectService.detectDocumentsGcs(ocrPathDto.getGcsSourcePath(), ocrPathDto.getGcsDestinationPath());
		} catch (Exception e) {
			result=new ResponseEntity<>(e.getMessage()==null?"NullPointer":e.getMessage(),HttpStatus.BAD_REQUEST);
		}
		return result;
	}
	
	@GetMapping("test")
	public String testOCRController() {
		return "Hello OCR";
	}
}
