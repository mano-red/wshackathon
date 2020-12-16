package com.wholesale.hackathon.demo.dto;

public class OcrOutputDto {

	private String docName;
	private String extractedText;

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getExtractedText() {
		return extractedText;
	}

	public void setExtractedText(String extractedText) {
		this.extractedText = extractedText;
	}

}
