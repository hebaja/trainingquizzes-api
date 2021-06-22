package com.trainingquizzes.english.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.trainingquizzes.english.model.FileChecksum;
import com.trainingquizzes.english.repository.FileChecksumRepository;

@Controller
@RequestMapping("/api")
public class FileChecksumRest {

	@Autowired
	private FileChecksumRepository repository;
	
	@RequestMapping(value = "file_checksum", method = RequestMethod.GET)
	public ResponseEntity<FileChecksum> getFileChecksum(@RequestParam("file") String file) {
		
		Optional<FileChecksum> optionalFileChecksum = repository.findById(file);
		FileChecksum fileChecksum = optionalFileChecksum.orElse(null);
		
		if(fileChecksum != null) {
			return ResponseEntity.ok(fileChecksum);
		}
		return null;
	}
	
	@RequestMapping(value = "json/{file}", method = RequestMethod.GET)
	public String returnJson(@PathVariable("file") String file, @RequestParam(value = "checksum", required = false) String receivedChecksum) {
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("json-files/");
		stringBuilder.append(file);
		
		Optional<FileChecksum> optionalFileChecksum = repository.findById(file);
		FileChecksum fileChecksum = optionalFileChecksum.orElse(null);
		
		return "files/json/" + file;
	}
}
