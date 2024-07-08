package com.paintingscollectors.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "styles")
public class Style {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    @Column(unique = true,nullable = false)
    private StyleName name;
    @Column(nullable = false)
    private String description;

    public Style() {
    }

    public Style(StyleName name, String description) {
        this.name = name;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public StyleName getName() {
        return name;
    }

    public void setName(StyleName name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
