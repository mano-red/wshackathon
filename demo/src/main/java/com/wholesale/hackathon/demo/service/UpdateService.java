package com.wholesale.hackathon.demo.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.wholesale.hackathon.demo.dto.OcrInputDto;
import com.wholesale.hackathon.demo.dto.OcrOutputDto;
import com.wholesale.hackathon.demo.dto.User;
import com.wholesale.hackathon.demo.repo.UserRepository;

@Service
public class UpdateService {
	@Autowired
	UserRepository repo;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private DetectService detectService;

	@Value("${file.path}")
	private String path;

	public void save(User user) {
		repo.save(user);

	}

	public List<User> getData() {
		Iterable<User> user = repo.findAll();

		List<User> users = new ArrayList<>();
		user.forEach(e -> users.add(e));

		return users;

	}

	public void doUpload(List<MultipartFile> files, String depName, String clientId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<String, Object>();
		for (MultipartFile file : files) {

			try {
				body.add("file",
						new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String fileName = null;

			try {
				fileName = storeFile(file);
			} catch (Exception e) {
				e.printStackTrace();
			}

			System.out.println(fileName);
			String fileExtension = "." + detectService.getFileExtension(fileName);

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
			int randomNum = ThreadLocalRandom.current().nextInt(33000, 99000 + 1);
			String url = "http://34.70.70.22:8081/upload-files?bucket_name=codemongers.appspot.com&source_file_name="
					+ fileName + "&destination_blob_name=" + depName + "|" + clientId + "|" + String.valueOf(randomNum)
					+ fileExtension;

			String ocrBaseUrl = "gs://codemongers.appspot.com/";
			String ocrUrl = ocrBaseUrl + depName + "/" + clientId + "/" + String.valueOf(randomNum) + fileExtension;
			System.out.println(url);
			ResponseEntity<String> yes = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
			OcrInputDto ocrInputDto = new OcrInputDto();
			ocrInputDto.setGcsSourcePath(ocrUrl);
			ocrInputDto.setGcsDestinationPath(ocrUrl + "Text");

			System.out.println("yes.getStatusCode() " + yes.getStatusCode());
			if (yes.getStatusCode() == HttpStatus.OK) {
				try {
					OcrOutputDto ocrOutputDto = detectService.detectText(ocrInputDto);
					System.out.println(ocrOutputDto.getDocName() + " " + ocrOutputDto.getExtractedText());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			System.out.println(yes);
		}

	}

	public String storeFile(MultipartFile file) throws Exception {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {
			if (fileName.contains("..")) {
				throw new Exception("Sorry! Filename contains invalid path sequence " + fileName);
			}

			createDirectory(path);

			FileOutputStream out = new FileOutputStream(path + fileName);
			copyStream(file.getInputStream(), out);
			out.close();

			return fileName;
		} catch (IOException ex) {
			throw new Exception("Could not store file " + fileName + ". Please try again!", ex);
		}
	}

	public static void copyStream(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

	private void createDirectory(String... directory) {
		Stream.of(directory).forEach(dir -> {
			try {
				Path dirPath = new File(new File(dir).getCanonicalPath()).toPath();
				if (!Files.exists(dirPath)) {
					Files.createDirectories(dirPath);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

	}

	public void doDownload() {
		restTemplate.getForEntity(
				"http://34.70.70.22:8081/download-files?bucket_name=codemongers.appspot.com&source_blob_name=hack-hashed_name.pdf&destination_file_name=business_name.pdf",
				Integer.class);

	}

}
