package com.nguyenvanancodeweb.lakesidehotel.controller;

import com.nguyenvanancodeweb.lakesidehotel.exception.UserAlreadyExistsException;
import com.nguyenvanancodeweb.lakesidehotel.model.User;
import com.nguyenvanancodeweb.lakesidehotel.request.ChangePasswordRequest;
import com.nguyenvanancodeweb.lakesidehotel.request.CreateAccountRequest;
import com.nguyenvanancodeweb.lakesidehotel.request.LoginRequest;
import com.nguyenvanancodeweb.lakesidehotel.request.RegisterRequest;
import com.nguyenvanancodeweb.lakesidehotel.response.DTO.ApiResponseDTO;
import com.nguyenvanancodeweb.lakesidehotel.response.DTO.DataResponseDTO;
import com.nguyenvanancodeweb.lakesidehotel.response.JwtResponse;
import com.nguyenvanancodeweb.lakesidehotel.security.jwt.JwtUtils;
import com.nguyenvanancodeweb.lakesidehotel.security.user.HotelUserDetails;
import com.nguyenvanancodeweb.lakesidehotel.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author Simpson Alfred
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IUserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest requestUser){
        try{
            userService.registerUser(requestUser);
            return ResponseEntity.ok("Registration successful!");

        }catch (UserAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest request){
        Authentication authentication =
                authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtTokenForUser(authentication);
        HotelUserDetails userDetails = (HotelUserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority).toList();

        return ResponseEntity.ok(new JwtResponse(
                userDetails.getId(),
                userDetails.getEmail(),
                jwt,
                roles));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication == null || authentication instanceof UsernamePasswordAuthenticationToken) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Chưa đăng nhập tài khoản!");
//        }

        // Xóa thông tin xác thực khỏi SecurityContext
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Đăng xuất thành công");
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponseDTO<Void>> changePassword(@RequestBody ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponseDTO<>(false,
                    "Chưa xác thực tài khoản"));
        }

        try {
            HotelUserDetails userDetails = (HotelUserDetails) authentication.getPrincipal();
            String userEmail = userDetails.getEmail();

            userService.changePassword(userEmail, request.getOldPassword(), request.getNewPassword());

            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Đổi mật khẩu thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponseDTO<>(false,
                    "Phiên đăng nhập hết hạn"));
        }
    }

    @PostMapping("/create-user")
    public ResponseEntity<ApiResponseDTO<String>> createAccount(@RequestBody CreateAccountRequest request) {
        try{
            String rawPassword = userService.createAccount(request);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Tạo tài khoản thành công",
                    new DataResponseDTO<>(null, rawPassword)));

        }catch (UserAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponseDTO<>(false, e.getMessage()));
        }
    }
}