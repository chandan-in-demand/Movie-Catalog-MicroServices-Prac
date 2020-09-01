package com.jaxs.ratingdataservice.resources;

import com.jaxs.ratingdataservice.model.Rating;
import com.jaxs.ratingdataservice.model.UserRating;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/ratingsdata")
public class RatingResource {

    @RequestMapping("/movieId")
    public Rating getRating(@PathVariable("movieId") String movieId){
        return new Rating(movieId, 4);
    }

    @RequestMapping("user/{userId}")
    public UserRating getUserRating(@PathVariable("userId") String userId){
        List<Rating> rating = Arrays.asList(
                new Rating("movie1",3),
                new Rating("movie2", 5)
        );
        UserRating userRating = new UserRating();
        userRating.setUserRating(rating);
        return userRating;
    }

}
