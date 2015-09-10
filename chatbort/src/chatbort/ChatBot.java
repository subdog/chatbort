import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class ChatBot
{
  public ChatBot() throws IOException, MalformedInputException
  {
    init();
  }
  
  /**
   * Location of the conversations file from which the dictionary will be
   * parsed and constructed.
   */
  public static final String CONVERSATION_FILEPATH = "chatbort/chatbort/src/chatbort/chatdat.txt";
  
  /**
   * The hashmap that stores input phrases and lists of possible corresponding
   * output phrases.
   */
  private static HashMap<String, ArrayList<String>> dictionary;
  
  /**
   * This method builds the dictionary hashmap from the specified conversations
   * file.
   * @throws IOException If the conversation file is not found.
   * @throws MalformedInputException If there is a syntax error in the
   * conversations file.
   */
  public static void init() throws IOException, MalformedInputException
  {
    BufferedReader in = new BufferedReader(new FileReader(CONVERSATION_FILEPATH));
    String line;
    boolean inBranch = false;
    
    LinkedList<String> keys = null;
    ArrayList<String> values = null;
    
    dictionary = new HashMap<String, ArrayList<String>>();
    
    while ((line = in.readLine()) != null)
    {
      line = line.trim();
      if (line.length() > 0) // Skip any empty lines.
      {
        if (!inBranch) //If we are not currently in a conversation branch...
        {
          if (line.equals("[")) //Start a new conversation branch.
          {
            keys = new LinkedList<String>();
            values = new ArrayList<String>();
            inBranch = true;
          }
          else
          {
            throw new MalformedInputException(line);
          }
        }
        else
        {
          if (line.equals("]")) //End the current conversation branch and put
        	                    //what we have in the hashmap.
          {
            inBranch = false;
            
            for (String key : keys)
            {
              dictionary.put(key, values);
            }
            
            keys = null;
            values = null;
          }
          else if (line.charAt(0) == 'q') //Add a new query to the current
        	                               //conversation branch.
          {
            keys.add(line.substring(2));
          }
          else if (line.charAt(0) == 'r') //Add a possible response to the
        	                               //current conversation branch.
          {
            values.add(line.substring(2));
          }
          else
          {
            throw new MalformedInputException(line);
          }
        }
      }
    }
    
    in.close();
  }
  
  /**
   * @param s Some input string.
   * @return A lowercase version of the input string with all punctuation and
   *          extra spaces deleted.
   */
  public static String stripPunctuationAndLowercase(String s)
  {
    StringBuilder key = new StringBuilder();
    boolean singleSpace = false;
    for (int i = 0; i < s.length(); i++)
    {
      char c = s.charAt(i);
      if (Character.isAlphabetic(c))
      {
        singleSpace = false;
        key.append(Character.toLowerCase(c));
      }
      else if (c == ' ')
      {
        if (!singleSpace)
        {
          key.append(c);
          singleSpace = true;
        }
      }
    }
    return key.toString().trim();
  }
  
  public static ArrayList<String> adjacentCombinations(String s)
  {
    String [] words = s.split(" ");
    ArrayList<String> combinations = new ArrayList<String>();
    for (int i = words.length; i > 0; i--)
    {
      for (int j = 0; j + i <= words.length; j++)
      {
        combinations.add(joinWords(Arrays.copyOfRange(words, j, j + i)));
      }
    }
    return combinations;
  }
  
  /**
   * Join an array of words into a sentence.
   * @param words The array with the words of a sentence.
   * @return The string comprising of the sentence.
   */
  public static String joinWords(String [] words)
  {
    if (words.length == 0) return "";
    StringBuilder s = new StringBuilder(words[0]);
    
    for (int i = 1; i < words.length; i++)
    {
      s.append(' ');
      s.append(words[i]);
    }
    
    return s.toString();
  }
  
  /**
   * @param s The user's query.
   * @return A response if the query exists somewhere in the hashmap, or a
   * simple default string if it does not.
   */
  public static String respond(String s)
  {
    String key = stripPunctuationAndLowercase(s);
    
    /* Break the key up into all possible adjoining words and see if any get a
     * match in the dictionary.
     */
    
    ArrayList<String> adjCombs = adjacentCombinations(key);
    ArrayList<String> vals = null;
    
    for (String phrase : adjCombs)
    {
      ArrayList<String> curVals = dictionary.get(phrase);
      if (curVals != null)
      {
        vals = curVals;
        break;
      }
    }
    
    if (vals == null)
    {
      return "Sorry, I was lost in thought.  Could we talk about something else?";
    }
    else
    {
      return vals.get((int) (Math.random() * vals.size()));
    }
  }
  
//  public static void main(String args[])
//  {
//    try
//    {
//      init();
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//      System.exit(-1);
//    }
//    
//    Scanner in = new Scanner(System.in);
//    while (true)
//    {
//      System.out.print("You: ");
//      String input = in.nextLine();
//      System.out.println("Dan: " + respond(input));
//    }
//  }
  
  /**
   * Exception class to handle parse errors in the input conversation file and
   * display a message to the user based on where the error was made.
   */
  static class MalformedInputException extends Exception
  {
    private static final long serialVersionUID = 1L;
	
    public MalformedInputException(String line)
    {
      super("Syntax error on token " + line);
    }
  }

  public String setInput(String text)
  {
    return "Dan: " + respond(text) + "\n";
  }
}
