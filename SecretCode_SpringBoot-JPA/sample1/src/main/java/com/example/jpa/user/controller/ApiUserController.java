package com.example.jpa.user.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.jpa.notice.entity.Notice;
import com.example.jpa.notice.entity.NoticeLike;
import com.example.jpa.notice.model.NoticeResponse;
import com.example.jpa.notice.model.ResponseError;
import com.example.jpa.notice.repository.NoticeLikeRepository;
import com.example.jpa.notice.repository.NoticeRepository;
import com.example.jpa.user.entity.User;
import com.example.jpa.user.exception.ExistsEmailException;
import com.example.jpa.user.exception.PasswordNotMatchException;
import com.example.jpa.user.exception.UserNotFoundException;
import com.example.jpa.user.model.*;
import com.example.jpa.user.repository.UserRepository;
import com.example.jpa.util.PasswordUtils;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import sun.security.util.Password;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ApiUserController {

    private final UserRepository userRepository;
    private final NoticeRepository noticeRepository;
    private final NoticeLikeRepository noticeLikeRepository;
/*    @PostMapping("/api/user")
    public ResponseEntity<?> addUser(@RequestBody @Valid UserInput userInput, Errors errors){

        List<ResponseError> responseErrorList = new ArrayList<>();

        if(errors.hasErrors()){
            errors.getAllErrors().forEach((e) ->
                    responseErrorList.add(ResponseError.of((FieldError) e)));
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().build();
    }*/

    /*
    @PostMapping("/api/user")
    public ResponseEntity<?> addUser(@RequestBody @Valid UserInput userInput, Errors errors){

        List<ResponseError> responseErrorList = new ArrayList<>();

        if(errors.hasErrors()){
            errors.getAllErrors().forEach((e) ->
                    responseErrorList.add(ResponseError.of((FieldError) e)));
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        User user = User.builder()
                .email(userInput.getEmail())
                .userName(userInput.getUserName())
                .password(userInput.getPassword())
                .phone(userInput.getPhone())
                .regDate(LocalDateTime.now())
                .build();
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }
    */

    @PutMapping("/api/user/{id}")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserUpdate userUpdate, @PathVariable long id, Errors errors){

        List<ResponseError> responseErrorList = new ArrayList<>();

        if(errors.hasErrors()){
            errors.getAllErrors().forEach((e) ->
                    responseErrorList.add(ResponseError.of((FieldError) e)));
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("????????? ????????? ????????????."));
        user.setPhone(userUpdate.getPhone());
        user.setUpdateDate(LocalDateTime.now());

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> UserNotFoundExceptionHandler(UserNotFoundException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/api/user/{id}")
    public UserResponse getUser(@PathVariable long id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("????????? ????????? ????????????."));

        // UserResponse userResponse = new UserResponse(user); //???????????? ????????????

        UserResponse userResponse = UserResponse.of(user); // of ????????? ????????? builder??? ????????????

        return userResponse;

    }

    @GetMapping("/api/user/{id}/notice")
    public List<NoticeResponse> userNotice(@PathVariable long id){
        User user = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("????????? ????????? ????????????."));

        List<Notice> noticeList = noticeRepository.findByUser(user);

        List<NoticeResponse> noticeResponses = new ArrayList<>();
        noticeList.stream().forEach((e) ->
                noticeResponses.add(NoticeResponse.of(e)));


        return noticeResponses;
    }
    /*
    @PostMapping("/api/user")
    public ResponseEntity addUser(@RequestBody @Valid UserInput userInput, Errors errors){

        List<ResponseError> responseErrorList = new ArrayList<>();
        if(errors.hasErrors()){
            errors.getAllErrors().stream().forEach((e) ->
                    responseErrorList.add(ResponseError.of((FieldError)e))
                    );
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        if(userRepository.countByEmail(userInput.getEmail()) > 0) {
            throw new ExistsEmailException("?????? ????????? ???????????? ???????????????.");
        };

        User user = User.builder()
                .email(userInput.getEmail())
                .userName(userInput.getUserName())
                .phone(userInput.getPhone())
                .password(userInput.getPassword())
                .regDate(LocalDateTime.now())
                .build();
        userRepository.save(user);

        return ResponseEntity.ok().build();

    }
    */
    @ExceptionHandler(value = {ExistsEmailException.class, PasswordNotMatchException.class})
    public ResponseEntity ExistsEmailExceptionHandler(RuntimeException e){
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @PatchMapping("/api/user/{id}/password")
    public ResponseEntity updateUserPassword(@PathVariable Long id, @RequestBody UserInputPassword userInputPassword, Errors errors){

        List<ResponseError> responseErrorList = new ArrayList<>();
        if(errors.hasErrors()){
            errors.getAllErrors().stream().forEach((e) ->
                    responseErrorList.add(ResponseError.of((FieldError)e))
            );
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByIdAndPassword(id, userInputPassword.getPassword())
                .orElseThrow(()-> new PasswordNotMatchException("??????????????? ???????????? ????????????."));

        user.setPassword(userInputPassword.getPassword());
        userRepository.save(user);

        return ResponseEntity.ok().build();

    }

    private String getEncryptPassword(String password){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }


    @PostMapping("/api/user")
    public ResponseEntity addUser(@RequestBody @Valid UserInput userInput, Errors errors){

        List<ResponseError> responseErrorList = new ArrayList<>();
        if(errors.hasErrors()){
            errors.getAllErrors().stream().forEach((e) ->
                    responseErrorList.add(ResponseError.of((FieldError)e))
            );
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        if(userRepository.countByEmail(userInput.getEmail()) > 0) {
            throw new ExistsEmailException("?????? ????????? ???????????? ???????????????.");
        };



        User user = User.builder()
                .email(userInput.getEmail())
                .userName(userInput.getUserName())
                .phone(userInput.getPhone())
                .password(getEncryptPassword(userInput.getPassword()))
                .regDate(LocalDateTime.now())
                .build();
        userRepository.save(user);

        return ResponseEntity.ok().build();

    }


    @DeleteMapping("/api/user/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id){
        User user = userRepository.findById(id).orElseThrow(
                ()->new UserNotFoundException("????????? ????????? ????????????."));

        // ?????? ??? ??????????????? ?????? ??????
        // -> ??
        try {
            userRepository.delete(user);
        }catch (DataIntegrityViolationException e){
            String message = "??????????????? ????????? ?????????????????????.";
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            String message = "?????? ?????? ??? ????????? ?????????????????????.";
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().build();
    }

    private String getResetPassword(){
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
    }


    @GetMapping("/api/user")
    public ResponseEntity findUser(@RequestBody UserInputFind userInputFind){

        User user = userRepository.findByUserNameAndPhone(userInputFind.getUserName(), userInputFind.getPhone())
                .orElseThrow(()-> new UserNotFoundException("????????? ????????? ????????????."));

        UserResponse userResponse = UserResponse.of(user);

        return ResponseEntity.ok().body(userResponse);
    }



    @GetMapping("/api/user/{id}/password/reset")
    public ResponseEntity resetUserPassword(@PathVariable long id ){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("????????? ????????? ????????????."));

        // ???????????? ?????????
        String resetPassword = getResetPassword();
        String resetEncryptPassword = getEncryptPassword(getResetPassword());
        user.setPassword(resetEncryptPassword);

        userRepository.save(user);

        String message = String.format("[%s]?????? ?????? ??????????????? [%s]??? ????????? ???????????????. "
                , user.getUserName()
                ,resetPassword);

        sendSNS(message);

        return ResponseEntity.ok().build();

    }


    void sendSNS(String message){
        System.out.println("[?????????????????????]");
        System.out.println(message);
    }


    // ?????? ???????????? ??????????????? ?????? API??? ????????? ?????????.
    @GetMapping("/api/user/{id}/notice/like")
    public List<NoticeLike> likeNotice(@PathVariable Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("????????? ????????? ????????????."));

        List<NoticeLike> noticeLikeList = noticeLikeRepository.findByUser(user);

        return noticeLikeList;
     }

    /*
    // ???????????? ???????????? ??????????????? ????????? JWT??? ???????????? API??? ????????? ?????????.
    @PostMapping("/api/user/login")
    public ResponseEntity createToken(@RequestBody @Valid UserLogin userLogin, Errors errors){

        List<ResponseError> responseErrorList = new ArrayList<>();
        if(errors.hasErrors()) {
            errors.getAllErrors().stream().forEach((e) ->
                    responseErrorList.add(ResponseError.of((FieldError) e))
            );
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmail(userLogin.getEmail())
                .orElseThrow(()-> new UserNotFoundException("????????? ????????? ????????????."));

        if(!PasswordUtils.equalPassword(userLogin.getPassword(), user.getPassword())){
            throw new PasswordNotMatchException("??????????????? ???????????? ????????????.");
        }

        return ResponseEntity.ok().build();
    }
    */

    /*
    // ???????????? ???????????? ??????????????? ????????? JWT??? ???????????? API??? ????????? ?????????.
    @PostMapping("/api/user/login")
    public ResponseEntity createToken(@RequestBody @Valid UserLogin userLogin, Errors errors){

        List<ResponseError> responseErrorList = new ArrayList<>();
        if(errors.hasErrors()) {
            errors.getAllErrors().stream().forEach((e) ->
                    responseErrorList.add(ResponseError.of((FieldError) e))
            );
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmail(userLogin.getEmail())
                .orElseThrow(()-> new UserNotFoundException("????????? ????????? ????????????."));

        if(!PasswordUtils.equalPassword(userLogin.getPassword(), user.getPassword())){
            throw new PasswordNotMatchException("??????????????? ???????????? ????????????.");
        }

        // ?????? ?????? ??????
        String token = JWT.create()
                .withExpiresAt(new Date())
                .withClaim("user_id", user.getId())
                .withSubject(user.getUserName())
                .withIssuer(user.getEmail())
                .sign(Algorithm.HMAC512("fastcampus".getBytes()));

        UserLoginToken.builder().token(token).build();

        return ResponseEntity.ok().body(UserLoginToken.builder().token(token).build());
    }
    */


    //  JWT?????? ????????? ??????????????? 1????????? ???????????? API??? ????????? ?????????
    @PostMapping("/api/user/login")
    public ResponseEntity createToken(@RequestBody @Valid UserLogin userLogin, Errors errors){

        List<ResponseError> responseErrorList = new ArrayList<>();
        if(errors.hasErrors()) {
            errors.getAllErrors().stream().forEach((e) ->
                    responseErrorList.add(ResponseError.of((FieldError) e))
            );
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmail(userLogin.getEmail())
                .orElseThrow(()-> new UserNotFoundException("????????? ????????? ????????????."));

        if(!PasswordUtils.equalPassword(userLogin.getPassword(), user.getPassword())){
            throw new PasswordNotMatchException("??????????????? ???????????? ????????????.");
        }

        LocalDateTime expiredDateTime = LocalDateTime.now().plusMonths(1);
        Date expiredDate = java.sql.Timestamp.valueOf(expiredDateTime);

        // ?????? ?????? ??????
        String token = JWT.create()
                .withExpiresAt(expiredDate)
                .withClaim("user_id", user.getId())
                .withSubject(user.getUserName())
                .withIssuer(user.getEmail())
                .sign(Algorithm.HMAC512("fastcampus".getBytes()));

        UserLoginToken.builder().token(token).build();

        return ResponseEntity.ok().body(UserLoginToken.builder().token(token).build());
    }





}
