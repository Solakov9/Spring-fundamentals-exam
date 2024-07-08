package com.paintingscollectors.model.entity;

import com.paintingscollectors.PaintingsCollectorsApplication;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(unique = true,nullable = false)
    private String email;
    @OneToMany(mappedBy = "owner",fetch = FetchType.EAGER)
    private Set<Painting> addedPaintings;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Painting> favouritePainting;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Painting> ratedPaintings;

    public User() {
        this.addedPaintings = new HashSet<>();
        this.favouritePainting = new HashSet<>();
        this.ratedPaintings = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Painting> getAddedPaintings() {
        return addedPaintings;
    }

    public void setAddedPaintings(Set<Painting> addedPaintings) {
        this.addedPaintings = addedPaintings;
    }

    public Set<Painting> getFavouritePainting() {
        return favouritePainting;
    }

    public void setFavouritePainting(Set<Painting> favouritePainting) {
        this.favouritePainting = favouritePainting;
    }

    public List<Painting> getRatedPaintings() {
        return ratedPaintings;
    }

    public void setRatedPaintings(List<Painting> ratedPaintings) {
        this.ratedPaintings = ratedPaintings;
    }

    public void addFavouritPainting(Painting painting) {
        this.favouritePainting.add(painting);
    }
}
