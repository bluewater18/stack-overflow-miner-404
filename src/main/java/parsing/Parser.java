package parsing;

import models.Post;
import models.Tags;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private String jarString;

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
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                        doc.getRootElement().getAttributeValue("models.Tags"),
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