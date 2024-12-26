package com.example.demo.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// PasswordEncoder 클래스의 encode()와 matches() 메서드가 정상적으로 작동하는지 확인
public class PasswordEncoderTests {

    // 평문 비밀번호가 암호화된 후 null이 아닌지, 원본 비밀번호와 다르게 암호화되는지 확인
    @Test
    void testEncodeTest() {
        String rawPassword = "test123!@1";
        String encodedPassword = PasswordEncoder.encode(rawPassword);

        assertNotNull(encodedPassword, "암호화된 비밀번호는 null이 될 수 없습니다.");
        assertNotEquals(rawPassword, encodedPassword, "암호화된 비밀번호는 평문 비밀번호와 같을 수 없습니다.");
    }

    // 암호화된 비밀번호가 평문 비밀번호와 일치하는지 확인
    @Test
    void testEncodedPasswordMatchesRawPassword() {
        String rawPassword = "sdjgijstj!#$@3253";
        String encodedPassword = PasswordEncoder.encode(rawPassword);

        assertTrue(PasswordEncoder.matches(rawPassword, encodedPassword),
                "평문 비밀번호는 암호화된 비밀번호와 matches로 비교했을 때 True를 리턴해야 합니다.");
    }

    // 동일한 평문 비밀번호로 암호화된 비밀번호가 서로 다른지 확인
    @Test
    void testEncodedPasswordsAreDifferent() {
        String rawPassword = "sdjgijs_-~tj!#$@3253";
        String encodedPassword1 = PasswordEncoder.encode(rawPassword);
        String encodedPassword2 = PasswordEncoder.encode(rawPassword);

        assertNotEquals(encodedPassword1, encodedPassword2, "암호화된 비밀번호는 서로 달라야 합니다.");
    }

    // 잘못된 비밀번호로 암호화된 비밀번호와 비교했을 때 false를 리턴하는지 확인
    @Test
    void testPasswordDoesNotMatch() {
        String rawPassword = "asdfzxcv09832@";
        String wrongPassword = "SkDoClGm3425";

        String encodedPassword = PasswordEncoder.encode(rawPassword);

        assertFalse(PasswordEncoder.matches(wrongPassword, encodedPassword),
                "wrongPassword는 encodedPassword와 matches로 비교했을 때 False를 리턴해야 합니다.");
    }
}
