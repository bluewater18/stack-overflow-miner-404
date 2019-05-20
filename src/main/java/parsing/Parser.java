package parsing;

import models.Post;
import models.Tags;
import models.Words;

import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Parser {
    private String jarString;
    private List<Integer> years = new ArrayList<>(Arrays.asList(2009,2010,2011,2012,2013,2014,2015,2016,2017,2018,2019));
    private Map<Integer, Map<String, Integer>> tagMap;
    private Map<Integer, Map<String, Integer>> architectureMap;
    private Map<Integer, Map<String, Integer>> frameworkMap;
    private Map<Integer, Map<String, Integer>> languageMap;


    public Parser() {
        String jarUrl = null;
        try {
            jarUrl = URLDecoder.decode(Parser.class.getProtectionDomain().getCodeSource().getLocation().getPath(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        assert jarUrl != null;
        File jarDir = new File(jarUrl);
        jarString = jarDir.getParentFile().toString();

    }

    /**
     * Parse larger file to a smaller file that only contains posts with the tag specified
     * @param fileIn name of the file to be read from the jar directory
     * @param fileOut name of the file to be written to in the jar directory
     * @param tag tag to be matched
     */
    public void parseForReduction(String fileIn, String fileOut, Tags tag) {
        BufferedReader reader;
        BufferedWriter writer;
        Path file = Paths.get(jarString+ System.getProperty("file.separator") + fileIn);
        Path outfilePath = Paths.get(jarString+ System.getProperty("file.separator") + fileOut);
        File outFile = new File(String.valueOf(outfilePath));

        try {
            reader = new BufferedReader(new FileReader(String.valueOf(file)));
            outFile.createNewFile();
            writer = Files.newBufferedWriter(outfilePath, StandardCharsets.UTF_8, StandardOpenOption.WRITE);
            String line = reader.readLine();
            int savedPostCount = 0;

            while (line != null) {
                Post p = parsePost(line);
                if(p != null && p.isQuestion() && p.getStringTags().contains(tag.getTagString())){
                    writer.write(line);
                    writer.newLine();
                    savedPostCount++;
                }

                line = reader.readLine();
                System.out.println("Copied " + savedPostCount + " from " + fileIn + " to " + fileOut + " with tag " + tag.getTagString());
            }
            reader.close();
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseForAnalysis(String fileIn, String fileOut) {
        BufferedReader reader;
        BufferedWriter writer;

        Path file = Paths.get(jarString+ System.getProperty("file.separator") + fileIn);
        Path outfilePath = Paths.get(jarString+ System.getProperty("file.separator") + fileOut);
        File outFile = new File(String.valueOf(outfilePath));

        List<Post> posts = new ArrayList<>();

        try {
            reader = new BufferedReader(new FileReader(String.valueOf(file)));

            String line = reader.readLine();

            while (line != null) {
                Post p = parsePost(line);
                if(p != null)
                    posts.add(p);
                line = reader.readLine();
            }
            //created posts list /\

            //Init Maps for years/tags/words
            tagMap = new HashMap<>();
            architectureMap = new HashMap<>();
            languageMap = new HashMap<>();
            frameworkMap = new HashMap<>();

            for(Integer i: years){
                tagMap.put(i, new HashMap<String, Integer>());
                architectureMap.put(i, new HashMap<String, Integer>());
                languageMap.put(i, new HashMap<String, Integer>());
                frameworkMap.put(i, new HashMap<String, Integer>());
            }

            for(Map.Entry<Integer, Map<String, Integer>> e: architectureMap.entrySet()) {
                for(String word: Words.architectures)
                    e.getValue().put(word,0);
            }

            for(Map.Entry<Integer, Map<String, Integer>> e: frameworkMap.entrySet()) {
                for(String word: Words.frameworks)
                    e.getValue().put(word,0);
            }

            for(Map.Entry<Integer, Map<String, Integer>> e: languageMap.entrySet()) {
                for(String word: Words.languages)
                    e.getValue().put(word,0);
            }
            int count = 0;


            int year;
            for(Post p: posts) {
                count++;
                year = p.getYear();
                for(String s : p.getWords()) {
                    insertIntoMapExisting(year, s, architectureMap);
                    insertIntoMapExisting(year, s, frameworkMap);
                    insertIntoMapExisting(year, s, languageMap);
                }

                for(String s : p.getTags()){
                    insertIntoMapNotExisting(year, s, tagMap);
                }
                System.out.println("analysis of post " + count + " completed");
            }


            outFile.createNewFile();
            writer = Files.newBufferedWriter(outfilePath, StandardCharsets.UTF_8, StandardOpenOption.WRITE);

            writeMapToWriter(writer, architectureMap);
            writeMapToWriter(writer, frameworkMap);
            writeMapToWriter(writer, languageMap);
            writeMapToWriterWithMin(writer, tagMap, 10);

            reader.close();
            writer.flush();
            writer.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeMapToWriter(BufferedWriter writer, Map<Integer, Map<String, Integer>> map) {
        try {
            List<Map.Entry<Integer, Map<String, Integer>>> temp = sortMapByYear(map);
            
            for (Map.Entry<Integer, Map<String, Integer>> e : temp) {
            
                List<Map.Entry<String, Integer>> tempInside = sortMapByOccurence(e.getValue());   
                for (Map.Entry<String, Integer> m : tempInside) {
                    writer.write(e.getKey() + " : " + m.getKey() + " : " + m.getValue());
                    writer.newLine();
                    //System.out.println(e.getKey() + " : " + m.getKey() + " : " + m.getValue());
                }
                writer.write(":::::::::::::::::::::::::");
                writer.newLine();
            }
            writer.write("============================");
            writer.newLine();
            writer.write("============================");
            writer.newLine();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private List<Map.Entry<String, Integer>> sortMapByOccurence(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(map.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> t0, Map.Entry<String, Integer> t1) {
                return(t0.getValue()).compareTo(t1.getValue());
            }
        });

        return list;
    }

    private List<Map.Entry<Integer, Map<String, Integer>>> sortMapByYear(Map<Integer, Map<String, Integer>> map) {
        List<Map.Entry<Integer, Map<String, Integer>>> list = new LinkedList<>(map.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<Integer, Map<String,Integer>>>() {
            @Override
            public int compare(Map.Entry<Integer, Map<String, Integer>> t0, Map.Entry<Integer, Map<String,Integer>> t1) {
                return(t0.getKey()).compareTo(t1.getKey());
            }
        });
        return list;
    }
    
    

    private void writeMapToWriterWithMin(BufferedWriter writer, Map<Integer, Map<String, Integer>> map, int min) {
        try {
            List<Map.Entry<Integer, Map<String, Integer>>> temp = sortMapByYear(map);
            for (Map.Entry<Integer, Map<String, Integer>> e : temp) {

                List<Map.Entry<String, Integer>> tempInside = sortMapByOccurence(e.getValue());
                for (Map.Entry<String, Integer> m : tempInside) {
                    if(m.getValue() >= min) {
                        writer.write(e.getKey() + " : " + m.getKey() + " : " + m.getValue());
                        writer.newLine();
                        //System.out.println(e.getKey() + " : " + m.getKey() + " : " + m.getValue());
                    }
                }
                writer.write(":::::::::::::::::::::::::");
                writer.newLine();
            }
            writer.write("============================");
            writer.newLine();
            writer.write("============================");
            writer.newLine();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    private void insertIntoMapExisting(Integer year, String toInsert, Map<Integer, Map<String, Integer>> map) {
        try {
            Map<String, Integer> temp = map.get(year);
            if (temp.containsKey(toInsert))
                temp.put(toInsert, temp.get(toInsert) + 1);
        }catch (NullPointerException e) {}
    }

    private void insertIntoMapNotExisting(Integer year, String toInsert,  Map<Integer, Map<String, Integer>> map){
        try {
            Map<String, Integer> temp = map.get(year);
            if (temp.containsKey(toInsert))
                temp.put(toInsert, temp.get(toInsert) + 1);
            else
                temp.put(toInsert, 1);
        } catch (NullPointerException e){}

    }
    

    /**
     * parses an xml representation of a singular post from a string
     * @param line xml representation of a post
     * @return post object made from the data
     */
    private Post parsePost(String line) {
        org.jdom.input.SAXBuilder saxBuilder = new SAXBuilder();
        try {
            org.jdom.Document doc = saxBuilder.build(new StringReader(line));
            if (Integer.parseInt(doc.getRootElement().getAttributeValue("PostTypeId")) == 1) {
                Post p = new Post(Integer.parseInt(doc.getRootElement().getAttributeValue("Id")),
                        Integer.parseInt(doc.getRootElement().getAttributeValue("PostTypeId")),
                        doc.getRootElement().getAttributeValue("CreationDate"),
                        doc.getRootElement().getAttributeValue("Score"),
                        doc.getRootElement().getAttributeValue("ViewCount"),
                        doc.getRootElement().getAttributeValue("Body"),
                        doc.getRootElement().getAttributeValue("Title"),
                        doc.getRootElement().getAttributeValue("Tags"),
                        doc.getRootElement().getAttributeValue("AnswerCount"),
                        doc.getRootElement().getAttributeValue("CommentCount"),
                        doc.getRootElement().getAttributeValue("FavoriteCount"));
                return p;
            }
            return null;
        } catch (JDOMException e) {
            System.out.println("JDME");
            // handle JDOMException
        } catch (IOException e) {
            System.out.println("IOE");
            // handle IOException
        } catch (NumberFormatException e) {
            System.out.println("NFE");
        }
        return null;
    }
}