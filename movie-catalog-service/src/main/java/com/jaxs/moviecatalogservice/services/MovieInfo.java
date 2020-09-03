package com.jaxs.moviecatalogservice.services;

import com.jaxs.moviecatalogservice.model.CatalogItem;
import com.jaxs.moviecatalogservice.model.Movie;
import com.jaxs.moviecatalogservice.model.Rating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MovieInfo {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getFallbackCatalogItem",
//            using bulkhead Method/ creating seperate pool

            threadPoolKey = "movieInfoPool",
            threadPoolProperties = {
                @HystrixProperty(name ="coreSize", value = "20"),
                @HystrixProperty(name="maxQueueSize", value = "10")
            }
    )
    public CatalogItem getCatalogItem(Rating rating) {
        Movie movie = restTemplate.getForObject("http://movie-info-service/movies/"+ rating.getMovieId(),Movie.class);
        return new CatalogItem("Movie name not found","", rating.getRating());
    }

    private CatalogItem getFallbackCatalogItem(Rating rating){
        return new CatalogItem("Movie Test", "Desc", rating.getRating());
    }
}
