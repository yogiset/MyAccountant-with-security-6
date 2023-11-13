package com.accountant.MyAccountant.service;

import com.accountant.MyAccountant.constant.ApiConstant;
import com.accountant.MyAccountant.entity.User;
import com.accountant.MyAccountant.exception.AllException;
import com.accountant.MyAccountant.jwt.JwtUtil;
import com.accountant.MyAccountant.repository.UserRepository;
import com.accountant.MyAccountant.utils.UserUtils;
import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Configuration
public class UserService{

    private final UserRepository userRepository;

    private final MailService mailService;

    private final JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;


    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup{}", requestMap);
        try {
            if (!validateSignUpMap(requestMap)) {
                return UserUtils.getResponseEntity(ApiConstant.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }

            String email = requestMap.get("email");
            if (userRepository.existsByEmail(email)) {
                return UserUtils.getResponseEntity("Email already exists", HttpStatus.BAD_REQUEST);
            }

            String name = requestMap.get("name");
            Optional<User> userOptional = userRepository.findByName(name);
            if (userOptional.isPresent()) {
                return UserUtils.getResponseEntity("Username already exists", HttpStatus.BAD_REQUEST);
            }

            String contact = requestMap.get("contactNumber");
            Optional<User> userOpt = userRepository.findByContactNumber(contact);
            if (userOpt.isPresent()) {
                return UserUtils.getResponseEntity("Phone Number already exists", HttpStatus.BAD_REQUEST);
            }

            User user = createUserFromMap(requestMap);

            // Send activation email with the token
            mailService.sendActivationEmail(user);

            return UserUtils.getResponseEntity("Successfully Registered, Please Check Your Email And Activate your account", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return UserUtils.getResponseEntity(ApiConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSignUpMap(Map<String, String> requestMap) {
        return requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password");
    }

    private User createUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));

        String rawPassword = requestMap.get("password");
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);

        user.setStatus("false");
        user.setRole("user");
        user.setCreated(Instant.now());
        user.setEnabled(false);
        return userRepository.save(user);
    }


    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside Login");
        try {
            String email = requestMap.get("email");
            String password = requestMap.get("password");


            Optional<User> userOpt1 = userRepository.findByEmail(email);
            if (!userOpt1.isPresent()) {
                return new ResponseEntity<>("{\"message\":\"Our System didn't find your email, Please Register first !\"}", HttpStatus.BAD_REQUEST);
            }
            // Retrieve the user by email from the repository
            User user = userRepository.findByEmailId(email);
            if (user != null) {
                // Check if the provided password matches the stored password
                if (passwordEncoder.matches(password, user.getPassword())) {
                    if ("true".equalsIgnoreCase(user.getStatus())) {
                        // Generate the JWT token for the authenticated user
                        String jwtToken = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getName());

                        // Return the token as a JSON response
                        return new ResponseEntity<>("{\"token\":\"" + jwtToken + "\"}", HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("{\"message\":\"Please Verify your email first.\"}", HttpStatus.BAD_REQUEST);
                    }
                }
            }


        } catch (Exception ex) {
            log.error("{}", ex);
        }
        return new ResponseEntity<>("{\"message\":\"Bad Credentials. Please check your password or email\"}", HttpStatus.BAD_REQUEST);
    }


    public ResponseEntity<String> verifyAccount(String token) {
        try {
            // Validate and decode the token
            Claims claims = jwtUtil.validateAndExtractClaims(token);
            if (claims == null) {
                return UserUtils.getResponseEntity("Invalid or expired token.", HttpStatus.BAD_REQUEST);
            }

            String email = claims.getSubject();

            // Retrieve the user by email from the database
            Optional<User> userOptional = userRepository.findByEmail(email);

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                if (!user.getStatus().equals("true")) {
                    // Update the user's status to "true"
                    user.setStatus("true");
                    userRepository.save(user);

                    return UserUtils.getResponseEntity("Account verified successfully.", HttpStatus.OK);
                } else {
                    return UserUtils.getResponseEntity("Account is already verified.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return UserUtils.getResponseEntity("No user with this email exists.", HttpStatus.BAD_REQUEST);
            }
        } catch (JwtException ex) {
            log.error("Error verifying token: {}", ex);
            return UserUtils.getResponseEntity("Invalid token.", HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            log.error("Error verifying account: {}", ex);
            return UserUtils.getResponseEntity(ApiConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public List<User> listUser() {
        return userRepository.findAll();
    }

//    @Override
//    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
//        try {
//            log.info("Inside changePassword");
//
//            // Get the current user's email from the JWT token
//            String currentEmail = jwtFilter.getCurrentUser();
//
//            if (currentEmail != null) {
//                log.info("Current Email: " + currentEmail);
//
//                // Retrieve the user from the database
//                User user = userRepository.findByEmailId(currentEmail);
//
//                if (user != null) {
//                    String oldPassword = requestMap.get("oldPassword");
//                    String newPassword = requestMap.get("newPassword");
//
//                    // Encode the old password using the same password encoder
//                    String encodedOldPassword = passwordEncoder.encode(oldPassword);
//
//                    // Compare the encoded old password with the stored hashed password
//                    if (passwordEncoder.matches(oldPassword, user.getPassword())) {
//                        // Encode the new password before saving it
//                        String encodedNewPassword = passwordEncoder.encode(newPassword);
//
//                        // Update the user's password with the new hashed password
//                        user.setPassword(encodedNewPassword);
//                        userRepository.save(user);
//
//                        // Password updated successfully
//                        return UserUtils.getResponseEntity("Password Updated Successfully", HttpStatus.OK);
//                    } else {
//                        // Incorrect old password
//                        return UserUtils.getResponseEntity("Incorrect Old Password", HttpStatus.BAD_REQUEST);
//                    }
//                }
//
//                // User not found
//                return UserUtils.getResponseEntity("User not found", HttpStatus.INTERNAL_SERVER_ERROR);
//            } else {
//                log.error("Token is null or empty");
//                return UserUtils.getResponseEntity("Token is null or empty", HttpStatus.BAD_REQUEST);
//            }
//        } catch (Exception ex) {
//            // Log the exception for debugging purposes
//            log.error("An error occurred while changing the password", ex);
//
//            // Handle the exception more gracefully and provide a meaningful error message
//            return UserUtils.getResponseEntity("An error occurred while changing the password.", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }


    public ResponseEntity<String> changePassword(Map<String, String> requestMap,User userr,String userEmail,String userRole) {
        try {
            log.info("Inside changePassword");

            log.info("Received request with payload: {}", requestMap);

//            userRole = requestMap.get("role");
//            log.info("User Role from request: {}", userRole);
//            if (userRole == null || userRole.isEmpty()) {
//                log.error("role is null or empty");
//                throw new AllException("role is null or empty");
//            }

//            if ("admin".equalsIgnoreCase(userRole)) {
//                User roles = userRepository.findByRole(userRole);
//
//                if (roles != null) {

            userEmail = requestMap.get("email");

            if (userEmail != null) {
                log.info("Current Email: " + userEmail);

                // Retrieve the user from the database
                User user = userRepository.findByEmailId(userEmail);

                if (user != null) {
                    String oldPassword = requestMap.get("oldPassword");
                    String newPassword = requestMap.get("newPassword");

                    // Encode the old password using the same password encoder
                    String encodedOldPassword = passwordEncoder.encode(oldPassword);

                    // Compare the encoded old password with the stored hashed password
                    if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                        // Encode the new password before saving it
                        String encodedNewPassword = passwordEncoder.encode(newPassword);

                        // Update the user's password with the new hashed password
                        user.setPassword(encodedNewPassword);
                        userRepository.save(user);

                        // Password updated successfully
                        return UserUtils.getResponseEntity("Password Updated Successfully", HttpStatus.OK);
                    } else {
                        // Incorrect old password
                        return UserUtils.getResponseEntity("Incorrect Old Password", HttpStatus.BAD_REQUEST);
                    }
                }

                return UserUtils.getResponseEntity("User not found", HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                log.error("Token is null or empty");
                return UserUtils.getResponseEntity("Token is null or empty", HttpStatus.BAD_REQUEST);
            }
//                } else {
//                    throw new AllException("Role not found");
//                }
//            } else {
//                throw new AllException("Invalid User Role");
//            }

        } catch (Exception ex) {
            // Log the exception for debugging purposes
            log.error("An error occurred while changing the password", ex);

            // Handle the exception more gracefully and provide a meaningful error message
            return UserUtils.getResponseEntity("An error occurred while changing the password.", HttpStatus.INTERNAL_SERVER_ERROR);
        }



    }


    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap,String userRole) {
        log.info("Inside forgotPassword");
        log.info("Received request with payload: {}", requestMap);

        userRole = requestMap.get("role");
        log.info("User Role from request: {}", userRole);

        try {
            User user = userRepository.findByEmailId(requestMap.get("email"));
            if (!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail())) {
                // Generate a new random password (you can customize this part)
                String newPassword = generateRandomPassword(); // Implement this method

                // Hash the new password with BCryptPasswordEncoder
                String hashedPassword = passwordEncoder.encode(newPassword);

                // Update the user's password in the database with the new hash
                user.setPassword(hashedPassword);
                userRepository.save(user);

                // Send the new password via email
                mailService.forgotMail(user.getEmail(), "New Password for MyAccountant Management System", newPassword);

                return UserUtils.getResponseEntity("Check your mail for the new password", HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return UserUtils.getResponseEntity("Email did not exists", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 12;

    private String generateRandomPassword() {
        StringBuilder password = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = secureRandom.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }
}