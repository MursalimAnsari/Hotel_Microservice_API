package com.hotel.booking.service.interfac;

import com.hotel.booking.dto.Response;
import com.hotel.booking.entity.Booking;

public interface IBookingService {

    Response saveBooking(Long roomId, Long userId, Booking bookingRequest);
    Response findBookingByConfirmationCode(String confirmationCode);
    Response getAllBookings();
    Response cancelBooking(Long bookingId);

}
