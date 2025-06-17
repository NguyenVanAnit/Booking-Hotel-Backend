package com.nguyenvanancodeweb.lakesidehotel.service;

import com.nguyenvanancodeweb.lakesidehotel.exception.ResourceNotFoundException;
import com.nguyenvanancodeweb.lakesidehotel.exception.UserAlreadyExistsException;
import com.nguyenvanancodeweb.lakesidehotel.model.Role;
import com.nguyenvanancodeweb.lakesidehotel.model.User;
import com.nguyenvanancodeweb.lakesidehotel.repository.RoleRepository;
import com.nguyenvanancodeweb.lakesidehotel.repository.UserRepository;
import com.nguyenvanancodeweb.lakesidehotel.request.CreateAccountRequest;
import com.nguyenvanancodeweb.lakesidehotel.request.RegisterRequest;
import com.nguyenvanancodeweb.lakesidehotel.response.UserResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public void registerUser(RegisterRequest requestUser) {
        if(userRepository.existsByEmail(requestUser.getEmail())){
            throw new UserAlreadyExistsException("Email " + requestUser.getEmail() + " đã tồn tại");
        }
        if(userRepository.existsByPhoneNumber(requestUser.getPhoneNumber())){
            throw new UserAlreadyExistsException("Số điện thoại "+ requestUser.getPhoneNumber() + " đã tồn tại");
        }
        User user = new User();
        user.setEmail(requestUser.getEmail());
        user.setFullName(requestUser.getFullName());
        user.setPhoneNumber(requestUser.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(requestUser.getPassword()));
        System.out.println(user.getPassword());
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        user.setRoles(Collections.singletonList(userRole));
        userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        User theUser = getUserByUserId(id);
        if (theUser != null) {
            // Clear quan hệ với Role để tránh lỗi foreign key
            theUser.getRoles().clear();
            userRepository.save(theUser); // cập nhật lại trước khi xóa

            userRepository.deleteById(id);
        }
    }

    @Override
    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng"));
    }

    @Override
    public User getUserByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserResponse getUserResponse(String email) {
        User user = getUser(email);
        UserResponse userResponse = new UserResponse();
        userResponse.setEmail(user.getEmail());
        userResponse.setId(user.getId());
        userResponse.setFullName(user.getFullName());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        if (!user.getRoles().isEmpty()) {
            Role role = user.getRoles().iterator().next();
            userResponse.setRoleId(role.getId());
        }
        return userResponse;
    }

    @Override
    public void changePassword(String email, String oldPassword, String newPassword) {
        User user = getUser(email);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadCredentialsException("Mật khẩu hiện tại không đúng");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public String createAccount(CreateAccountRequest requestUser) {
        if(userRepository.existsByEmail(requestUser.getEmail())){
            throw new UserAlreadyExistsException("Email " + requestUser.getEmail() + " đã tồn tại");
        }
        if(userRepository.existsByPhoneNumber(requestUser.getPhoneNumber())){
            throw new UserAlreadyExistsException("Số điện thoại "+ requestUser.getPhoneNumber() + " đã tồn tại");
        }
        User user = new User();
        user.setEmail(requestUser.getEmail());
        user.setFullName(requestUser.getFullName());
        user.setPhoneNumber(requestUser.getPhoneNumber());

        String rawPassword = generateRandomPassword(10);
        user.setPassword(passwordEncoder.encode(rawPassword));

        Role role = roleRepository.findById(requestUser.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role không tồn tại với ID = " + requestUser.getRoleId()));
        user.setRoles(Collections.singletonList(role));

        userRepository.save(user);

        return rawPassword;
    }

    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }
}
