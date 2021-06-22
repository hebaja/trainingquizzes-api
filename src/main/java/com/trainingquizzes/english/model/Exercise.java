package com.trainingquizzes.english.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.trainingquizzes.english.enums.LevelType;

@Entity
public class Exercise {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
	@ManyToOne
    private User user;

    private String subject;
    
    @Enumerated(EnumType.STRING)
    private LevelType level;

    private double score;

    public Exercise(User user, String subject, LevelType level, double score) {
        this.user = user;
        this.subject = subject;
        this.level = level;
        this.score = score;
    }

    @Deprecated
    public Exercise() {}

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getSubject() {
        return subject;
    }

    public LevelType getLevel() {
        return level;
    }

    public double getScore() { return score; }

}
