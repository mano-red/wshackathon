package com.wholesale.hackathon.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wholesale.hackathon.demo.dto.OcrInputDto;
import com.wholesale.hackathon.demo.dto.OcrOutputDto;
import com.wholesale.hackathon.demo.service.DetectService;

@RequestMapping("/doXpert/ocr")
@RestController
public class OcrController {

	@Autowired
	private DetectService detectService;

	@PostMapping
	public ResponseEntity<OcrOutputDto> extractText(@RequestBody OcrInputDto ocrPathDto) {

		OcrOutputDto ocrOutputDto = new OcrOutputDto();

		try {
			ocrOutputDto = detectService.detectText(ocrPathDto);
			return new ResponseEntity<OcrOutputDto>(ocrOutputDto, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println("Exception occured " + e.getMessage());
			e.printStackTrace();
		}
		return new ResponseEntity<OcrOutputDto>(HttpStatus.BAD_REQUEST);

	}

	@PostMapping("/pdf")
	public ResponseEntity<String> extractTextfromPdf(@RequestBody OcrInputDto ocrPathDto) {

		ResponseEntity<String> result = new ResponseEntity<>(HttpStatus.OK);
		try {
			String extractedText = detectService.detectDocumentsGcs(ocrPathDto.getGcsSourcePath(),
					ocrPathDto.getGcsDestinationPath());
			result = new ResponseEntity<>(extractedText, HttpStatus.OK);
		} catch (Exception e) {
			result = new ResponseEntity<>(e.getMessage() == null ? "NullPointer" : e.getMessage(),
					HttpStatus.BAD_REQUEST);
		}
		return result;
	}

	@PostMapping("/image")
	public ResponseEntity<String> extractTextfromImage(@RequestBody OcrInputDto ocrPathDto) {
		ResponseEntity<String> result = new ResponseEntity<>("Successfull", HttpStatus.OK);
		try {
			detectService.detectDocumentTextGcs(ocrPathDto.getGcsSourcePath());
		} catch (Exception e) {
			result = new ResponseEntity<>(e.getMessage() == null ? "NullPointer" : e.getMessage(),
					HttpStatus.BAD_REQUEST);
		}
		return result;
	}

	@GetMapping("test")
	public String testOCRController() {
		return "Hello OCR";
	}
}
