package com.hotel.booking.service.interfac;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

public interface IUploadImageToCloudinary {
    public Map uploadImageToCloudinary(MultipartFile file);
}
