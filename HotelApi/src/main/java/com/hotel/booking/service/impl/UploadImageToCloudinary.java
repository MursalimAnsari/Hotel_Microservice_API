package com.hotel.booking.service.impl;

import com.cloudinary.Cloudinary;
import com.hotel.booking.exception.OurException;
import com.hotel.booking.service.interfac.IUploadImageToCloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UploadImageToCloudinary implements IUploadImageToCloudinary {
    private final Cloudinary cloudinary;

    @Override
    public Map uploadImageToCloudinary(MultipartFile file)  {

        try {
            Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            return data;
        } catch (IOException e) {
            throw new OurException("Unable to upload image to cloud");
        }
    }
}
