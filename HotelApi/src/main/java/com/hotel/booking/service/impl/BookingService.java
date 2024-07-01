package com.hotel.booking.service.impl;

import com.hotel.booking.dto.BookingDto;
import com.hotel.booking.dto.Response;
import com.hotel.booking.dto.RoomDto;
import com.hotel.booking.entity.Booking;
import com.hotel.booking.entity.Room;
import com.hotel.booking.entity.User;
import com.hotel.booking.exception.OurException;
import com.hotel.booking.repository.BookingRepository;
import com.hotel.booking.repository.RoomRepository;
import com.hotel.booking.repository.UserRepository;
import com.hotel.booking.service.interfac.IBookingService;
import com.hotel.booking.service.interfac.IRoomService;

import com.hotel.booking.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BookingService implements IBookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private IRoomService roomService;


    @Override
    public Response saveBooking(Long roomId, Long userId, Booking bookingRequest) {

        Response response=new Response();

        try {
                if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
                    throw new IllegalArgumentException("check in date must come before check out date");
                }

                Room room = roomRepository.findById(roomId)
                        .orElseThrow(()-> new OurException("Room not Found"));
                User user = userRepository.findById(userId)
                    .orElseThrow(()-> new OurException("User not Found"));

                List<Booking> existingBookings = room.getBookings();

                if(!roomIsAvailable(bookingRequest, existingBookings)){
                    throw new OurException("Room is not available");
                }

                bookingRequest.setRoom(room);
                bookingRequest.setUser(user);

                // get booking confirmation code
                String confirmationCode  = Utils.generateRandomConfirmationCode(6);
                bookingRequest.setBookingConfirmationCode(confirmationCode);

                bookingRepository.save(bookingRequest);
                response.setMessage("successful");
                response.setStatusCode(200);
                response.setBookingConfirmationCode(confirmationCode);

        } catch (OurException e){
                response.setStatusCode(404);
                response.setMessage(e.getMessage());
        } catch (Exception e){
                response.setStatusCode(500);
                response.setMessage("Error saving booking " + e.getMessage());
        }
        return response;

    }

    private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings) {
        return  existingBookings
                .stream()
                .noneMatch(existingBooking ->
                  bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                  || (bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate()))
                  || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate()))
                  && (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                  || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate()))

                  && (bookingRequest.getCheckOutDate().isEqual(existingBooking.getCheckOutDate()))
                  || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate()))

                  && (bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                  || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate()))
                  && (bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                  || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate()))
                  && (bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))

                );
    }

    @Override
    public Response findBookingByConfirmationCode(String confirmationCode) {
        Response response=new Response();

        try {
            Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode)
                    .orElseThrow(()-> new OurException("Booking not Found"));

            BookingDto bookingDto = Utils.mapBookingEntityToBookingDtoPlusBookedRooms(booking,true);

            response.setMessage("successful");
            response.setStatusCode(200);
            response.setBooking(bookingDto);
        } catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting booking by  confirmation code " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllBookings() {
        Response response=new Response();

        try {
            List<Booking> bookings = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<BookingDto> bookingDtos = Utils.mapBookingListEntityTyBookingListDto(bookings);

            response.setMessage("successful");
            response.setStatusCode(200);
            response.setBookingList(bookingDtos);
        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all bookings " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response cancelBooking(Long bookingId) {
        Response response=new Response();

        try {
             bookingRepository.findById(bookingId)
                             .orElseThrow(()-> new OurException("Booking not Found"));
             bookingRepository.deleteById(bookingId);

            response.setMessage("room booking deleted successfully");
            response.setStatusCode(204);
        } catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error deleting a booking " + e.getMessage());
        }
        return response;
    }
}
