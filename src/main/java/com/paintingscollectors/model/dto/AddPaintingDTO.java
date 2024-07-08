package com.paintingscollectors.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AddPaintingDTO {
    @NotBlank
    @Size(min = 5, max = 40, message = "Name must be between 5 and 40 symbols.")
    private String name;
    @NotBlank
    @Size(min = 5, max = 30, message = "Author must be between 5 and 30 symbols.")
    private String author;
    @NotBlank
    @Size(max = 150, message = "ImageUrl must be no more than 150 symbols.")
    private String imageUrl;
    @NotBlank(message = "Need to select option!")
    private String style;

    public AddPaintingDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
