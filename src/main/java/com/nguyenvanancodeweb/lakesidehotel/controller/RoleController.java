package com.nguyenvanancodeweb.lakesidehotel.controller;

import com.nguyenvanancodeweb.lakesidehotel.exception.RoleAlreadyExistException;
import com.nguyenvanancodeweb.lakesidehotel.model.Role;
import com.nguyenvanancodeweb.lakesidehotel.model.User;
import com.nguyenvanancodeweb.lakesidehotel.response.DTO.ApiResponseDTO;
import com.nguyenvanancodeweb.lakesidehotel.response.DTO.DataResponseDTO;
import com.nguyenvanancodeweb.lakesidehotel.response.RoleDTO;
import com.nguyenvanancodeweb.lakesidehotel.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.FOUND;

//@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    private final IRoleService roleService;

    @GetMapping("/all-roles")
    public ResponseEntity<ApiResponseDTO<List<RoleDTO>>> getAllRoles() {
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "200",
                new DataResponseDTO<>(null, roleService.getRoles()))); // 200 OK mặc định
    }

    @PostMapping("/create-new-role")
    public ResponseEntity<String> createRole(@RequestBody Role theRole){
        try{
            roleService.createRole(theRole);
            return ResponseEntity.ok("New role created successfully!");
        }catch(RoleAlreadyExistException re){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(re.getMessage());

        }
    }
    @DeleteMapping("/delete/{roleId}")
    public void deleteRole(@PathVariable("roleId") Long roleId){
        roleService.deleteRole(roleId);
    }
    @PostMapping("/remove-all-users-from-role/{roleId}")
    public Role removeAllUsersFromRole(@PathVariable("roleId") Long roleId){
        return roleService.removeAllUsersFromRole(roleId);
    }

    @PostMapping("/remove-user-from-role")
    public User removeUserFromRole(
            @RequestParam("userId") Long userId,
            @RequestParam("roleId") Long roleId){
        return roleService.removeUserFromRole(userId, roleId);
    }
    @PostMapping("/assign-user-to-role")
    public User assignUserToRole(
            @RequestParam("userId") Long userId,
            @RequestParam("roleId") Long roleId){
        return roleService.assignRoleToUser(userId, roleId);
    }
}
