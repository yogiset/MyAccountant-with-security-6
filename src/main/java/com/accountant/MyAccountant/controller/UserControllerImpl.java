package com.accountant.MyAccountant.controller;

import com.accountant.MyAccountant.constant.ApiConstant;
import com.accountant.MyAccountant.dto.AuthenticationResponse;
import com.accountant.MyAccountant.entity.User;
import com.accountant.MyAccountant.service.UserService;
import com.accountant.MyAccountant.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        try {
            return userService.signUp(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return UserUtils.getResponseEntity(ApiConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @Override
//    public ResponseEntity<String> login(User user) {
//        try {
//            return userService.login(user);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return UserUtils.getResponseEntity(ApiConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    @Override
    public ResponseEntity<AuthenticationResponse> authenticate(User user) {
        return userService.authenticate(user);
    }


    @Override
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        try {

            return userService.verifyAccount(token);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return UserUtils.getResponseEntity(ApiConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public List<User> listUser() {

        return userService.listUser();

    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap,String userRole) {
        try {
            return userService.forgotPassword(requestMap,userRole);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return UserUtils.getResponseEntity(ApiConstant.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap,User userr,String userEmail,String userRole) {
        try {

            return userService.changePassword(requestMap,userr,userEmail,userRole);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return UserUtils.getResponseEntity(ApiConstant.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
