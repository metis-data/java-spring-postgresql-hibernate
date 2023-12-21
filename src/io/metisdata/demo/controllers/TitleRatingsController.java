package io.metisdata.demo.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.metisdata.demo.models.TitleRating;

@RestController
public class TitleRatingsController {
	@GetMapping("/titles/ratings/best")
	public TitleRating[] getBestMovies() {
		return new TitleRating[] {
			new TitleRating()
		};
	}
}
