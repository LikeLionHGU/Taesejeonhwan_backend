package org.example.taesejeanhwan_backend.service;

import org.example.taesejeanhwan_backend.domain.User;
import org.example.taesejeanhwan_backend.dto.user.request.*;
import org.example.taesejeanhwan_backend.dto.user.response.*;
//import org.example.taesejeanhwan_backend.repository.ProfileImgRepository;
import org.example.taesejeanhwan_backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

//    private final ProfileImgRepository profileImgRepository;
    private final UserRepository userRepository;

    //img_url 을 받아와 user_id에 맞는 유저에 url을 저장하고 성공하면 success 응답
    public UserResponseResult setProfileImage(UserRequestSetImage userRequestSetImage) {
        User user = userRepository.findById(userRequestSetImage.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        user.builder()
                .profile_img(userRequestSetImage.getProfile_img())
                .build();
        userRepository.save(user);

        String resultReturn;
        if(userRepository.existsByIdAndProfileImg(userRequestSetImage.getUserId(), userRequestSetImage.getProfile_img())) {
            resultReturn = "success";
        }
        else {
            resultReturn = "fail";
        }
        return UserResponseResult.builder()
                .result(resultReturn)
                .build();
    }

    //닉네임 설정
    public UserResponseResult setNickname(UserRequestSetNickname userRequestSetNickname) {
        User user = userRepository.findById(userRequestSetNickname.getUser_id()).orElseThrow(() -> new RuntimeException("User not found"));
        user.builder()
                .nickname(userRequestSetNickname.getNickname())
                .build();
        userRepository.save(user);

        String resultReturn;
        if(userRepository.existsByIdAndNickname(userRequestSetNickname.getNickname(), userRequestSetNickname.getUser_id())) {
            resultReturn = "success";
        }
        else {
            resultReturn = "fail";
        }
        return UserResponseResult.builder()
                .result(resultReturn)
                .build();
    }

    //10개 content_id로 초기 table 생성
    public UserResponseSetPreference setPreference(UserRequestSetPreference userRequestSetPreference) {
        //컨텐츠 아이디를 통해 컨텐츠 장르를 가져와 리스트업
    };

}