package Arkanoid;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import javax.imageio.ImageIO;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

public class Drawing extends Canvas implements ActionListener, KeyListener {
	
	private Image dibujoAux;
	private Graphics gAux;
	private Dimension dimAux;
	private Dimension dimCanvas;
	
	private ServerSocket sSocket=null;	//contiene el socket server
	private int brickaux=0; 			//auxiliar para saber cuando el cliente rompio un brick
	private int naveX=150;				//posición inicial de la nave del player1
	private int naveY=600;				//posición inicial de la nave del player1
	private int nave2X= 450;			//posición inicial de la nave del player2
	private int nave2Y=600; 			//posición inicial de la nave del player2
	private int ballX=naveX+48;			//posición inicial de la bola1
	private int ballY= naveY-14;		//posición inicial de la bola1
	private int ball2X=nave2X+48;		// posición inicial de la bola2
	private int ball2Y= nave2Y-14;		// posición inicial de la bola2
	private int ballXdir=-2;			// dirección de bola1
	private int ballYdir=-1;			// dirección de bola1
	private int ball2Xdir=-1;			// dirección de bola2
	private int ball2Ydir=-2;			// dirección de bola2
	private int delay=8;				// tiempo en ms para repintar la pantalla
	private LevelGenerator lg;			// clase que genera el mapa
	private int score=0;				// puntaje del jugador 1
	private int score2=0;				// puntaje del jugador 2
	private int tScore=0;				// puntaje acumulado por ambos jugadores
	private int live1=3;				// vidas restantes del jugador 1
	private int live2=3;				// vidas restantes del jugador 2
	private int level=1;				// nivel que se esta jugando
	private int speed=1;				// velocidad a la que se esta jugando
	private int brokebrick = 0;			// detecta un bloque roto del jugador 2
	private int posFbrick = 0;			// fila del bloque roto
	private int posCbrick = 0;			// columna del bloque roto
	private String stime="";			// almacena el tiempo de juego
	private int flag=0;					// flag que me indica que perdí una vida
	private int flagl=0;				// flag indicadora de nivel alcanzado
	private int fadds=0;				// flag para sumar puntaje extra
	private Vector<int[]> brickDel= new Vector<int[]>(); // almacena fila y columna del bloque eliminado
	private int brickH=25;				// altura en pixeles de un bloque
	private int brickW=70;				// ancho en pixeles de un bloque
	private int totalbricks=54;			// indica el total de bloques
	private int flagGo=0;				// indica que ya perdi, Game Over
	private int flagGo2=0;				// indica que ya perdio el jugador 2, Game Over
	private int flagsc=0;				// ingreso por unica vez al seteo inicial del game over
	private int flagPause=0;			// indica el estado de Pausa
	private int flagpm=0;				// estado de la reproducción de música
	private String name = "";			// nombre del jugador
	private int flagmbrick=0;			// indica si se rompio un bloque para reproducir sonido
	private int flaglup=0;				// indica si subi de nivel
	private int countGO=0;				// contador game over
	private int countlose=0;			// contador de perdidas de vidas
	private int flagf=0;				// indica si el juego ha concluido
	private int flagsp=0;				// indica el numero de jugadores seleccionados
	private Timer t1;					// timer
	private boolean fb2 = false; 		// flag para identificar si intersecta con nave dos
	private int fauxF = 0;				
	private int flagST = 0; 			// flag indicadora para visualizar la tabla de scores
	private int portn = 0;				// numero de puerto
	private int flagBn = 0;
	private BufferedImage bi = null;
	private BufferedImage ni = null;
	private BufferedImage f1 = null;
	
	//getters and setters
	public int getPortn() {
		return portn;
	}

	public void setPortn(int portn) {
		this.portn = portn;
	}
	
	public int getFlagST() {
		return flagST;
	}

	public void setFlagST(int flagST) {
		this.flagST = flagST;
	}

	public int gettScore() {
		return tScore;
	}

	public void settScore(int tScore) {
		this.tScore = tScore;
	}
	
	public int getFlagsp() {
		return flagsp;
	}

	public void setFlagsp(int flagsp) {
		this.flagsp = flagsp;
	}

	public int getCountGO() {
		return countGO;
	}

	public void setCountGO(int countGO) {
		this.countGO = countGO;
	}

	public int getFlagPause() {
		return flagPause;
	}

	public void setFlagPause(int flagPause) {
		this.flagPause = flagPause;
	}

	public LevelGenerator getLg() {
		return lg;
	}

	public void setLg(LevelGenerator lg) {
		this.lg = lg;
	}

	public int getFlagl() {
		return flagl;
	}

	public void setFlagl(int flagl) {
		this.flagl = flagl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getFlagGo() {
		return flagGo;
	}
	
	public void setFlagGo(int flagGo) {
		this.flagGo = flagGo;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getLive1() {
		return live1;
	}

	public void setLive1(int live) {
		this.live1 = live;
	}

	public int getLive2() {
		return live2;
	}

	public void setLive2(int live2) {
		this.live2 = live2;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	public int get_score() {
		return this.score;
	}
	
	public void set_score(int score) {
		this.score = score;
	}
	
	public int get_score2() {
		return score2;
	}

	public void set_score2(int score2) {
		this.score2 = score2;
	}
	
	public int get_totalbricks() {
		return totalbricks;
		
	}
	
	public void set_totalbricks (int q) {
		this.totalbricks=q;
	}
	
	public int getFadds() {
		return fadds;
	}

	public void setFadds(int fadds) {
		this.fadds = fadds;
	}
	
	public String getStime() {
		return stime;
	}

	public int getFlagGo2() {
		return flagGo2;
	}

	public void setFlagGo2(int flagGo2) {
		this.flagGo2 = flagGo2;
	}

	public void setStime(String stime) {
		this.stime = stime;
	}
	/*
	// Hilo que procesa el repaint del juego
	Thread rp = new Thread() {
		private int finalizar = 0;
		@Override
	    public void run() {
			while (finalizar == 0) {
				if((fauxF == 0 && flagGo == 1) || (fauxF==0 && flagf==1)) {
					finalizar = 1;
				}
				repaint();
				try {
					sleep(16);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};*/
	
	// Hilo que procesa el sonido del juego
	Thread mt = new Thread() {
		
		private AudioInputStream audio;
		private Clip clip;
		private AudioInputStream sound;
		private Clip clips;
		private String [] songs= {"audio/level1.wav","audio/level2.wav","audio/level3.wav","audio/level4.wav","audio/level5.wav","audio/break.wav","audio/lose_sound.wav","audio/wallHit.wav","audio/GameOversound.wav", "audio/levelCompleted.wav"};
		
		public Clip getClip() {
			return clip;
		}

		public void setClip(int state) {	
			if(state==0) clip.start();
			else clip.stop();
		}
		
		@Override
	    public void run() {
			//Función que pregunta al usuario su nombre y si desea o no música en el nivel.
			JFrame jFrame = new JFrame();
			do{
				name = JOptionPane.showInputDialog(jFrame, "Enter your name (max 4 characters):");
			}while(name.length()>4);
			
			int sound_ON = JOptionPane.showConfirmDialog(null, "Music ON?", "Music Panel",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);			
			
			if (flagsp==2) {
				while(playersSync()==-1);
			
				initServer(flagsp);
			}
			try {
				sleep(1000);
			} catch (InterruptedException e1) {}
			
			flagPause=0;
			playMusic(sound_ON, level);
			
			while(true) {
				
				if(clip != null) {
					if(flagPause==1 && flagpm==0) {
						flagpm=1;	
						clip.stop();
					}
					
					if(flagPause==0 && flagpm==1) {
						clip.start();
						flagpm=0;
					}
				}
				
				if(flagmbrick == 1){
					playSound(7);
					flagmbrick=0;
				}
				
				if(flagGo==1 && countGO==1) {
					playSound(8);
					countGO=0;	
				}
				
				if(flag==1 && countlose==1) {
					playSound(6);
					countlose=0;
				}
				
				synchronized(songs) {
					if(flagl==1) {
						try {
							clip.stop();
							playSound(9);
							sleep(500);
							sound_ON = JOptionPane.showConfirmDialog(null, "Music ON?", "Music Panel",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
							songs.wait();
						} catch (InterruptedException e) {
							try {
								sleep(1000);
							} catch (InterruptedException e1) {}
							
							playMusic(sound_ON, level);
							clip.start();
						}
					}
				}
				try {
					sleep(1);
				} catch (InterruptedException e) {}
			}
	    }
		
		//Método para reproducir un sonido por única vez
		public void playSound(int nsound) {
			
			try {	
				sound = AudioSystem.getAudioInputStream(new File(songs[nsound]).getAbsoluteFile());
				clips = AudioSystem.getClip();
				clips.open(sound);
				clips.loop(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//Función para el armado de la reproducción de música
		public void playMusic(int yesNo, int mlevel) {
			if (yesNo == 1) {
				return;
			} else if (mlevel == 5) {
				mlevel = 1;
			}
			try {
				audio = AudioSystem.getAudioInputStream(new File(songs[mlevel-1]).getAbsoluteFile());
				clip = AudioSystem.getClip();
				clip.open(audio);
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	// Hilo que maneja la tabla de scores
	Thread tablet = new Thread() {
		
		@Override
	    public void run() {
			try {
				sleep(6000);
				tScore = cScore();
				set_score(0);
				fauxF = 1;
				setVisible(false);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			flagST = 1; //flag indicadora para visualizar la tabla de scores
		}
	};
	
	// constructor de la clase
	public Drawing() {
		setSize(650,650);
		setBackground(Color.BLACK);
		setLg(new LevelGenerator(6,9));
		addKeyListener(this);
		flagPause=1;
		
		dimCanvas = this.getSize();
		try {
	        bi = ImageIO.read(new File("ball15x15.png")); 
	        ni = ImageIO.read(new File("nave110x21.png")); 
	        f1 = ImageIO.read(new File("fondo1_650x650.png"));

	    } catch (IOException e) {
	        e.printStackTrace();
	        System.out.println("Image could not be read");
	        
	    }

		
		mt.start();
		setFocusable(true);
		t1= new Timer(delay,this);
		t1.start();
		//rp.start();
	}	
	
	// devuelve el puntaje
	public int cScore() {
		
		if(flagsp == 1) {
			return this.score;
		}else if(flagsp == 2) {
			return this.score + this.score2;
		}
		return -1; // retorno -1 en caso que no se detecte ningun jugador
	}
	
	//Función que inicia al servidor en caso de seleccion de 2 players
	public void initServer(int x) {
		if(x == 2) {
			System.out.println("Entre al run");
			socketServer.start();
		}
	}
	
	// Función que reinicia la música para el nivel correspondiente
	public void restartMusic() {
		mt.interrupt();
	}
	
	// cambio de velocidad
	public void change_Speed(int l) {
		switch(l) {
			case 1:
				ballXdir=-2;
				ballYdir=-1;
				break;
			case 2:
				ballXdir=-3;
				ballYdir=-2;
			break;
			case 3:
				ballXdir=-4;
				ballYdir=-2;
				break;
			case 4:
				ballXdir=-5;
				ballYdir=-3;
				break;
			case 5:
				ballXdir=-6;
				ballYdir=-3;
				break;
		}
	}
	
	// procesa el movimiento de la bola y sus movimientos
	private void moveBall() {
		
		if(flagPause==0) {
			int brickX=10;
			int brickY=15;
			int brickW=70;
			int brickH=25;
			int flagIrect=0;
			Rectangle recti = null;
			flaglup=0;
			
			// Interseccion de las dos naves
			if (flagsp==2) {
				if(flagGo2==0) {
					if(new Rectangle(nave2X,nave2Y,110,12).intersects(new Rectangle(naveX,naveY,110,12))) {		
						if(naveX < nave2X) {
							recti= new Rectangle (naveX,naveY, nave2X+110,12);
						}else {
							recti= new Rectangle (nave2X,nave2Y, naveX+110,12);
						}			
						flagIrect = 1;
					}
				}
			}
			if(flagIrect != 1) {
				// Intersección de la bola 1 con el player1
				if(new Rectangle(ballX,ballY,14,14).intersects(new Rectangle(naveX,naveY,110,12))) {
					ballYdir=-ballYdir;
					System.out.println("inter con nave 1");
				}
				
				if (flagsp== 2) {
					if(flagGo2==0) {
						// Intersección de la bola 2 con el player1
						if(new Rectangle(ball2X,ball2Y,14,14).intersects(new Rectangle(naveX,naveY,110,12))) {
							ball2Ydir=-ball2Ydir;
						}
						// Intersección de la bola 2 con el player2
						if(new Rectangle(ball2X,ball2Y,14,14).intersects(new Rectangle(nave2X,nave2Y,110,12))) {
							ball2Ydir=-ball2Ydir;
						}
						// Intersección de la bola 1 con el player2
						if (new Rectangle (ballX,ballY,14,14).intersects(new Rectangle(nave2X,nave2Y,110,12))) {
							ballYdir=-ballYdir;
						}
					}
				}
			}else {
				if(flagGo2==0) {
					if (flagsp== 2) {
						// Intersección de la bola 1 con el rectangular
						if(new Rectangle(ballX,ballY,14,14).intersects(recti)) {
							ballYdir=-ballYdir;
						}
						// Intersección de la bola 2 con el rectangular
						if(new Rectangle(ball2X,ball2Y,14,14).intersects(recti)) {
							ball2Ydir=-ball2Ydir;
						}
					}
				}
			}
			
			flagIrect = 0;
			
			A:for (int i=0;i<getLg().arena.length;i++) {
				for(int j=0;j<getLg().arena[0].length;j++) {
					if(getLg().arena[i][j]>0) {
						brickX= 10 + j*brickW;
						brickY= 15 + i*brickH;
						Rectangle rect= new Rectangle(brickX,brickY, brickW,brickH);
						Rectangle ballrect= new Rectangle(ballX,ballY,14,14);
						Rectangle brickrect= rect;
						
						if(ballrect.intersects(brickrect)|| fb2) {
							if(ballrect.intersects(brickrect)) {
								flagBn = 1;
							}
							
							getLg().set_Ladrillo(0, i, j);
							
							if(ballrect.intersects(brickrect)) {
								score+=addscore(i,j);
								brokebrick = 1;
								posFbrick = i;
								posCbrick = j;
							}else {
								score2+=addscore(i,j);
							}
							totalbricks--;
							flagmbrick=1;
							
							int coord []= new int [2];
							coord[0]=brickrect.x;
							coord[1]=brickrect.y;
							brickDel.add(coord);
							
							if(flagBn == 1) {
								if(ballX +13 <=brickrect.x ||ballX +1 >=brickrect.x + brickrect.width) {
									ballXdir=-ballXdir;
								}else {
									ballYdir= -ballYdir;
								}
							}else {
								if(flagGo2!=1) {
									if(flagsp==2) {
										if(ball2X +13 <=brickrect.x ||ball2X +1 >=brickrect.x + brickrect.width) {
											ball2Xdir=-ball2Xdir;
										}else {
											ball2Ydir= -ball2Ydir;
										}
									}
								}
							}
							flagBn=0;
							break A;
						}
					}
				}
			}
			
			// avance de la bola
			ballX+=ballXdir;
			ballY+=ballYdir;
			
			// limites
			if(ballX<7) {
				flagmbrick=1;
				ballXdir=-ballXdir;
			}
			if(ballY<7) {
				flagmbrick=1;
				ballYdir=-ballYdir;
			}
			
			if(ballX>629) {
				flagmbrick=1;
				ballXdir=-ballXdir;
			}
			
			if(flagGo2!=1) {
				if(flagsp==2) {
					if(ball2X<0) {
						flagmbrick=1;
						ball2Xdir=-ball2Xdir;
					}
					if(ball2Y<0) {
						flagmbrick=1;
						ball2Ydir=-ball2Ydir;
					}
					if(ball2X>642) {
						flagmbrick=1;
						ball2Xdir=-ball2Xdir;
					}
				}
			}
		}
	}
	
	// puntajes segun la posicion
	private int addscore(int i, int j) {
		return getLg().scorem[i][j];
	}

	//Timer
	@Override
	public void actionPerformed(ActionEvent e) {
		
		is_Finished();
		if((fauxF == 0 && flagGo == 1) || (fauxF==0 && flagf==1)) {
			tablet.start();
			fauxF = 1;
		}
		if (flag!=1 && flagGo!=1) {
			moveBall();
			is_ballOut();
			is_LevelUp();
		}
		if(flagGo==1 && flagsc == 0) {
			getLg().initialize();
			setLive1(3);
			if(flagsp==2) {
				setLive2(3);
			}
			setSpeed(1);
			setLevel(1);
			flag=0;
			flagsc = 1;
		}	
		repaint();
	}
	
	// resta puntaje
	public void subScore(int sc) {
		if(sc == 1) {
			if(score<=25 || score==0) {
				score = 0;
			}else {
				score-=25;
			}
		}
		if(sc == 2) {
			if(score2<=25 || score2==0) {
				score2 = 0;
			}else {
				score2-=25;
			}
		}
	}
	
	// suma puntaje
	public void addScore() {
		score+=30;
	}
	
	// detecta si la bola sobrepaso a la nave
	public int is_ballOut() {
		
		if(ballY>=630) {
			naveX=150;
			naveY=600;
			ballX=naveX+48;
			ballY= naveY-14;
			ballXdir=-ballXdir;
			ballYdir=-ballYdir;
			flagPause=1;
			if(live1>1) {
				live1--;
				subScore(1);
				countlose=1;
			}
			else {
				flagGo=1;
			}
			flag=1;
		}
		
		if(flagsp==2) {
			if(ball2Y>=630) {
				nave2X=200;
				nave2Y=600;
				ball2X=nave2X+48;
				ball2Y= nave2Y-14;
				ball2Xdir=-ball2Xdir;
				ball2Ydir=-ball2Ydir;
				flagPause=1;
				if(live2>1) {
					live2--;
					subScore(2);
					countlose=1;
				}
				else {
					flagGo=1;
				}
				flag=1;
			}
		}
		return flag;
	}
	
	// detecta si perdio todas las vidas
	public boolean is_gameOver(){
		return (flagGo==1) ? (true) : (false);
	}
	
	// detecta si paso de nivel
	public void is_LevelUp() {
		if(totalbricks==0) {
			flagPause = 1;
			flagl=1;
			change_Speed(level);
			if(fadds==0) {
				addScore();
				fadds=1;
			}
			naveX=150;
			naveY=600;
			ballX=naveX+48;
			ballY= naveY-14;
		}
	}

	// detecta si termino el juego
	@SuppressWarnings("deprecation")
	public void is_Finished() {
		if(level>5) {
			flagf=1;
			mt.stop();
			t1.stop();
		}
	}
	
	// detecta las teclas presionadas
	@Override 
	public void keyPressed( KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
			if(naveX>=532) {
				naveX=532;
			}else {
				naveX+=15;
			}
		}
		if(e.getKeyCode()==KeyEvent.VK_LEFT) {
			if(naveX<=8) {
				naveX=8;
			}else {
				naveX-=15;
			}
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}
	
	@Override
	public void update(Graphics g) {
		paint(g);
	}
	
	// pinta la pantalla del juego
	@Override
	public void paint(Graphics g) {
		
		if(gAux == null || dimAux == null || dimCanvas.width != dimAux.width
			|| dimCanvas.height != dimAux.height) {
			System.out.println("entre al paint por primera vez");
			dimAux= dimCanvas;
			dibujoAux = createImage(dimAux.width, dimAux.height);
			gAux = dibujoAux.getGraphics();
			
			//System.out.println("height , width "+dibujoAux.getHeight(this) + dibujoAux.getWidth(this));
			
		}
		
		
		gAux.drawImage(f1, 0, 0, this);
		
		//gAux.setColor(Color.YELLOW);
		//System.out.println("getColor gaux "+gAux.getColor());
		
		//gAux.fillRect(0, 0,650,7);
		//gAux.fillRect(0, 0,7,661);
		//gAux.fillRect(643,0,7,661);
		
		
		if(brickaux==1) {
			brickaux = 0;
			totalbricks--;
		}
		
		if(flagf==0){
			///////////////////////////////////////////////////
			
            //g.drawImage(bi, 0, 0, this);
            ///////////////////////////////////////
			// Bola1
			//gAux.setColor(Color.WHITE);
			//gAux.fillOval(ballX,ballY, 14,14);
            gAux.drawImage(bi, ballX, ballY, this);
			// Nave 1
			//gAux.setColor(new Color (255,204,153));
			//gAux.fillRect(naveX,naveY,110,12);
            gAux.drawImage(ni, naveX, naveY, this);
			
			if(flagGo2==0) {
				// Bola 2
				if(flagsp==2) {
					g.setColor(Color.PINK);//new Color(204,229,255)
					g.fillOval(ball2X,ball2Y, 14,14);
				}
				// Nave 2
				if(flagsp==2) {
					g.setColor(new Color(229,204,255));
					g.fillRect(nave2X, nave2Y,110,12);
				}
			}
			
			getLg().dibujarArena(gAux);
			//g.drawImage(dibujoAux, 0, 0, this);
			if(flagGo==1) {
				gAux.setColor(new Color(255,189,73));
				gAux.setFont(new Font("pixel dead", Font.PLAIN,52));
				gAux.drawString("GAME OVER"+" "+name, 139, 255);
			}else {
				if (flag==1) {
					gAux.setColor(new Color(255,189,73));
					gAux.setFont(new Font("ROCK-ON Demo", Font.PLAIN,46));
					gAux.drawString("You lose!", 139, 255);
					gAux.setFont(new Font("ROCK-ON Demo", Font.PLAIN,15));
					gAux.drawString("Press Play to play again", 153, 285);
				}
			}
			
			if(flagl==1) {
				gAux.setColor(new Color(255,128,0));
				gAux.setFont(new Font("ROCK-ON Demo", Font.PLAIN,52));
				if(flaglup ==0) {
					level=level+1;
					flaglup = 1;
				}
				gAux.drawString("Level:"+level, 159, 355);
				gAux.setFont(new Font("ROCK-ON Demo", Font.PLAIN,15));
				gAux.drawString("Press Play to level up",175,380);
			}
			//g.drawImage(dibujoAux, 0, 0, this);
			//makeBlack(g);
			//g.dispose();
			
		
		}else {
			gAux.drawString("Creado y dirigido por empresa Torravi!", 139, 255);
			gAux.setFont(new Font("ROCK-ON Demo", Font.PLAIN,15));
		}
		g.drawImage(dibujoAux, 0, 0, this);
		//System.out.println(g.drawImage(dibujoAux, 0, 0, this));
		//g.drawImage(dibujoAux, 0, 0, this);
		dibujoAux = createImage(dimAux.width, dimAux.height);
		gAux = dibujoAux.getGraphics();
		
	}
	
	// pinta de negro los bloques rotos
	public void makeBlack(Graphics g){
		for (int[] xs : brickDel) {
			g.setColor(Color.black);
			g.fillRect(xs[0],xs[1], brickW ,brickH );
		}
		brickDel.removeAllElements();
	}
	
	// se encarga de sincronizar ambos jugadores antes de comenzar
	public int playersSync(){
		
		int foutn = 1;
		Socket sConnect=null;
		DataInputStream serverIn=null;
		DataOutputStream serverOut = null;
		String sdata;
		String aux;
		int error = 0;
		
		try {
			sSocket= new ServerSocket(portn);
		} catch (IOException e) {
			foutn = -1;
		}
		
		try {
			// metodo bloqueante que espera conexion de un cliente
			sConnect = sSocket.accept();
		} catch (IOException e) {
			foutn = -1;
		}
		
		try {
			serverIn = new DataInputStream(sConnect.getInputStream());
			serverOut = new DataOutputStream(sConnect.getOutputStream());
		} catch (IOException e) {
			foutn = -1;
		}
		
		try {
			// el mensaje del cliente
			sdata = serverIn.readUTF();
			//comprabar que mensaje recibio
			aux = sdata.substring(0,1);
			if(Integer.parseInt(aux) != 1) {
				// como estoy esperando un sincro de inicio y recibi un 1 continuo
				// de lo contrario es un error
				foutn = -1;
				error = 1;
			}
		} catch (IOException e) {
			foutn = -1;
		}	
		
		try {
			// respuesta del server
			if(error == 0) {
				serverOut.writeUTF("1,Ready");
			}else {
				serverOut.writeUTF("9,Ready");
			}
		} catch (IOException e) {
			foutn = -1;
		}
		
		try {
			// cierro el socket
			sConnect.close();
		} catch (IOException e) {
			foutn = -1;
		}
		
		return foutn;
	}
	
	// sincronizacion de los jugadores para finalizar
	public int socketFinishedServer() {
		
		Socket sConnect=null;
		DataInputStream serverIn = null;
		DataOutputStream serverOut = null;
		String sdata;
		String ldataIn [] = new String [3];
		int error = 0;
		String aux;
		
		try {
			sSocket= new ServerSocket(portn);
		} catch (IOException e) {}
		
		try {
			// metodo bloqueante que espera conexion de un cliente
			sConnect = sSocket.accept();
		} catch (IOException e) {}
		
		try {
			serverIn = new DataInputStream(sConnect.getInputStream());
			serverOut = new DataOutputStream(sConnect.getOutputStream());
		} catch (IOException e) {}
		
		try {
			sdata = serverIn.readUTF();
			aux = sdata.substring(0,1);
			if(Integer.parseInt(aux) == 3) {
				ldataIn = sdata.split(",");
				tScore = Integer.parseInt(ldataIn[0]) + score;
				error = 0;
			}else {
				if(Integer.parseInt(aux) == 2) {
					ldataIn = sdata.split(",");
					if(Integer.parseInt(ldataIn[8])==1) {
						error = 1;
					}else {error = -1;}
				}else {
					error = -1;
				}
			}
		} catch (IOException e) {}
		
		try {
			if(error == 0) {
				serverOut.writeUTF("3"+","+name+","+tScore+","+stime);
			}else if(error == 1){
				String saux = "2"+","+naveX+","+naveY+","+ballX+","+ballY+","+brokebrick+","+posFbrick+","+posCbrick
				+","+score+","+score2+","+live1+","+live2+","+level+","+speed+","+"1"+","+flagf+","+"0";
				serverOut.writeUTF(saux);
			}else {
				serverOut.writeUTF("9"+","+name+","+tScore+","+stime);
			}
		} catch (IOException e) {}
		
		try {
			sConnect.close();
		} catch (IOException e) {}
		return error;
	}
	

	//Hilo que maneja el socket Server
	Thread socketServer = new Thread() {

		Socket sConnect=null;
		DataInputStream serverIn=null;
		DataOutputStream serverOut = null;
		String sdata;
		String ldataIn [] = new String [9];
		int auxLevelUp = 0;
		String aux;
		int error = 0;
		int auxbrokebrick = 0;
		int auxposFb = 0;
		int auxposCb = 0;
			
		@Override
		public void run() {
			// Crea y abre el puerto para el socket
			try {
				sSocket= new ServerSocket(portn);	
			} catch (IOException e) {
				e.printStackTrace();
			}
				
			while(true) {
				try {
					// metodo bloqueante que espera conexion de un cliente
					sConnect = sSocket.accept();
				} catch (IOException e) {
					e.printStackTrace();
				}
					
				try {
					serverIn = new DataInputStream(sConnect.getInputStream());
					serverOut = new DataOutputStream(sConnect.getOutputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
					
				try {
					sdata = serverIn.readUTF();
					aux = sdata.substring(0,1);
					if(Integer.parseInt(aux) != 2) {
						error = 1;
					}
					
					if(error == 0) {
						ldataIn = sdata.split(","); 
						nave2X 		  = Integer.parseInt(ldataIn[1]);
						nave2Y 		  = Integer.parseInt(ldataIn[2]);
						ball2X 		  = Integer.parseInt(ldataIn[3]);
						ball2Y 		  = Integer.parseInt(ldataIn[4]);
						auxbrokebrick = Integer.parseInt(ldataIn[5]);
						auxposFb 	  = Integer.parseInt(ldataIn[6]);
						auxposCb	  = Integer.parseInt(ldataIn[7]);
						flagGo2 	  = Integer.parseInt(ldataIn[8]);
						flagf		  = Integer.parseInt(ldataIn[9]);
			
						if(auxbrokebrick == 1) { // brokebrick
							getLg().set_Ladrillo(0, auxposFb, auxposCb);
							brickaux=1;
						}
						
						if(totalbricks == 0) {
							auxLevelUp = 1;
						}else {
							auxLevelUp = 0;
						}
						
						String aux = "2"+","+naveX+","+naveY+","+ballX+","+ballY+","+brokebrick+","+posFbrick+","+posCbrick
								+","+score+","+score2+","+live1+","+live2+","+level+","+speed+","+flagGo+","+flagf+","+auxLevelUp;
						serverOut.writeUTF(aux);
						brokebrick = 0;
						posFbrick = 0;
						posCbrick = 0;

					}else {
						// si se produce un error
						String aux = "9"+","+naveX+","+naveY+","+ballX+","+ballY+","+brokebrick+","+posFbrick+","+posCbrick
								+","+score+","+score2+","+live1+","+live2+","+level+","+speed+","+flagGo+","+flagf+","+auxLevelUp;
						serverOut.writeUTF(aux);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				if(flagGo2 == 1) {
					break;	
				}
			}				
		}
	}; // Fin del thread 

}
