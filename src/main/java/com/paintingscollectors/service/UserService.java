package com.paintingscollectors.service;

import com.paintingscollectors.config.UserSession;
import com.paintingscollectors.model.dto.UserLoginDTO;
import com.paintingscollectors.model.dto.UserRegisterDTO;
import com.paintingscollectors.model.entity.Painting;
import com.paintingscollectors.model.entity.User;
import com.paintingscollectors.repository.PaintingRepository;
import com.paintingscollectors.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserSession userSession;
    private PaintingRepository paintingRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserSession userSession, PaintingRepository paintingRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userSession = userSession;
        this.paintingRepository = paintingRepository;
    }
    public boolean register(UserRegisterDTO data) {
        Optional<User> byUsername = userRepository.findByUsernameAndEmail(data.getUsername(), data.getEmail());
        if (byUsername.isPresent()){
            return false;
        }
        User user = new User();
        user.setUsername(data.getUsername());
        user.setEmail(data.getEmail());
        user.setPassword(passwordEncoder.encode(data.getPassword()));
        userRepository.save(user);
        return true;
    }

    public boolean login(UserLoginDTO data) {
        Optional<User> byUsername = userRepository.findByUsername(data.getUsername());

        if (byUsername.isEmpty()){
            return false;
        }
        boolean passwordMatch = passwordEncoder.matches(data.getPassword(), byUsername.get().getPassword());
        if (!passwordMatch){
            return false;
        }
        userSession.login(byUsername.get().getId(),data.getUsername());
        return true;
    }
    public Set<Painting> getAddedPaintings(){
        Optional<User> user = userRepository.findById(userSession.byId());
        if (user.isEmpty()){
            return new HashSet<>();
        }
        return user.get().getAddedPaintings();
    }

    public Set<Painting> getOtherUsersPaintings() {
        Set<Painting> paintings = new HashSet<>();
        List<User> users = userRepository.findAll();
        for (User u : users) {
            if (u.getId() != userSession.byId()){
                paintings.addAll(u.getAddedPaintings());
            }
        }
        return paintings;
    }

    public Set<Painting> getFavouritePaintings() {
        Optional<User> user = userRepository.findById(userSession.byId());
        if (user.isEmpty()){
            return new HashSet<>();
        }
        return user.get().getFavouritePainting();
    }

    public void addToVoted(long id, long paintingId) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()){
            return;
        }
        Optional<Painting> paintingOpt = paintingRepository.findById(paintingId);
        if (paintingOpt.isEmpty()){
            return;
        }
        User user = userOpt.get();
        List<Long> paintingIds = user.getRatedPaintings().stream().map(Painting::getId).toList();

        if (paintingIds.contains(paintingId)){
            return;
        }
        user.addRatedPainting(paintingOpt.get());
        paintingOpt.get().setVotes(paintingOpt.get().getVotes() + 1);
        paintingRepository.save(paintingOpt.get());
        userRepository.save(user);
    }
}
