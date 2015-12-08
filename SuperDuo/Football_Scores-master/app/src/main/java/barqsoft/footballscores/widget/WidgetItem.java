package barqsoft.footballscores.widget;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by andermaco on 23/11/15.
 */
public class WidgetItem {

    ImageView imageViewHome;
    TextView textViewHome;

    TextView textViewScore;
    TextView textViewData;

    ImageView imageViewAway;
    TextView textViewAway;


    public WidgetItem(ImageView imageViewHome, TextView textViewHome, TextView textViewScore,
                      TextView textViewData, ImageView imageViewAway, TextView textViewAway) {
        this.imageViewHome = imageViewHome;
        this.textViewHome = textViewHome;
        this.textViewScore = textViewScore;
        this.textViewData = textViewData;
        this.imageViewAway = imageViewAway;
        this.textViewAway = textViewAway;
    }

    public ImageView getImageViewHome() {
        return imageViewHome;
    }

    public void setImageViewHome(ImageView imageViewHome) {
        this.imageViewHome = imageViewHome;
    }

    public TextView getTextViewHome() {
        return textViewHome;
    }

    public void setTextViewHome(TextView textViewHome) {
        this.textViewHome = textViewHome;
    }

    public TextView getTextViewScore() {
        return textViewScore;
    }

    public void setTextViewScore(TextView textViewScore) {
        this.textViewScore = textViewScore;
    }

    public TextView getTextViewData() {
        return textViewData;
    }

    public void setTextViewData(TextView textViewData) {
        this.textViewData = textViewData;
    }

    public ImageView getImageViewAway() {
        return imageViewAway;
    }

    public void setImageViewAway(ImageView imageViewAway) {
        this.imageViewAway = imageViewAway;
    }

    public TextView getTextViewAway() {
        return textViewAway;
    }

    public void setTextViewAway(TextView textViewAway) {
        this.textViewAway = textViewAway;
    }
}
