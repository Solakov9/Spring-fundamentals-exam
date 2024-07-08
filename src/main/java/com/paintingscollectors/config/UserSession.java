package com.paintingscollectors.config;

import com.paintingscollectors.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@SessionScope
@Component
public class UserSession {
    private long id;

    private String username;

    private UserRepository userRepository;

    public UserSession(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void login(long id, String username) {
        this.id = id;
        this.username = username;
    }
    public boolean isLoggedIn(){
        return id != 0;
    }
    public String username(){
        return username;
    }

    public void logout() {
        this.id = 0;
        this.username = "";
    }

    public long byId() {
        return id;
    }
}
