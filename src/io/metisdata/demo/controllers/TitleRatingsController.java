package io.metisdata.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.metisdata.demo.models.TitleRating;
import io.metisdata.demo.repositories.TitleRatingRepository;

@RestController
public class TitleRatingsController {
    @Autowired
    private TitleRatingRepository titleRatingRepository;

	@GetMapping("/titles/ratings/best")
	public List<TitleRating> getBestMovies() {
		return titleRatingRepository.findByAverageRating(10.0);
	}
}
