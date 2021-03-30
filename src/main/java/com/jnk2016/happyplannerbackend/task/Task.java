package com.jnk2016.happyplannerbackend.task;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;

@Entity
@Table(name="task")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="task_id")
    private long taskId;
    private String name;
    private int priority;   // 0 = upcoming, 1 = high, 2 = med, 3 = low
    private Boolean status = false;

    public HashMap<String, Object> toResponse(){
        HashMap<String, Object> response = new HashMap<>();
        response.put("id", this.taskId);
        response.put("name", this.name);
        response.put("priority", this.priority);
        response.put("status", this.status);

        return response;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private ApplicationUser user;
}
