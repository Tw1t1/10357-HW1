package com.example.hw1.Model;

import com.example.hw1.Logic.GameManager;
import com.example.hw1.R;

public class RoadCell {

    private GameManager.RoadCellTypes type;
    private int image;

    public RoadCell() {
        this.type = GameManager.RoadCellTypes.EMPTY;
        updateImage();
    }

    public GameManager.RoadCellTypes getType() {
        return type;
    }

    /**
     * Set the road cell type
     * <br>And update the road cell image base on the new type</br>
     * @param type base on the RoadCellTypes enum
     */
    public void setType(GameManager.RoadCellTypes type) {
        this.type = type;
        updateImage();
    }


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    /**
     * Update the road cell base on the type
     */
    public void updateImage() {
        switch (type){
            case CAR:
                image = R.drawable.car;
                break;
            case ROCK:
                image = R.drawable.rock;
                break;
            case EMPTY:
            default:
                image = 0;
        }
    }
}
