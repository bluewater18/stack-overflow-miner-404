import java.util.ArrayList;

public class Question extends Post {

    public Question(int id, String creationDate, String score, String viewCount, String body, String title, String tags, String answerCount, String commentCount, String favouriteCount) {

    }

    @Override
    public String toString(){
        return title + " with tags " + tags;
    }

    public String getStringTags() {
        return stringTags;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public String getTitle() {
        return title;
    }
}
