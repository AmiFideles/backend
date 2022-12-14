package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "hits")
@Getter
@Setter
// TODO проблемы с циклическими зависимостями?
@NoArgsConstructor
public class Hit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private double xValue;
    @Column(nullable = false)
    private double yValue;
    @Column(nullable = false)
    private double rValue;
    @Column(nullable = false)
    private boolean result;
    @Column(nullable = false)
    private LocalDateTime dateTime;
    @Column(nullable = false)
    private Long executionTime;
    //TODO посмотреть сюда
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    public void prePersist(){
        executionTime =  System.nanoTime()-executionTime;
    }

    @Override
    public String toString() {
        return "Hit{" +
                "id=" + id +
                ", xValue=" + xValue +
                ", yValue=" + yValue +
                ", rValue=" + rValue +
                ", result=" + result +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hit hit = (Hit) o;
        return id == hit.id && Double.compare(hit.xValue, xValue) == 0 && Double.compare(hit.yValue, yValue) == 0 && Double.compare(hit.rValue, rValue) == 0 && result == hit.result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, xValue, yValue, rValue, result);
    }
}
