package com.jnk2016.happyplannerbackend.groceryitem;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="groceries")
@Data
public class GroceryItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="grocery_id")
    private long groceryId;
    private String name;
    private int type;   // 0=produce, 1=Meat&Fish, 2=Pantry, 3=Frozen, 4=Drinks, 5=Bakery

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private ApplicationUser user;
}
