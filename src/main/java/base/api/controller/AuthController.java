package base.api.controller;

import base.api.base.BaseAPIController;
import base.api.config.EmailService;
import base.api.config.JwtUtil;
import base.api.dto.request.*;
import base.api.dto.response.AuthResponse;
import base.api.dto.response.TFUResponse;
import base.api.enums.UserRole;
import base.api.model.CategoryModel;
import base.api.model.user.UserModel;
import base.api.repository.IUserRepository;
import base.api.service.IUserService;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController extends BaseAPIController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("login1")
    public ResponseEntity<TFUResponse<AuthResponse>> login1(@RequestBody AuthRequest dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.getUsername(),
                            dto.getPassword()
                    )
            );
            UserModel user = userService.findByUserName(dto.getUsername());
            if (user == null) {
                return badRequest("User not found");
            }

            if (!user.isActive()) {
                return badRequest("Your account has been disabled");
            }
            String jwt = jwtUtil.generateToken(user);
            AuthResponse authResponse = new AuthResponse();
            authResponse.setAccessToken(jwt);
            authResponse.setUserName(user.getUserName());
            authResponse.setEmail(user.getEmail());
            return success(authResponse, "Login Successful");

        } catch (BadCredentialsException e) {
            return badRequest("Incorrect username or password");
        }catch (Exception e) {
            return badRequest("Login failed: " + e.getMessage());
        }


    }


    @PostMapping("login")
    public ResponseEntity<TFUResponse<AuthResponse>> login(@RequestBody AuthRequest dto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));

        UserModel user = userService.findByUserName(dto.getUsername());
        if (user == null) {
            return badRequest("Không tìm thấy user");
        }

        String jwt = jwtUtil.generateToken(user);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(jwt);
        authResponse.setUserName(user.getUserName());
        authResponse.setEmail(user.getEmail());
        return success(authResponse, "Login Successful");
    }

    // c => post
    @PostMapping("register")
    public ResponseEntity<TFUResponse<UserModel>> register(@RequestBody RegisterDto dto) {
        UserModel user = userService.registerUser(dto);
        if (user == null) {
            return badRequest("Không tạo được user");
        }
        return success(user);
    }


    @GetMapping("get-user-by-id")
    public ResponseEntity<TFUResponse<UserModel>> getUserById(@RequestParam Long id) {
        UserModel user = userService.findById(id);
        if (user == null) {
            return badRequest("Không tìm thấy user");
        }
        return success(user);
    }

}
