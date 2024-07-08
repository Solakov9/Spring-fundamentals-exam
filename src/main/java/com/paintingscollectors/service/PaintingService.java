package com.paintingscollectors.service;

import com.paintingscollectors.config.UserSession;
import com.paintingscollectors.model.dto.AddPaintingDTO;
import com.paintingscollectors.model.entity.Painting;
import com.paintingscollectors.model.entity.StyleName;
import com.paintingscollectors.model.entity.User;
import com.paintingscollectors.repository.PaintingRepository;
import com.paintingscollectors.repository.StyleRepository;
import com.paintingscollectors.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaintingService {
    private UserSession userSession;
    private UserRepository userRepository;
    private StyleRepository styleRepository;
    private PaintingRepository paintingRepository;

    public PaintingService(UserSession userSession, UserRepository userRepository, StyleRepository styleRepository, PaintingRepository paintingRepository) {
        this.userSession = userSession;
        this.userRepository = userRepository;
        this.styleRepository = styleRepository;
        this.paintingRepository = paintingRepository;
    }

    public boolean addPainting(AddPaintingDTO data){
        if (!userSession.isLoggedIn()){
            return false;
        }
        Optional<User> user = userRepository.findByUsername(userSession.username());
        Painting painting = new Painting();
        painting.setName(data.getName());
        painting.setAuthor(data.getAuthor());
        painting.setImageUrl(data.getImageUrl());
        painting.setStyle(styleRepository.findByName(StyleName.valueOf(data.getStyle())));
        painting.setOwner(user.orElse(null));
        painting.setFavourite(false);
        painting.setVotes(0);
        paintingRepository.save(painting);
        return true;
    }
    public void addToFavourite(Long id,long paintingId){
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()){
            return;
        }
        Optional<Painting> paintingOpt = paintingRepository.findById(paintingId);
        if (paintingOpt.isEmpty()){
            return;
        }
        userOpt.get().addFavouritPainting(paintingOpt.get());
        paintingOpt.get().setFavourite(true);
        paintingRepository.save(paintingOpt.get());
        userRepository.save(userOpt.get());
    }

    public void delete(long id) {
        Optional<User> user = userRepository.findById(userSession.byId());
        if (user.isEmpty()) {
            return;
        }
        Optional<Painting> maybePainting = paintingRepository.findByIdAndOwner(id, user.get());
        if (maybePainting.isEmpty()){
            return;
        }
        if (!maybePainting.get().isFavourite()){
            paintingRepository.delete(maybePainting.get());
        }
    }
    public List<Painting> getTopTwoPaintings() {
        return paintingRepository.findAll()
                .stream()
                .sorted((p1, p2) -> Integer.compare(p2.getVotes(), p1.getVotes()))
                .limit(2)
                .collect(Collectors.toList());
    }

}
