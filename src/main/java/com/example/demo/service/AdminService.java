package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {
    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // TODO: 4. find or save 예제 개선
    @Transactional
    public void reportUsers(List<Long> userIds) {
        // userIds 리스트에 해당하는 사용자 데이터를 한 번의 쿼리로 가져오고 한 번의 update 쿼리로 업데이트한다.
        List<User> users = userRepository.findAllById(userIds);

        if(users.isEmpty()){
            throw new IllegalArgumentException("해당 ID에 맞는 값이 존재하지 않습니다.");
        }

        userRepository.updatePendingStatus(userIds);

//        for (Long userId : userIds) {
//            User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 ID에 맞는 값이 존재하지 않습니다."));
//
//            user.updateStatusToBlocked();
//
//            userRepository.save(user);
//        }
    }
}
