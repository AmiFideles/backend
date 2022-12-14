package com.example.demo.service;


import org.springframework.stereotype.Component;

@Component
public class HitChecker {
    public boolean check(double x, double y, double r){
        return checkHitInCircle(x,y,r) || checkHitInTriangle(x,y,r) || checkHitInRectangle(x,y,r);
    }

    private boolean checkHitInTriangle(double x, double y, double r){
        return x<=0 && y>=0 && y<=r+x;
    }

    private boolean checkHitInRectangle (double x, double y, double r){
        return x >= 0 && y <= 0 && x <= r/2 && y <= r;
    }

    private boolean checkHitInCircle(double x, double y, double r){
        return x >= 0 && y >= 0 && x<=1.5 && y<=1.5 && x*x+y*y<=r*r/4;
    }
}
