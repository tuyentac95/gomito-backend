package com.gomito.Gomitobackend.controller;

import com.gomito.Gomitobackend.dto.ChangePasswordRequest;
import com.gomito.Gomitobackend.model.GBoard;
import com.gomito.Gomitobackend.model.GUser;
import com.gomito.Gomitobackend.dto.GUserDto;
import com.gomito.Gomitobackend.service.AuthService;
import com.gomito.Gomitobackend.service.GBoardService;
import com.gomito.Gomitobackend.service.GUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private GBoardService gBoardService;

    @Autowired
    private AuthService authService;

    @Autowired
    private GUserService gUserService;

    @GetMapping("/{id}")
    public ResponseEntity<List<GBoard>> findAllBoardByUserId(@PathVariable Long id) {
        GUser user = authService.getCurrentUser();
        if (user.getUserId().equals(id)) {
            List<GBoard> gboards = gBoardService.findAllBoardByUserId(id);
            return ResponseEntity.status(HttpStatus.OK).body(gboards);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        if(authService.changePassword(request)) {
            return ResponseEntity.status(HttpStatus.OK).body("Password change successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not allow to change password");
        }
    }

    @GetMapping("")
    public ResponseEntity<GUserDto> getUserInfo() {
        GUser user = authService.getCurrentUser();
        GUserDto responseUser = new GUserDto();
        responseUser.setUserId(user.getUserId());
        responseUser.setUsername(user.getUsername());
        responseUser.setEmail(user.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }

    @GetMapping("/join/{token}")
    public ResponseEntity<String> joinBoard(@PathVariable String token) {
        System.out.println("check token: " + token);
        if (gUserService.verifyToken(token)) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body("You have been added to the table. Start working now!\n" +
                            "Login here: http://localhost:4200/login");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong token");
        }
    }

}
