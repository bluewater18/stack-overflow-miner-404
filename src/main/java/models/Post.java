package models;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XmlRootElement
public class Post {
    private int id;
    private int postTypeId;
    private String creationDate;
    private int score;
    private int viewCount;
    private String body;
    private String title;
    private String stringTags;
    private ArrayList<String> tags;
    private int answerCount;
    int commentCount;
    int favouriteCount;

    public Post(int id, int postTypeId, String creationDate, String score, String viewCount, String body, String title, String tags, String answerCount, String commentCount, String favouriteCount ){
        this.id = id;
        this.postTypeId = postTypeId;
        this.creationDate = creationDate;
        if(score != null)
            this.score = tryNullOrZero(score);
        if(viewCount != null)
            this.viewCount = tryNullOrZero(viewCount);
        this.body = body;
        this.title = title;
        if(tags != null && tags.length() > 0) {
            String temp;
            temp = tags.replaceAll("<", "");
            temp = temp.replaceAll(">", ",");
            this.tags = new ArrayList<>(Arrays.asList(temp.split(",")));
        }
        this.stringTags = tags;
        if(answerCount != null)
            this.answerCount = tryNullOrZero(answerCount);
        if(commentCount != null)
            this.commentCount = tryNullOrZero(commentCount);
        if(favouriteCount != null)
            this.favouriteCount = tryNullOrZero(favouriteCount);
    }

    public boolean isQuestion(){
        return postTypeId == 1;
    }

    public boolean isAnswer(){
        return postTypeId == 2;
    }

    private int tryNullOrZero(String score) {
        int temp = 0;
        try {
            temp = Integer.parseInt(score);
        } catch (NumberFormatException e){

        }
        return temp;
    }

    public String getStringTags() {
        return stringTags;
    }

    public int getYear(){
        try {
            return Integer.parseInt(creationDate.substring(0, 4));
        }catch (NumberFormatException nfe) {
            return -1;
        }
    }

    public List<String> getWords() {
        List<String> words = new ArrayList<>();
        List<String> finalWords = new ArrayList<>();
        words.addAll(Arrays.asList(title.split(" ")));
        words.addAll(Arrays.asList(body.split(" ")));
        for(String s: words){
            finalWords.add(s.trim().toLowerCase());
        }
        return finalWords;

    }

    public ArrayList<String> getTags() {
        if(tags != null)
            return tags;
        return new ArrayList<>();
    }
}
