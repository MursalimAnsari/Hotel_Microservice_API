package com.hotel.booking.service.impl;

import com.hotel.booking.dto.LoginRequest;
import com.hotel.booking.dto.Response;
import com.hotel.booking.dto.UserDto;
import com.hotel.booking.entity.User;
import com.hotel.booking.exception.OurException;
import com.hotel.booking.repository.UserRepository;
import com.hotel.booking.service.interfac.IUserService;
import com.hotel.booking.utils.JWTUtils;
import com.hotel.booking.utils.Utils;
import jdk.jshell.execution.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

   @Autowired
   private UserRepository userRepository;
   @Autowired
   private PasswordEncoder passwordEncoder;
   @Autowired
   private JWTUtils jwtUtils;
   @Autowired
   private AuthenticationManager authenticationManager;

    @Override
    public Response register(User user) {
        Response response=new Response();

        try{

            if(user.getRole()==null || user.getRole().isBlank()){
                user.setRole("USER");
            }

            if(userRepository.existsByEmail(user.getEmail())){
                throw  new OurException(user.getEmail()  +" already exists !!");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            UserDto userDto = Utils.mapUserEntityToUserDto(savedUser);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUser(userDto);

        }catch (OurException e){
           response.setStatusCode(400);
           response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error saving user " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response login(LoginRequest loginRequest) {

        Response response=new Response();

        try{

            authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()));

            var user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(()-> new OurException("User not found !!"));
            var token = jwtUtils.generateToken(user);
            response.setToken(token);
            response.setExpirationTime("7 days");
            response.setRole(user.getRole());
            response.setMessage("successful");
            response.setStatusCode(200);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error logging in  " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllUsers() {
        Response response=new Response();

        try {
            List<User> userList = userRepository.findAll();
            List<UserDto> userDtoList = Utils.mapUserListEntityToUserListDto(userList);

            response.setUserList(userDtoList);
            response.setMessage("successful");
            response.setStatusCode(200);
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error in getting users " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserBookingHistory(String userId) {
        Response response=new Response();

        try{
            User user = userRepository.findById(Long.valueOf(userId))
                    .orElseThrow(()-> new OurException("User Not Found"));
            UserDto userDto = Utils.mapUserEntityToUserDtoPlusUserBookingAndRoom(user);

            response.setMessage("successful");
            response.setStatusCode(200);
            response.setUser(userDto);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting user bookings  " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteUser(String userId) {
        Response response=new Response();

        try{
            userRepository.findById(Long.valueOf(userId))
                    .orElseThrow(()-> new OurException("User Not Found"));

            userRepository.deleteById(Long.valueOf(userId));
            response.setMessage("successful");
            response.setStatusCode(200);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error deleting user " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserById(String userId) {
        Response response=new Response();

        try{
            User user = userRepository.findById(Long.valueOf(userId))
                    .orElseThrow(()-> new OurException("User Not Found"));
            UserDto userDto = Utils.mapUserEntityToUserDto(user);

            response.setMessage("successful");
            response.setStatusCode(200);
            response.setUser(userDto);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting user by id " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getMyInfo(String email) {
        Response response=new Response();

        try{
            User user = userRepository.findByEmail(email)
                    .orElseThrow(()-> new OurException("User Not Found"));
            UserDto userDto = Utils.mapUserEntityToUserDto(user);

            response.setMessage("successful");
            response.setStatusCode(200);
            response.setUser(userDto);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting user information  " + e.getMessage());
        }
        return response;
    }
}
