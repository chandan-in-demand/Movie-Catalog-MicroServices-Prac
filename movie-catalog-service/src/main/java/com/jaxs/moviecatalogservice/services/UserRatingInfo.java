package com.jaxs.moviecatalogservice.services;

import com.jaxs.moviecatalogservice.model.Rating;
import com.jaxs.moviecatalogservice.model.UserRating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class UserRatingInfo {
    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getFallbackUserRating",
//            using circuit breaker method
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "50"),
                    //last n=10 request to be checked
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
                    //failure percentage of last n values.
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "10"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "1000"),
            }
    )
    public UserRating getUserRating(@PathVariable("userId") String userId) {
        return restTemplate.getForObject("http://rating-data-service/ratingsdata/user/" + userId,
                UserRating.class);
    }

    private UserRating getFallbackUserRating(@PathVariable("userId") String userID) {
        UserRating userRating = new UserRating();
        userRating.setUserId(userID);
        userRating.setRatings(
                Arrays.asList(
                        new Rating("0", 0)
                )
        );
        return userRating;
    }
}
