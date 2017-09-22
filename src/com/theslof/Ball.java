package com.theslof;

import java.awt.*;

public class Ball extends Graphical {
    private int radius;
    private double x;
    private double y;
    private double vx;
    private double vy;

    public Ball(int radius, double x, double y, double vx, double vy){
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
    }

    public void update(double dt){
        //Flytta objektet baserat på hastighet multiplecerat med dt
        x += vx * dt;
        y += vy * dt;
    }

    @Override
    public void paint(Graphics2D g) {
        //Rita ut en boll med korrekt radie på koordinaterna
        //Graphics2D-objekt har metoder för att rita upp och fylla geometriska former. draw/fill
        //Innan detta så får man specifiera färg (setColor) och linjetyp (setStroke)
    }

    //Lägg till setters och getters för vx/vy/x/y/radius
}
