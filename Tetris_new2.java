import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.*;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.event.ActionListener;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
 class GamePanel extends JPanel implements KeyListener
{
    private int mapRow =21;
    private int mapCol =12;
    private int mapGame[][]=new int [mapRow][mapCol];//存放map資訊
    private Timer timer;
    Random random = new Random();
    private int curShapeType = -1;// 設定當前的形狀型別
    private int curShapeState = -1;// 設定當前的形狀狀態
    private int nextShapeType =-1; //設定下一次出現的方塊組的型別
    private int nextShapeState = -1;//設定下一次出現方塊組的狀態aaa
    private int posx = 0;//初始位置
    private int posy = 0;//初始位置
    private int score = 0;

    private final int shapes[][][] = new int[][][]
    {
        //T字形按逆時針的順序儲存
        {
            {0,1,0,0, 1,1,1,0, 0,0,0,0, 0,0,0,0},
            {0,1,0,0, 1,1,0,0, 0,1,0,0, 0,0,0,0},
            {1,1,1,0, 0,1,0,0, 0,0,0,0, 0,0,0,0},
            {0,1,0,0, 0,1,1,0, 0,1,0,0, 0,0,0,0}
        },
         //I字形按逆時針的順序儲存
        {
            {0,0,0,0, 1,1,1,1, 0,0,0,0, 0,0,0,0},
            {0,1,0,0, 0,1,0,0, 0,1,0,0, 0,1,0,0},
            {0,0,0,0, 1,1,1,1, 0,0,0,0, 0,0,0,0},
            {0,1,0,0, 0,1,0,0, 0,1,0,0, 0,1,0,0}
        },
        //倒Z形按逆時針的順序儲存
        {
            {0,1,1,0, 1,1,0,0, 0,0,0,0, 0,0,0,0},
            {1,0,0,0, 1,1,0,0, 0,1,0,0, 0,0,0,0},
            {0,1,1,0, 1,1,0,0, 0,0,0,0, 0,0,0,0},
            {1,0,0,0, 1,1,0,0, 0,1,0,0, 0,0,0,0}
        },
            //Z形按逆時針的順序儲存
        {
            {1,1,0,0, 0,1,1,0, 0,0,0,0, 0,0,0,0},
            {0,1,0,0, 1,1,0,0, 1,0,0,0, 0,0,0,0},
            {1,1,0,0, 0,1,1,0, 0,0,0,0, 0,0,0,0},
            {0,1,0,0, 1,1,0,0, 1,0,0,0, 0,0,0,0}
        },
            //J字形按逆時針的順序儲存
        {
            {0,1,0,0, 0,1,0,0, 1,1,0,0, 0,0,0,0},
            {1,1,1,0, 0,0,1,0, 0,0,0,0, 0,0,0,0},
            {1,1,0,0, 1,0,0,0, 1,0,0,0, 0,0,0,0},
            {1,0,0,0, 1,1,1,0, 0,0,0,0, 0,0,0,0}
        },
            //L字形按逆時針的順序儲存
        {
            {1,0,0,0, 1,0,0,0, 1,1,0,0, 0,0,0,0},
            {0,0,1,0, 1,1,1,0, 0,0,0,0, 0,0,0,0},
            {1,1,0,0, 0,1,0,0, 0,1,0,0, 0,0,0,0},
            {1,1,1,0, 1,0,0,0, 0,0,0,0, 0,0,0,0}
         },
            //田字形按逆時針的順序儲存
            {
            {1,1,0,0, 1,1,0,0, 0,0,0,0, 0,0,0,0},
            {1,1,0,0, 1,1,0,0, 0,0,0,0, 0,0,0,0},
            {1,1,0,0, 1,1,0,0, 0,0,0,0, 0,0,0,0},
            {1,1,0,0, 1,1,0,0, 0,0,0,0, 0,0,0,0}
            }
    };

    private int rowRect = 4;
    private int colRect = 4;        //4*4二維陣列
    private int RectWidth = 10;

    public GamePanel()//建構函式----建立好地圖
    {
        CreateRect();
        initmap();//初始化這個地圖
        setWall();//設定牆
    // CreateRect();
        timer = new Timer(500,new TimerListener());
        timer.start();
    }

    class TimerListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            movedown();                 //下墜
        }
    }

    public void setWall()
    {
        for(int i=0;i<mapRow-1;i++)
        {
            mapGame[i][0]=2;                //設定0 和 11列都是牆壁
            mapGame[i][11]=2;
        }
        for(int j =0;j<mapCol;j++)
        {
            mapGame[20][j] =2;                  //設定畫底
        }
    }

    public void initmap()               //初始地圖      設定牆壁    = 2, 空格 = 0, rec = 1
    {
        for(int i=0;i<mapRow;i++)
        {
            for(int j =0;j<mapCol;j++)
            {
                mapGame[i][j]=0;
            }
        }
    }

    

    public void CreateRect()
    {
        if(curShapeType == -1 && curShapeState == -1)
        {
            curShapeType = random.nextInt(shapes.length);
            curShapeState = random.nextInt(shapes[0].length);
        }else{
            curShapeType = nextShapeType;
            curShapeState = nextShapeState;
        }
        nextShapeType = random.nextInt(shapes.length);
        nextShapeState = random.nextInt(shapes[0].length);

        posx = 0;  //生成rec的位置
        posy = 1;
        if(GameOver(posx,posy, curShapeType, curShapeState))
        {
            JOptionPane.showConfirmDialog(null, "game over","hint",JOptionPane.OK_OPTION);
            System.exit(0);
        }
    }

    public boolean GameOver(int x, int y, int Type, int State)
    {//判斷是否結束
        if(IsOrNOMoving(x,y,Type,State))
        {
            return false;
        }
        return true;
    }
    
  
    public boolean IsOrNOMoving(int x,int  y,int ShapeType,int ShapeState)
    {
        for(int i=0;i<rowRect;i++){
            for(int j=0;j<colRect;j++){
                if(shapes[ShapeType][ShapeState][i*colRect+j]==1 && mapGame[x+i][y+j]==1 ||
                shapes[ShapeType][ShapeState][i*colRect+j]==1 && mapGame[x+i][y+j]==2 )
                {
                    return false;
                }
            }
        }
        return true;
    }
    public void Turn()
    {
        int temp =curShapeState;
        curShapeState = (curShapeState+1) %shapes[0].length;
        System.out.println(curShapeState);
        if(IsOrNOMoving(posx,posy,curShapeType,curShapeState)){}
        else{
            curShapeState = temp;
        }
        repaint();
    }

    public void movedown()
    {
        if(IsOrNOMoving(posx+1,posy,curShapeType,curShapeState)){
            posx++;
        }else{
            AddToMap();
            CheckLine();
            CreateRect();
        }
        repaint();

    }

    public void moveleft()
    {
        if(IsOrNOMoving(posx,posy-1,curShapeType,curShapeState)){
            posy--;
        }
        repaint();
    }
    public void moveright(){
        if(IsOrNOMoving(posx,posy+1,curShapeType,curShapeState)){
            posy++;
        }
        repaint();
    }
    
    public void AddToMap(){
        for(int i=0;i<rowRect;i++){
            for(int j=0;j<colRect;j++){
                if(shapes[curShapeType][curShapeState][i*colRect+j]==1){
                    mapGame[posx+i][posy+j]=shapes[curShapeType][curShapeState][i*colRect+j];
                }
            }
        }
    }

    public void CheckLine()             //檢查是否滿行
    {
        int count =0;
        for(int i=mapRow-2;i>=0;i--)
        {
            count=0;
            for(int j=1;j<mapCol;j++)
            {
                if(mapGame[i][j]==1)
                {
                    count++;
                }else{
                    break;
                }
                if(count>=mapCol-2)
                {
                    for(int k=i;k>0;k--)
                    {
                        for(int p =1;p<mapCol-1;p++)
                        {
                            mapGame[k][p] = mapGame[k-1][p];
                            
                        }
                    }
                    score+=10;
                    i++;
                }
            }
        }
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        for(int i=0;i<rowRect;i++){
            for(int j=0;j<colRect;j++){
                if(shapes[curShapeType][curShapeState][i*colRect+j]==1){
                    g.fillRect((posy+j+1)*RectWidth,(posx+i+1)*RectWidth, RectWidth, RectWidth);
                }
            }
        }
        for(int i=0;i<mapRow;i++){
            for(int j=0;j<mapCol;j++){
                if(mapGame[i][j]==2){
                    g.fillRect((j+1)*RectWidth,(i+1)*RectWidth, RectWidth, RectWidth);
                }
                if(mapGame[i][j]==1){
                    g.fillRect((j+1)*RectWidth,(i+1)*RectWidth, RectWidth, RectWidth);
                }
            }
        }

      
        g.drawString("Score ="+ score, 255,15);
        g.drawString("next:",225,50);
        for(int i=0;i< rowRect; i++)
        {
            for(int j =0; j< colRect; j++)
            {
                if(shapes[nextShapeType][nextShapeState][i*colRect+j]==1)
                {
                    g.fillRect(225+(j*RectWidth), 100+(i*RectWidth), RectWidth, RectWidth);
                }
            }
        }
    }
    public void newgame()
    {
        score =0;
        inimap();
        setWall();
        CreateRect();
        repaint();
    }
    public void StopGame()
    {
        timer.stop();
        
    }
    public void continueGame()
    {
        timer.start();
    }
    @Override
    public void keyTyped(KeyEvent e){}
    @Override
    public void keyPressed(KeyEvent e){
        switch(e.getKeyCode()){
            case KeyEvent.VK_UP:
                Turn();
                break;
            case KeyEvent.VK_DOWN:
                movedown();
                break;
            case KeyEvent.VK_RIGHT:
                moveright();
                break;
            case KeyEvent.VK_LEFT:
                moveleft();
                break;
        }
    }
    @Override
    public void keyReleased(KeyEvent e){}
}

public class Tetris_new2 extends JFrame implements ActionListener
{
    private JMenu menu  = new JMenu();
    private JMenuItem newgame = menu.add("restart");
    private JMenuItem exit = menu.add("exit");
    private JMenuItem stop = menu.add("stop");
    private JMenuItem conti = menu.add("continue");

    
    GamePanel gamePanel = new GamePanel();
    private int widthFrame = 500;
    private int heightFrame = 350;
    public Tetris_new2()
    {
        addKeyListener(gamePanel);
        newgame.addActionListener(this);
        exit.addActionListener(this);
        stop.addActionListener(this);
        conti.addActionListener(this);
        this.add(gamePanel);
        JMenuBar menu_bar = new JMenuBar();
        menu_bar.add(menu);
        this.setJMenuBar(menu_bar);

        menu_bar.add(new JMenuItem("menu") {

            @Override
            
            public Dimension getPreferredSize() {
            
            Dimension d = super.getPreferredSize();
            
            d.width = Math.max(d.width, 10); // set minimums
            
            d.height = Math.max(d.height, 50);
            
            return d;
            
            }
        });
        //this.add(menu_bar);
        this.setTitle("T_e_t_r_i_s");
        this.setBounds(50,10,widthFrame,heightFrame);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == newgame)//遊戲重新開始
        {
            gamepanel.newgame();
        }
        if(e.getSource()==exit)
        {
            System.exit(0);
        }
        if(e.getSource()==stop)
        {
            gamePanel.StopGame();
        }
        if(e.getSource()==conti)
        {
            gamePanel.continueGame();
        }
    }

    public static void main(String[] args) {
            new Tetris_new2();
    }
}