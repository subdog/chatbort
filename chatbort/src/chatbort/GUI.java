import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

public class GUI
{

  String name = "C.I.A. SecretChat v. 2.6.9";
  JFrame init;
  JFrame chatFrame = new JFrame(name);
  JTextArea chatWindow;
  JButton send;
  JTextField newInput;
  JTextField user;
  ChatBot chatBot;

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
    chatBot = new ChatBot();
  }

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

    southPanel.add(newInput, left);
    southPanel.add(send, right);

    mainPanel.add(BorderLayout.SOUTH, southPanel);

    chatFrame.add(mainPanel);
    chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    chatFrame.setSize(670, 500);
    chatFrame.setVisible(true);
  }

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

  String username;

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