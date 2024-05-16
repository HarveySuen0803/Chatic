package com.harvey.user;

import com.harvey.result.Result;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author harvey
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @PostMapping("/login")
    public Result login(@RequestBody User user, HttpSession httpSession) {
        if ("admin".equals(user.getUsername()) && "111".equals(user.getPassword())) {
            httpSession.setAttribute("username", user.getUsername());
            return Result.success();
        } else {
            return Result.failure("username or password error");
        }
    }
}
