package models;

import java.util.ArrayList;
import java.util.List;

public enum Tags {
    JAVASCRIPT("<javascript>"),
    JAVA("<java>"),
    C_SHARP("<C#>"),
    PHP("<php>"),
    ANDROID("<android>"),
    PYTHON("<python>"),
    JQUERY("<jquery>"),
    HTML("<html>"),
    C_PLUS_PLUS("<c++>"),
    IOS("<ios>"),
    CSS("<css>"),
    MY_SQL("<mysql>"),
    SQL("<sql>"),
    ASP_DOT_NET("<asp.net>"),
    RUBY_ON_RAILS("<ruby-on-rails>"),
    C("<c>"),
    ARRAYS("<arrays>"),
    R("<r>"),
    OBJECTIVE_C("<objective-c>"),
    DOT_NET("<.net>"),
    NODE_DOT_JS("<node.js>"),
    JSON("<json>"),
    SQL_SERVER("<sql-server>"),
    ANGULAR_JS("<angularjs>"),
    SWIFT("<swift>"),
    //top 25 /\
    AJAX("<ajax>"),
    DJANGO("<django>"),
    XML("<xml>"),
    ASP_DOT_NET_MVC("<asp.net-mvc>"),
    ANGULAR("<angular>"),
    DATABASE("<database>"),
    SPRING("<spring>"),
    REACT_JS("<reactjs>"),
    LARAVEL("<laravel>"),
    MONGO_DB("<mongodb>"),
    TWITTER_BOOTSTRAP("<twitter-bootstrap>"),
    SCALA("scala"),
    TYPESCRIPT("<typescript>"),
    ENTITY_FRAMEWORK("<entity-framework>"),
    EXPRESS("<express>"),
    REACT_NATIVE("<react-native>"),
    SPRING_MVC("<spring-mvc>"),
    OOP("<oop>"),
    VUE_DOT_JS("<vue.js>"),
    IONIC_FRAMEWORK("<ionic-framework>"),
    ASP_DOT_NET_WEB_API("<asp.net-web-api>"),
    ASP_DOT_NET_CORE("<asp.net-core>"),
    DESIGN_PATTERNS("<design-patterns>"),
    ARCHITECTURE("<architecture>");

    private String tagString;

    Tags(String tagString){
        this.tagString = tagString;
    }

    public String getTagString() {
        return tagString;
    }

    public static List<String> getAllTagsStrings(){
        List<String> tags = new ArrayList<>();
        for(Tags t: Tags.values()){
            tags.add(t.getTagString());
        }
        return tags;
    }
}

