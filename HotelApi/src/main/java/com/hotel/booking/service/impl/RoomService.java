package com.hotel.booking.service.impl;

import com.hotel.booking.dto.Response;
import com.hotel.booking.dto.RoomDto;
import com.hotel.booking.entity.Room;
import com.hotel.booking.exception.OurException;
import com.hotel.booking.repository.BookingRepository;
import com.hotel.booking.repository.RoomRepository;
import com.hotel.booking.service.interfac.IUploadImageToCloudinary;
import com.hotel.booking.service.interfac.IRoomService;
import com.hotel.booking.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class RoomService implements IRoomService {


    @Autowired
    RoomRepository roomRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private IUploadImageToCloudinary uploadImageService;


    @Override
    public Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description) {
        Response response=new Response();

        try{
            Map data = uploadImageService.uploadImageToCloudinary(photo);
            String imageUrl = (String) data.get("url");
            Room room = new Room();
            room.setRoomPhotoUrl(imageUrl);
            room.setRoomType(roomType);
            room.setRoomPrice(roomPrice);
            room.setRoomDescription(description);

            Room savedRoom = roomRepository.save(room);

            RoomDto roomDto = Utils.mapRoomEntityToRoomDto(savedRoom);

            response.setRoom(roomDto);
            response.setMessage("successful");
            response.setStatusCode(200);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error adding room information  " + e.getMessage());
        }
        return response;
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findByDistinctRoomType();
    }

    @Override
    public Response getAllRooms() {
        Response response=new Response();

        try{
            List<Room> roomList = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<RoomDto> roomDtos = Utils.mapRoomListEntityToRoomListDto(roomList);
            response.setMessage("successful");
            response.setStatusCode(200);
            response.setRoomList(roomDtos);
        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all rooms " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteRoom(Long roomId) {

        Response response=new Response();

        try {

            roomRepository.findById(roomId)
                    .orElseThrow(() -> new OurException("Room not Found"));
            roomRepository.deleteById(roomId);
            response.setMessage("successful");
            response.setStatusCode(200);
        } catch (OurException e){
                response.setStatusCode(404);
                response.setMessage(e.getMessage());
        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all rooms " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateRoomDetails(Long roomId, String description, String roomType, BigDecimal roomPrice, MultipartFile roomPhoto) {

        Response response=new Response();

        try {
           Room room= roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room not Found"));
            String imageUrl ="";

            if(roomPhoto!= null || !roomPhoto.isEmpty()){
               Map data = uploadImageService.uploadImageToCloudinary(roomPhoto);
               imageUrl = (String) data.get("url");
           }
            if (roomType!=null)  room.setRoomType(roomType);
            room.setRoomPrice(roomPrice);
            room.setRoomDescription(description);
            room.setRoomPhotoUrl(imageUrl);

            Room updatedRoom = roomRepository.save(room);
            RoomDto roomDto = Utils.mapRoomEntityToRoomDto(updatedRoom);

            response.setMessage("successful");
            response.setStatusCode(200);
            response.setRoom(roomDto);
        } catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error updating room details " + e.getMessage());
        }
        return response;
    }
    @Override
    public Response getRoomById(Long roomId) {

        Response response=new Response();
        try {

          Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new OurException("Room not Found"));

          RoomDto roomDto = Utils.mapRoomEntityToRoomDto(room);
          response.setMessage("successful");
          response.setStatusCode(200);
          response.setRoom(roomDto);
        } catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting room " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {

        Response response=new Response();

        try {
            List<Room> availableRooms=
                    roomRepository.findAvailableRoomsByDateAndTypes(checkInDate,checkOutDate,roomType);
            List<RoomDto> roomDtoList =
                    Utils.mapRoomListEntityToRoomListDto(availableRooms);

            response.setMessage("successful");
            response.setStatusCode(200);
            response.setRoomList(roomDtoList);
        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting available rooms within range" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllAvailableRooms() {
        Response response=new Response();

        try {

            List<Room> availableRooms = roomRepository.getAllAvailableRooms();
            List<RoomDto> roomDtoList = Utils.mapRoomListEntityToRoomListDto(availableRooms);
            response.setMessage("successful");
            response.setStatusCode(200);
            response.setRoomList(roomDtoList);
        } catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all available rooms " + e.getMessage());
        }
        return response;
    }
}
