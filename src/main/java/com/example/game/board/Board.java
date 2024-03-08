package com.example.game.board;

import java.util.Date;
import java.util.List;

import com.example.game.board.cell.Cell;
import com.example.game.converter.ListConverter;
import com.example.game.player.Player;
import com.example.game.player.Player.Type;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Board {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Player player;

    private Type next;

    private Status status;

    private Cell option;

    @Convert(converter = ListConverter.class)
    private List<List<String>> lines;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Type getNext() {
        return next;
    }

    public void setNext(Type next) {
        this.next = next;
    }

    public Status getStatus() {
        return status;
    }

    public Cell getOption() {
        return option;
    }

    public void setOption(Cell option) {
        this.option = option;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<List<String>> getLines() {
        return lines;
    }

    public void setLines(List<List<String>> lines) {
        this.lines = lines;
    }


    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
}