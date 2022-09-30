package de.wellnerbou.springsecuritydemo.controller;

import de.wellnerbou.springsecuritydemo.dto.ExampleData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping(value = "/api")
@RestController
public class ExampleController {
	private final List<ExampleData> exampleDatas = new ArrayList<>();

	@GetMapping(value = "list")
	public List<ExampleData> getData() {
		return exampleDatas;
	}

	@PostMapping(value = "save")
	public ResponseEntity<ExampleData> addData(final @RequestBody ExampleData exampleData) {
		exampleDatas.add(exampleData);
		return ResponseEntity.status(HttpStatus.CREATED).body(exampleData);
	}
}
