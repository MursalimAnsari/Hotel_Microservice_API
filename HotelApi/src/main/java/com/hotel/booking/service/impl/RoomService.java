package com.hotel.booking.service.impl;

import com.hotel.booking.dto.Response;
import com.hotel.booking.repository.BookingRepository;
import com.hotel.booking.repository.RoomRepository;
import com.hotel.booking.service.interfac.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class RoomService implements IRoomService {


    @Autowired
    RoomRepository roomRepository;
    @Autowired
    private BookingRepository bookingRepository;



    @Override
    public Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String Description) {
        return null;
    }

    @Override
    public List<String> getAllRoomTypes() {
        return null;
    }

    @Override
    public Response getAllRooms() {
        return null;
    }

    @Override
    public Response deleteRoom(Long roomId) {
        return null;
    }

    @Override
    public Response updateRoomDetails(Long roomId, String description, String roomType, BigDecimal roomPrice, MultipartFile roomPhoto) {
        return null;
    }

    @Override
    public Response getRoomById(Long roomId) {
        return null;
    }

    @Override
    public Response getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        return null;
    }

    @Override
    public Response getAllAvailableRooms() {
        return null;
    }
}
