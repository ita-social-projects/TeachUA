package com.softserve.teachua.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.teachua.dto.OperationResponce;
import com.softserve.teachua.dto.RoleResponce;
import com.softserve.teachua.dto.controller.SuccessLogin;
import com.softserve.teachua.dto.controller.SuccessRegistration;
import com.softserve.teachua.dto.service.UserLogin;
import com.softserve.teachua.dto.service.UserProfile;
import com.softserve.teachua.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
//@RequestMapping("/api")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    // TODO
    @RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
    public OperationResponce index() {
        return new OperationResponce("index");
    }
    
    // TODO delete
    @GetMapping("/hello")
    public OperationResponce hello() {
        return new OperationResponce("hello");
    }
    
    @PostMapping("/signup")
//    public ResponseEntity<SuccessRegistration> signUp(
//            @RequestParam(value = "email", required = true)
//            String email,
//            @RequestParam(value = "password", required = true)
//            String password,
//            @RequestParam(value = "name", required = true)
//            String name) {
    public ResponseEntity<SuccessRegistration> signUp(
    		@Valid
    		@RequestBody
    		UserProfile userProfile) {
        log.info("**/signup userLogin = " + userProfile.getEmail());
        // User profile, UserSignup
        //UserProfile userProfile = new UserProfile(email, password, name);
        //return new OperationResponce(String.valueOf(userService.saveUser(userProfile)));
        return ResponseEntity
        		.status(HttpStatus.CREATED)
        		.body(userService.registerUser(userProfile));
    }

//    @ApiOperation("Sign-in by own security logic")
//    @ApiResponses(value = {
//        @ApiResponse(code = 200, message = HttpStatuses.OK, response = SuccessSignInDto.class),
//        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
//    })
    @PostMapping("/signin")
//    public SuccessLogin signIn(
//            @RequestParam(value = "email", required = true)
//            String email,
//            @RequestParam(value = "password", required = true)
//            String password) {
    public SuccessLogin signIn(
    		@Valid
    		@RequestBody
    		UserLogin userLogin) {
        log.info("**/signin userLogin = " + userLogin.getEmail());
        // UserLogin userLogin = new UserLogin(email, password);
        return userService.validateUser(userLogin);
    }

    /*-
    // TODO move
    @GetMapping("/user/date")
    public OperationResponce expirationDate() {
        log.info("/user/date");
        return new OperationResponce("expiration date is " + userService.getExpirationLocalDate());
    }
    */
    
    // TODO move
    @GetMapping("/roles")
    public List<RoleResponce> listRoles() {
        log.info("/roles");
        return userService.getAllRoles();
    }

}
