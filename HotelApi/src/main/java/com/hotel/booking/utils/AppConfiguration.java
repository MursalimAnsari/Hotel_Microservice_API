package com.hotel.booking.utils;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.*;

@Configuration
public class AppConfiguration {
    @Bean
    public Cloudinary cloudinaryInit(){

        Map map = new HashMap();
        map.put("cloud_name",Constants.CLOUD_NAME);
        map.put("api_key",Constants.API_KEY);
        map.put("api_secret",Constants.API_SECRET);
        map.put("secured", true);

        return new Cloudinary(map);
    }
}
