package com.jnk2016.happyplannerbackend.recipe;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="recipe")
@Data
public class Recipe implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="recipe_id")
    private long recipeId;
    private String title;
    private String type;
    private long preptime;
    private long cooktime;

    @Column(columnDefinition = "TEXT")
    private String preparation;
    @Column(columnDefinition = "TEXT")
    private String ingredients;
    @Column(columnDefinition = "TEXT")
    private String notes;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private ApplicationUser user;
}
