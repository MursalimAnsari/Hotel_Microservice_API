package com.hotel.booking.utils;

import com.hotel.booking.dto.BookingDto;
import com.hotel.booking.dto.RoomDto;
import com.hotel.booking.dto.UserDto;
import com.hotel.booking.entity.Booking;
import com.hotel.booking.entity.Room;
import com.hotel.booking.entity.User;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

import static com.hotel.booking.utils.Constants.ALPHANUMERIC_STRING;

public class Utils {
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateRandomConfirmationCode(int length){

        StringBuilder builder = new StringBuilder();
        for (int i=0; i<length; i++){
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            char randomCharacter = ALPHANUMERIC_STRING.charAt(randomIndex);
            builder.append(randomCharacter);
        }
      return  builder.toString();
    }

    public static UserDto mapUserEntityToUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setRole(user.getRole());

       return userDto;
    }

    public static RoomDto mapRoomEntityToRoomDto(Room room){
        RoomDto roomDto= new RoomDto();

        roomDto.setId(room.getId());
        roomDto.setRoomType(room.getRoomType());
        roomDto.setRoomPrice(room.getRoomPrice());
        roomDto.setRoomDescription(room.getRoomDescription());
        roomDto.setRoomPhotoUrl(room.getRoomPhotoUrl());
        return roomDto;
    }

    public static BookingDto mapBookingEntityToBookingDto(Booking booking){
        BookingDto bookingDto = new BookingDto();

        bookingDto.setId(booking.getId());
        bookingDto.setCheckInDate(booking.getCheckInDate());
        bookingDto.setCheckOutDate(booking.getCheckOutDate());
        bookingDto.setNumberOfAdults(booking.getNumberOfAdults());
        bookingDto.setNumberOfChildren(booking.getNumberOfChildren());
        bookingDto.setTotalNumberOfGuests(booking.getTotalNumberOfGuests());
        bookingDto.setBookingConfirmationCode(booking.getBookingConfirmationCode());
        return bookingDto;
    }

    public static RoomDto mapRoomEntityToRoomDtoPlusBookings(Room room){
        RoomDto roomDto= new RoomDto();

        roomDto.setId(room.getId());
        roomDto.setRoomType(room.getRoomType());
        roomDto.setRoomPrice(room.getRoomPrice());
        roomDto.setRoomDescription(room.getRoomDescription());
        roomDto.setRoomPhotoUrl(room.getRoomPhotoUrl());

              if(room.getBookings() != null){
                   roomDto.setBookings(room.getBookings()
                    .stream()
                    .map(Utils :: mapBookingEntityToBookingDto)
                    .collect(Collectors.toList()));
               }
        return roomDto;
    }

    public static BookingDto mapBookingEntityToBookingDtoPlusBookedRooms(Booking booking, boolean mapUser){

        BookingDto bookingDto = new BookingDto();

        bookingDto.setId(booking.getId());
        bookingDto.setCheckInDate(booking.getCheckInDate());
        bookingDto.setCheckOutDate(booking.getCheckOutDate());
        bookingDto.setNumberOfAdults(booking.getNumberOfAdults());
        bookingDto.setNumberOfChildren(booking.getNumberOfChildren());
        bookingDto.setTotalNumberOfGuests(booking.getTotalNumberOfGuests());
        bookingDto.setBookingConfirmationCode(booking.getBookingConfirmationCode());

        if(mapUser){
            bookingDto.setUser(Utils.mapUserEntityToUserDto(booking.getUser()));
        }

        if(booking.getRoom()!=null){
            RoomDto roomDto= new RoomDto();

            roomDto.setId(booking.getRoom().getId());
            roomDto.setRoomType(booking.getRoom().getRoomType());
            roomDto.setRoomPrice(booking.getRoom().getRoomPrice());
            roomDto.setRoomDescription(booking.getRoom().getRoomDescription());
            roomDto.setRoomPhotoUrl(booking.getRoom().getRoomPhotoUrl());
            bookingDto.setRoom(roomDto);
        }
        return bookingDto;
    }

    public static  UserDto mapUserEntityToUserDtoPlusUserBookingAndRoom(User user){

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setRole(user.getRole());

           if (!user.getBookings().isEmpty()){
               userDto.setBookings(
                  user.getBookings()
                  .stream()
                  .map(booking -> mapBookingEntityToBookingDtoPlusBookedRooms(booking, false))
                          .collect(Collectors.toList())
                          );
             }
        return userDto;
    }

    public static List<UserDto> mapUserListEntityToUserListDto(List<User> users){
        return users
                .stream()
                .map(Utils::mapUserEntityToUserDto).collect(Collectors.toList());
    }

    public static List<RoomDto> mapRoomListEntityToRoomListDto(List<Room>rooms){
        return rooms
                .stream()
                .map(Utils::mapRoomEntityToRoomDto).collect(Collectors.toList());
    }

    public static List<BookingDto> mapBookingListEntityTyBookingListDto(List<Booking>bookings){
        return bookings
                .stream()
                .map(Utils::mapBookingEntityToBookingDto).collect(Collectors.toList());
    }

}
