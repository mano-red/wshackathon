package com.wholesale.hackathon.demo.dto;

public class OcrInputDto {

	private String gcsSourcePath;
	private String gcsDestinationPath;
	
	public String getGcsSourcePath() {
		return gcsSourcePath;
	}
	public void setGcsSourcePath(String gcsSourcePath) {
		this.gcsSourcePath = gcsSourcePath;
	}
	public String getGcsDestinationPath() {
		return gcsDestinationPath;
	}
	public void setGcsDestinationPath(String gcsDestinationPath) {
		this.gcsDestinationPath = gcsDestinationPath;
	}
	
	
}
