/**
 * @author Dan Kestell
 * 
 *         GUI is a class that runs the GUI which includes a load screen on 
 *         which to enter your username, and a chat screen in shich you can
 *         engage in a chat with Dan the CIA agent.
 *         
 *         This was based on a basic outline from 
 *         http://codereview.stackexchange.com/questions/25461/simple-chat-room-swing-gui
 *         
 *         The GUI instantiates an instance of ChatBot.java and refers to it 
 *         for replies to input.
 */

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class GUI
{

  private String name = "C.I.A. SecretChat v. 2.6.9";
  private JFrame init;
  private JFrame chatFrame = new JFrame(name);
  private JTextArea chatWindow;
  private JButton send;
  private JTextField newInput;
  private JTextField user;
  public ChatBot chatBot;
  public String username;
  
  //runs the GUI, initiates the screen to the login phase 
  public static void main(String[] args)
  {

    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e)
        {
          e.printStackTrace();
        }
        GUI mainGUI = new GUI();
        try
        {
          mainGUI.login();
        } catch (IOException e)
        {
          e.printStackTrace();
        } catch (ChatBot.MalformedInputException e)
        {
          e.printStackTrace();
        }
      }
    });
  }

  //Gui for login, user enters their username and then enters the chat interface
  public void login() throws IOException, ChatBot.MalformedInputException
  {
    chatFrame.setVisible(false);
    init = new JFrame(name);
    user = new JTextField(15);
    JLabel chooseUsernameLabel = new JLabel("Choose your username:");
    JButton enterServer = new JButton("Start Chat");
    enterServer.addActionListener(new StartChatListener());
    JPanel prePanel = new JPanel(new GridBagLayout());

    GridBagConstraints preRight = new GridBagConstraints();
    preRight.insets = new Insets(0, 0, 0, 10);
    preRight.anchor = GridBagConstraints.EAST;
    GridBagConstraints preLeft = new GridBagConstraints();
    preLeft.anchor = GridBagConstraints.WEST;
    preLeft.insets = new Insets(0, 10, 0, 10);
    // preRight.weightx = 2.0;
    preRight.fill = GridBagConstraints.HORIZONTAL;
    preRight.gridwidth = GridBagConstraints.REMAINDER;

    prePanel.add(chooseUsernameLabel, preLeft);
    prePanel.add(user, preRight);
    init.add(BorderLayout.CENTER, prePanel);
    init.add(BorderLayout.SOUTH, enterServer);
    init.setSize(670, 500);
    init.setVisible(true);
    chatBot = new ChatBot(); //initializes the chatbot to interact with GUI
  }

  //The actual chat interface, press send to enter chat to Dan the CIA operative
  public void startChat()
  {
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());

    JPanel southPanel = new JPanel();
    southPanel.setLayout(new GridBagLayout());

    newInput = new JTextField(30);
    newInput.requestFocusInWindow();

    send = new JButton("Send");

    send.addActionListener(new SendListener());

    chatWindow = new JTextArea();
    chatWindow.setEditable(false);
    chatWindow.setFont(new Font("Serif", Font.PLAIN, 15));
    chatWindow.setLineWrap(true);

    mainPanel.add(new JScrollPane(chatWindow), BorderLayout.CENTER);

    GridBagConstraints left = new GridBagConstraints();
    left.anchor = GridBagConstraints.LINE_START;
    left.fill = GridBagConstraints.HORIZONTAL;
    left.weightx = 512.0D;
    left.weighty = 1.0D;
   
    GridBagConstraints right = new GridBagConstraints();
    right.insets = new Insets(0, 10, 0, 0);
    right.anchor = GridBagConstraints.LINE_END;
    right.fill = GridBagConstraints.NONE;
    right.weightx = 1.0D;
    right.weighty = 1.0D;

    //message box to enter message into
    southPanel.add(newInput, left);
    southPanel.add(send, right);

    mainPanel.add(BorderLayout.SOUTH, southPanel);

    chatFrame.add(mainPanel);
    chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    chatFrame.setSize(670, 500);
    chatFrame.setVisible(true);
  }

  //Listener for chat window, listens for a button press in order to send 
  //  message and recieve response.
  class SendListener implements ActionListener
  {
    public void actionPerformed(ActionEvent event)
    {
      if (newInput.getText().length() < 1)
      {
        // do nothing
      }
      else if (newInput.getText().equals(".clear"))
      {
        chatWindow.setText("Cleared all messages\n");
        newInput.setText("");
      }
      else
      {
        String input = newInput.getText();
        chatWindow.append(username + ": " + input + "\n");
        chatWindow.append(chatBot.setInput(input));
        newInput.setText("");
      }
      newInput.requestFocusInWindow();
    }
  }
  
  //Listens for start of chat, takes username from text box and saves it for
  //  use in the GUI.
  class StartChatListener implements ActionListener
  {
    public void actionPerformed(ActionEvent event)
    {
      username = user.getText();
      if (username.length() < 1)
      {
        System.out.println("No!");
      }
      else
      {
        init.setVisible(false);
        startChat();
      }
    }
  }
}