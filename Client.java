
import java.io.*;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Client extends JFrame{

    Socket socket;
    BufferedReader br;
    PrintWriter out;

    private JLabel heading = new JLabel("Client Area");
    private JTextArea msgArea = new JTextArea();
    private JTextField msgInput = new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,  20);
    
    public Client()
    {
        try
        {
            System.out.println("Sending request to server");
            socket = new Socket("127.0.0.1" , 7777);
            System.out.println("Connection done...");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();
            startReading();
            //startWriting();
        }   catch (Exception e){
              e.printStackTrace();
            }
    }
     
    
    private void handleEvents() {
        msgInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //System.out.println("Key released :" + e.getKeyChar());
                if(e.getKeyCode()==10){
                    //System.out.println("You have pressed enter button");
                    String contentToSend = msgInput.getText();
                    msgArea.append("Me : "+ contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    msgInput.setText("");
                }
            }

        });


        }


    private void createGUI() {
        this.setTitle("Client [END]"); 
        this.setSize(500,800);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setContentPane().setBackground(Color.pink);

        heading.setFont(font);
        msgArea.setFont(font);
        msgInput.setFont(font);
        ImageIcon imageIcon = new ImageIcon(new ImageIcon("logo.png").getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
        heading.setIcon(imageIcon);
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        msgArea.setEditable(false);
        msgInput.setHorizontalAlignment(SwingConstants.CENTER);
        this.setLayout(new BorderLayout());

        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(msgArea);
        this.add(jScrollPane,BorderLayout.CENTER);
       // this.add(msgArea,BorderLayout.CENTER);
        this.add(msgInput,BorderLayout.SOUTH);
        this.setVisible(true);
        
        
    }


    public void startReading()
    {
        Runnable r1=()->{
           System.out.println("Reader started...");
           try{

           while(true)
           {
           
               String msg=br.readLine();
               //System.out.println("Server :" +msg);
               if(msg.equals("exit"))
               {
                   System.out.println("Server terminated the chat");
                   JOptionPane.showMessageDialog(this, "Server Terminated the chat");
                   msgInput.setEnabled(false);
                   socket.close();
                   break;
               }
               msgArea.append("Server : " +msg + "\n");
           }
       }   catch(Exception e){
               System.out.println("Connection is Closed");
           }
           
        };
        new Thread(r1).start();
    }
     public void startWriting() 
     {
         Runnable r2=()->{
            System.out.println("Writer Started");
            try{
            while(!socket.isClosed()){
                
                    BufferedReader br1 = new BufferedReader(new 
                    InputStreamReader(System.in));
                    String content=br1.readLine();
                    
                    out.println(content);
                    out.flush();
                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }

                }
                
             }   catch(Exception e){
                   System.out.println("Connection is Closed");
                }
            
         };
         new Thread(r2).start();

     }
    public static void main(String[] args) {
        System.out.println("This is Client");
        new Client();
    }
}
