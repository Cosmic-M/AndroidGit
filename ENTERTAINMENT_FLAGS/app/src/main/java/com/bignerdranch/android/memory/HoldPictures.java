/*
 *5.03.2017
 */

package com.bignerdranch.android.memory;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HoldPictures {
    private static HoldPictures sPictures;
    private  List<Picture> mPictures;

    private HoldPictures(Context context, int quantityPics){
        int idOfDrawable = 0;
        String country = "";
        mPictures = new ArrayList<>();
        List<Integer> stencil = new ArrayList<>(100);
        List<Integer> randomOrder = new ArrayList<>(100);

        for (int i = 0; i < quantityPics / 2; i++)
            for (int j = 0; j < 2; j++) {
                stencil.add(i);
            }

        for (int i = 0; i < quantityPics; i++) {
            Random random = new Random();
            int randomNumber = random.nextInt(quantityPics / 2 + 1);
            if (stencil.contains(randomNumber)){
                randomOrder.add(randomNumber);
                stencil.remove((Object) randomNumber);
            }
            else{
                i--;
            }
        }

        for (int i = 0; i < quantityPics; i++){
            int n = randomOrder.get(i);
            switch (n){
                case 0:
                    idOfDrawable = R.drawable.albania_flag;
                    country = "ALBANIA";
                    break;
                case 1:
                    idOfDrawable = R.drawable.italy_flag;
                    country = "ITALY";
                    break;
                case 2:
                    idOfDrawable = R.drawable.ukrainian_flag;
                    country = "UKRAINE";
                    break;
                case 3:
                    idOfDrawable = R.drawable.france_flag;
                    country = "FRANCE";
                    break;
                case 4:
                    idOfDrawable = R.drawable.latvia_flag;
                    country = "LATVIA";
                    break;
                case 5:
                    idOfDrawable = R.drawable.canada_flag;
                    country = "CANADA";
                    break;
                case 6:
                    idOfDrawable = R.drawable.columbian_flag;
                    country = "COLUMBIA";
                    break;
                case 7:
                    idOfDrawable = R.drawable.japanese_flag;
                    country = "JAPANESE";
                    break;
                case 8:
                    idOfDrawable = R.drawable.russian_flag;
                    country = "RUSSIA";
                    break;
                case 9:
                    idOfDrawable = R.drawable.austria_flag;
                    country = "AUSTRIA";
                    break;
                case 10:
                    idOfDrawable = R.drawable.kuwait_flag;
                    country = "KUWAIT";
                    break;
                case 11:
                    idOfDrawable = R.drawable.qatar_flag;
                    country = "QATAR";
                    break;
                case 12:
                    idOfDrawable = R.drawable.argentina_flag;
                    country = "ARGENTINA";
                    break;
                case 13:
                    idOfDrawable = R.drawable.belgium_flag;
                    country = "BELGIUM";
                    break;
                case 14:
                    idOfDrawable = R.drawable.brazil_flag;
                    country = "BRAZIL";
                    break;
                case 15:
                    idOfDrawable = R.drawable.poland_flag;
                    country = "POLAND";
                    break;
                case 16:
                    idOfDrawable = R.drawable.somalia_flag;
                    country = "SOMALI";
                    break;
                case 17:
                    idOfDrawable = R.drawable.jamaica_flag;
                    country = "JAMAICA";
                    break;
                case 18:
                    idOfDrawable = R.drawable.algeria_flag;
                    country = "ALGERIA";
                    break;
                case 19:
                    idOfDrawable = R.drawable.indian_flag;
                    country = "INDIA";
                    break;
                case 20:
                    idOfDrawable = R.drawable.south_korea_flag;
                    country = "SOUTH KOREA";
                break;
            }
            Picture picture = new Picture(idOfDrawable, country);
            mPictures.add(picture);
        }//for (int i = 0; i < 60; i++)
    }

    public static HoldPictures getHoldPictures(Context context, int quantityPics) {
        if (null == sPictures) {
            sPictures = new HoldPictures(context, quantityPics);
        }
        return sPictures;
    }

    public List<Picture> getListPictures(){
        return mPictures;
    }

    public static HoldPictures setNewListPictures(Context context, int quantityPics) {
            sPictures = new HoldPictures(context, quantityPics);
        return sPictures;
    }
}
