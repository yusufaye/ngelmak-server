package org.open.ngelmakproject.web.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ResourceController {
	
	@GetMapping("/")
	public String getResource() {
		return "a value...";
	}
	
}