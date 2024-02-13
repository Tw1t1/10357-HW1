package com.example.hw1.Logic;

import android.util.Log;

import com.example.hw1.Model.RoadCell;

import java.util.Random;

public class GameManager {

    public enum RoadCellTypes {CAR, EMPTY, ROCK}

    public enum Directions {LEFT, RIGHT}

    public enum GameStatus {OK, CRASHED, BLOCKED, GAME_OVER};
    public static final long DELAY = 1000;
    private final int maxLife;
    private int life;
    private int rows;
    private int lanes;
    private RoadCell[][] road;
    private int carLane;

    public GameManager() {
        this.maxLife = 3;
        this.life = maxLife;
        this.rows = 7;
        this.lanes = 3;
        road = createRoad();
    }

    public GameManager(int life, int rows, int lanes) {
        this.maxLife = life;
        this.life = maxLife;
        this.rows = rows;
        this.lanes = lanes;
        road = createRoad();
        centerCar();
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getLanes() {
        return lanes;
    }

    public void setLanes(int lanes) {
        this.lanes = lanes;
    }

    public RoadCell[][] getRoad() {
        return road;
    }

    public void setRoad(RoadCell[][] road) {
        this.road = road;
    }

    public int getCarLane() {
        return carLane;
    }

    public void setCarLane(int carLane) {
        this.carLane = carLane;
    }

    public int getMaxLife() {
        return maxLife;
    }


    private RoadCell[][] createRoad() {
        if (rows * lanes > 0) {
            RoadCell[][] newRoad = new RoadCell[rows][lanes];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < lanes; j++)
                    newRoad[i][j] = new RoadCell();
            }
            return newRoad;
        } else throw new RuntimeException("Road rows and lanes must have positive value");
    }

    private void centerCar() {
        this.carLane = lanes / 2;
        this.road[rows - 1][carLane].setType(RoadCellTypes.CAR);
    }

    private GameStatus collision() {
        // in case the car hits a rock - subtract one life.
        life--;
        return life == 0 ? GameStatus.GAME_OVER : GameStatus.CRASHED;
    }

    public void reset() {
        // clear the road
        for (int r = this.rows - 1; r >= 0; r--) {   // run through all the rows from bottom to top
            for (int l = 0; l < this.lanes; l++)  // run on all the lanes from left to right
                this.road[r][l].setType(RoadCellTypes.EMPTY);   // clear the cell
        }
        centerCar();    // place the car
        life = maxLife; // reset the life to max
    }

    public GameStatus moveCar(Directions direction) {
        GameStatus status = GameStatus.OK;
        switch (direction) {
            case LEFT:
                if (carLane > 0) {  // the car isn't at the left edge of the road
                    if (road[rows - 1][carLane - 1].getType().equals(RoadCellTypes.ROCK))   // there is a rock in the lane to the left
                        status = collision();
                    this.road[rows - 1][carLane - 1].setType(RoadCellTypes.CAR);    // move the car to the left lane
                    this.road[rows - 1][carLane].setType(RoadCellTypes.EMPTY);  // clear the previous cell
                    carLane--;  // update the care lane
                }
                else
                    status = GameStatus.BLOCKED;
                break;
            case RIGHT:
                if (carLane < (lanes - 1)) {  // the car isn't at the right edge of the road
                    if (road[rows - 1][carLane + 1].getType().equals(RoadCellTypes.ROCK))   // there is a rock in the lane to the right
                        status = collision();
                    this.road[rows - 1][carLane + 1].setType(RoadCellTypes.CAR);  // move the car to the right lane
                    this.road[rows - 1][carLane].setType(RoadCellTypes.EMPTY);  // clear the previous cell
                    carLane++;  // update the care lane
                }
                else
                    status = GameStatus.BLOCKED;
                break;
            default:
                break;
        }
        return status;
    }

    public GameStatus moveRoad() {
        GameStatus status = GameStatus.OK;
        int rockCounter = 0;
        for (int r = this.rows - 1; r >= 0; r--) {   // run through all the rows from bottom to top
            for (int l = 0; l < this.lanes; l++) {   // run on all the lanes from left to right
                if (road[r][l].getType().equals(RoadCellTypes.ROCK)) { // it's a rock
                    if (r < this.rows - 1) { // not the last row
                        if (road[r + 1][l].getType().equals(RoadCellTypes.CAR)) // the car is in the cell below
                            status = collision();   // crashed
                        else    // the cell below is empty
                            road[r + 1][l].setType(RoadCellTypes.ROCK); // move the rock to the cell below
                    }
                    road[r][l].setType(RoadCellTypes.EMPTY);    // clear the current cell
                }
                if (r == 0) {   // the first row
                    if (rockCounter < 2 && new Random().nextInt(10) == 0) {  // can't have 3 rocks in a row & random 10% chance for rock to spawn
                        road[r][l].setType(RoadCellTypes.ROCK);
                        rockCounter++;
                    }
                }
            }
        }
        return status;
    }
}
