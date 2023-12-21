package io.metisdata.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import io.metisdata.demo.models.TitleRating;

@Repository
public interface TitleRatingRepository extends JpaRepository<TitleRating, String> {
    List<TitleRating> findByAverageRating(double averageRating);
}