package ArkanoidClient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class Main_Frame extends JFrame{

	private Long cTime_end;			// contiene el tiempo final de juego
	private Long difTime;			// contiene la diferencia de tiempo entre el inicio y fin de juego
	private Long cTime_start;		// contiene el tiempo inicial de juego
	private String sTotalTime;		// contiene el tiempo final total
	private int flagPstate;			// estado del puerto
	private int flagCstate;			
	private cWindow ventana;		// clase que genera una nueva ventana
	private Drawing dw1;			// clase que dibuja propiamente el juego
	private int flagSP;				// indica la cantidad de jugadores seleccionados
	private int iport;				// almacena el puerto ingresado por el usuario
	private String sIP;				// contiene la ip ingresada por el usuario
	private String aLineas [] = new String [10]; // contiene las lineas leidas del txt con los puntajes historicos
	private Container c;
	private BufferedImage initPic;
    private JLabel initPicLabel;
	private JLabel lp1;				// Label indicador de jugador 1
	private JLabel lp2;				// Label indicador de jugador 2
	private JLabel lscr1; 			// Label que indica el puntaje del jugador 1
	private JLabel lscr2;			// Label que indica el puntaje del jugador 2
	private JLabel lliv1;			// Label que me indica las vidas del jugador 1
	private JLabel lliv2;			// Label que me indica las vidas del jugador 2
	private JLabel llvl; 			// Label que me indica el nivel del juego 
	private JLabel lspd; 			// Label que me indica la velocidad del juego
	private JButton bplay; 			// Botón de play
	private JButton bpause;			// Botón de pausa
	private JButton bconfig;		// Botón de configuraciones
	private JLabel lcpyr;			// Etiqueta para selección de jugadores
	private JPanel pplayground;		// Panel donde se llevará a cabo el juego
	private JPanel pdata;			// Panel de datos
	private JPanel pp1;				// Panel del jugador 1
	private JPanel pp2;				// Panel del jugador 2
	private JPanel pconfig;			// Panel de configuraciones
	private JLabel scr1value;		// Etiqueta con el valor del puntaje del jugador 1
	private JLabel scr2value;		// Etiqueta con el valor del puntaje del jugador 2
	private JLabel llivvalue1;		// Etiqueta con el valor de las vidas del jugador 1
	private JLabel llivvalue2;		// Etiqueta con el valor de las vidas del jugador 2
	private JLabel lsplayers;		// Label indicador de Player1
	private JComboBox<String> cPlayers; // ComboBox indicador de cantidad de jugadores
	private JLabel llvlvalue;		// Label con el valor del nivel
	private JLabel lspdvalue;		// Label con el valor de la velocidad
	private JPanel pp12;			// Panel indicador de Player1 y/o Player2
	private JPanel pp22;			// Panel indicador de seleccion de jugadores
	private JButton bnetconfig; 	// Botón para la configuración de la red
	private boolean gamestate=false;// Variable booleana que va a indicarme el estado de mi juego
	
	// clase que genera y maneja la ventana net Client
	class cWindow extends JFrame{
		
		private Container d;
		
		cWindow(){
			//Layout absoluto
		    setLayout(null);
		    //Tamaño de la ventana
		    setBounds(10,10,300,150);
		    //Título
		    setTitle("Client Network configuration");
		    setIconImage(new ImageIcon (getClass().getResource("/net_icon.png")).getImage());
		    //No redimensionable
		    setResizable(false);
		    
		    //Paneles
		    JPanel p1 = new JPanel();
			p1.setLayout(new FlowLayout());
			JPanel p3 = new JPanel();
			p3.setLayout(new FlowLayout());
			JPanel p2 = new JPanel();
			p2.setLayout(new FlowLayout());
			p2.setPreferredSize(new Dimension(15, 30));
		    
		    d= getContentPane();
			d.setLayout(new BorderLayout());
			
			JLabel lPort = new JLabel("Port:");
			lPort.setPreferredSize(new Dimension(120, 25));
			lPort.setBorder(new CompoundBorder( // sets two borders
					BorderFactory.createMatteBorder(0,0, 0, 0, Color.LIGHT_GRAY), // outer border
					BorderFactory.createEmptyBorder(0, 30,0, 0))); // inner invisible border as the margin 
			
			JTextField tPort = new JTextField("50000");
			tPort.addKeyListener(new KeyAdapter()
			{
			   public void keyTyped(KeyEvent e)
			   {
			      char caracter = e.getKeyChar();
			      // Verificar si la tecla pulsada no es un digito
			      if(((caracter < '0') ||
			         (caracter > '9')) &&
			         (caracter != '\b'))
			      {
			         e.consume();  // ignorar el evento de teclado
			      }
			      
			      if(tPort.getText().length() == 5) {
			    	  e.consume();  // ignorar el evento de teclado
			      }
			   }
			});
			tPort.setPreferredSize(new Dimension(150, 30));
			
			JLabel lIp = new JLabel("IP:");
			lIp.setPreferredSize(new Dimension(120, 25));
			lIp.setBorder(new CompoundBorder( // sets two borders
					BorderFactory.createMatteBorder(0,0, 0, 0, Color.LIGHT_GRAY), // outer border
					BorderFactory.createEmptyBorder(0, 30,0, 0))); // inner invisible border as the margin 
			
			JTextField tIp = new JTextField("192.168.0.11");
			tIp.addKeyListener(new KeyAdapter()
			{
			   public void keyTyped(KeyEvent e)
			   {
			      char caracter = e.getKeyChar();
			      // Verificar si la tecla pulsada no es un digito
			      if(((caracter < '0') ||
			         (caracter > '9')) &&
			         ( (caracter != '\b')&&
			         (caracter != '.')))
			      {
			         e.consume();  // ignorar el evento de teclado
			      }
			      
			      if(tIp.getText().length() == 15) {
			    	  e.consume();  // ignorar el evento de teclado
			      }
			   }
			});
			tIp.setPreferredSize(new Dimension(150, 30));
			
			JButton bPort = new JButton("Accept");
			bPort.setBounds(0, 0, 20, 40);
			// accion del boton Aceptar del net config
			bPort.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					iport = Integer.parseInt(tPort.getText());
					sIP =	tIp.getText();
					System.out.println("IP: "+sIP);

					if(iport <= 0 || iport > 65535) {
						JOptionPane.showMessageDialog(new JFrame(), "Port range should be between 1 and 65535 ", "Input Error",
						JOptionPane.ERROR_MESSAGE);
						flagPstate = 1;
					}else {
						flagPstate = 0;
					}
												
					if(flagPstate == 0 && flagCstate == 0) {
						bplay.setEnabled(true);
						dispose();
					}else {
						bplay.setEnabled(false);
					}

				}
			});
			
			p1.add(lPort, BorderLayout.WEST);
			p1.add(tPort, BorderLayout.EAST);
			p3.add(lIp, BorderLayout.WEST);
			p3.add(tIp, BorderLayout.EAST);
			p2.add(bPort, BorderLayout.CENTER);
			d.add(p1, BorderLayout.NORTH);
			d.add(p3, BorderLayout.CENTER);
			d.add(p2, BorderLayout.SOUTH);		
			setLocationRelativeTo(null);
		    
		    setVisible(true);
		}
	}
	
	// Hilo de inicio de juego
	Thread BeginT = new Thread() {
		
		public void run() {
			dw1=new Drawing();
			dw1.setFlagsp(flagSP);
			System.out.println("Seleccione: "+flagSP);
			pplayground.add(dw1);
			String aux="";
			String auxscore;
			dw1.setPortn(iport);
			dw1.setIpn(sIP);
			SwingUtilities.invokeLater(new Runnable()
		    {
		      public void run() {
		    	  pplayground.setFocusable(true);
		    	  dw1.requestFocusInWindow();
		      }
		    });
	        
			while(gamestate==true) {

				auxscore=String.valueOf(dw1.get_score());
				aux="00000"+auxscore;
				aux=aux.substring(auxscore.length());
				scr1value.setText(aux);
				auxscore=String.valueOf(dw1.get_score2());
				aux="00000"+auxscore;
				aux=aux.substring(auxscore.length());
				scr2value.setText(aux);
				llivvalue1.setText(String.valueOf(dw1.getLive1()));
				llivvalue2.setText(String.valueOf(dw1.getLive2()));
				 
				if(dw1.getFlagGo()==1 || dw1.getFlag()==1) {
					bplay.setEnabled(true);
					bpause.setEnabled(false);
				}
				String levelaux = llvlvalue.getText();
				
				if(dw1.getFlagST() == 1) {
					while(true) {
						dw1.socketFinishedServer();
						if(dw1.getFlagGo1() == 1) {
							break;
						}
					}
				}
				 
				if(dw1.getFlagST() == 1 && dw1.getFlagGo1() == 1) {
					if(flagSP == 1) {
						endTimer();
						calcTimer();
						openFile();
					}
					if(flagSP == 2) {
						while(dw1.socketFinishedClient()==-1);
						openFile();
						writeFile(dw1.gettScore(), dw1.getName(), dw1.getStime());
					}else {
						writeFile(dw1.gettScore(), dw1.getName(), sTotalTime);
					}
					makeTable();
					dw1.setFlagST(0);
				}
				 
				if(Integer.parseInt(levelaux)!=dw1.getLevel()) {
					bplay.setEnabled(true);
					bpause.setEnabled(false); 
					llvlvalue.setText(dw1.getLevel()+"");
				}  
	    	}
	    }
	};
	
	// constructor de la clase
	public Main_Frame() {
		initConfig();
		initializeData();
		setVisible(true);
	}
	
	// inicializa la ventana principal
	private void initConfig() {
		setTitle("ARKANOID Torravi 3000 turbo edition - Client");
		setSize(900,700);
		setResizable(false);
		setIconImage(new ImageIcon (getClass().getResource("/logo.png")).getImage());
		c= getContentPane();
		c.setLayout(new BorderLayout());
		setLocationRelativeTo(null);
		pplayground = new JPanel();
		pplayground.setSize(300,300);
		pplayground.setLayout(new BorderLayout());

		try {
			initPic = ImageIO.read(new File("logoInit.png"));
			initPicLabel = new JLabel(new ImageIcon(initPic));
			pplayground.add(initPicLabel,BorderLayout.EAST);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		pdata = new JPanel();
		pdata.setLayout(new GridBagLayout());	
		pdata.setBackground(new Color(192,192,192));
		
		pp1 = new JPanel();
		pp1.setLayout(new BoxLayout(pp1, BoxLayout.Y_AXIS));
		pp2 = new JPanel();
		pp2.setLayout(new BoxLayout(pp2, BoxLayout.Y_AXIS));
		pconfig=new JPanel();
		pconfig.setLayout(new BoxLayout(pconfig,BoxLayout.Y_AXIS));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pp12= new JPanel();
		pp12.setLayout(new GridLayout(3,2));
		pp22= new JPanel();
		pp22.setLayout(new BoxLayout(pp22,BoxLayout.Y_AXIS));
	}
	
	// genera la tabla de puntajes que muestra al final del juego
	public void makeTable() {
		// nombre de las columnas 
		String[] columnNames = { "Rank", "Name", "Total Score", "Time" };
		DefaultTableModel modelo = new DefaultTableModel(null,columnNames){

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
							
	JTable tabla = new JTable (modelo);
	Object[] aux = {"","","",""};
	tabla.setBounds(30,40,100,100);
		
	TableColumnModel columnModel = tabla.getColumnModel();
		//cambio en el ancho de columnas
	    columnModel.getColumn(0).setPreferredWidth(50);
	    columnModel.getColumn(1).setPreferredWidth(100);
	    columnModel.getColumn(2).setPreferredWidth(100);
	    columnModel.getColumn(3).setPreferredWidth(100);
	    //cambio en la fuente de la tabla
	    tabla.setFont(new java.awt.Font("Tahoma", 0, 16));
	    //altura de la celda
	    tabla.setRowHeight(30);
	    //alineado de celdas
	    DefaultTableCellRenderer Alinear = new DefaultTableCellRenderer();
	    Alinear.setHorizontalAlignment(SwingConstants.CENTER);
	    tabla.getColumnModel().getColumn(0).setCellRenderer(Alinear);
	    tabla.getColumnModel().getColumn(1).setCellRenderer(Alinear);
	    tabla.getColumnModel().getColumn(2).setCellRenderer(Alinear);
	    tabla.getColumnModel().getColumn(3).setCellRenderer(Alinear);
	    // se añaden datos a la tabla
	    for (int i = 0; i < aLineas.length ; i++) {
	    	String linea = aLineas[i];
	    	String[] partes = linea.split(",");
	    	// creo un vector con una fila
	    	aux[0]= partes[0];
	    	aux[1]= partes[1];
	    	aux[2]= partes[2];
	    	aux[3]= partes[3];
	     	// añado la fila al modelo
	     	modelo.addRow(aux);
	    }
		
	    JScrollPane sp=new JScrollPane(tabla);
	    
	    BufferedImage myPicture;
	    JLabel picLabel;
		try {
			myPicture = ImageIO.read(new File("spaceship.png"));
			picLabel = new JLabel(new ImageIcon(myPicture));
			pplayground.add(picLabel,BorderLayout.SOUTH);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		pplayground.add(sp,BorderLayout.NORTH);
		pplayground.updateUI();
	}
	
	// abre el archivo txt
	public boolean openFile() {
	    File Flectura = null;
	    LineNumberReader lineaL = null;
	    BufferedReader bufferL = null;
	    try {
	    	Flectura = new File("highscore.txt");
			lineaL = new LineNumberReader(new FileReader(Flectura));
			bufferL = new BufferedReader(lineaL);
		    String linea;
		    int count = 0;
		    while((linea=bufferL.readLine())!=null && count < 10) {
	            System.out.println(linea);
	            aLineas[count] = linea;
	            count+=1;
		    }
		} catch(Exception e){
			e.printStackTrace();
	        return false;
	    }finally{
	    	// En el finally cerramos el fichero
	        try{                    
	        	if( null != lineaL ){   
	        		lineaL.close();     
	            }                  
	        }catch (Exception e2){ 
	            e2.printStackTrace();
	            return false;
	        }
	    }
		return true;
	}
	
	// convierte String a Int
	public int convertScore(String linea){
		String[] partes = linea.split(",");
		return Integer.parseInt(partes[2]);
	}
	
	// escribe el archivo txt
	public void writeFile(int newScore, String name, String ptime) {
		File fEscritura = null;
		FileWriter escribir = null;
		BufferedWriter bescribir = null;
		String aux;
		String aux2;
		
		for(int i = 0; i<aLineas.length;i++){
			if(newScore >= convertScore(aLineas[i])) {
				if(i==9) {
					aLineas[i] = "10"+","+name+","+newScore+","+ptime;
					break;
				}
				
				aux = aLineas[i];
				String[] partes = aux.split(",");
				Integer g = i+2;
				Integer j = i+1;
				partes[0]= g.toString();
				aux=partes[0]+","+partes[1]+","+partes[2]+","+partes[3];
				aLineas[i] = j.toString()+","+name+","+newScore+","+ptime;
				i++;
				do {
					aux2 = aLineas[i];
					aLineas[i] = aux;
					aux=aux2;
					partes = aux.split(",");
					g = i+2;
					partes[0]= g.toString();
					aux=partes[0]+","+partes[1]+","+partes[2]+","+partes[3];
					i++;
				}while(i!=10);
			}
		}
		
		try {
			fEscritura = new File("highscore.txt");
			escribir = new FileWriter(fEscritura);
			bescribir= new BufferedWriter(escribir);
			
			for(int i=0; i<aLineas.length;i++){
				bescribir.write(aLineas[i]+"\n");
			}
		}catch (Exception e2){ 
            e2.printStackTrace();
        }finally{
        	try {
				bescribir.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
		
	}
	
	// inicializa los paneles y labels
	private void initializeData() {
		
		lp1= new JLabel("Player 1");
		lp1.setFont(new Font("Fipps", Font.PLAIN, 14));
		lp1.setAlignmentX(CENTER_ALIGNMENT);
		lp2= new JLabel("Player 2");
		lp2.setFont(new Font("Fipps", Font.PLAIN, 14));
		lp2.setAlignmentX(CENTER_ALIGNMENT);
		lscr1= new JLabel("SCORE");
		lscr1.setFont(new Font("G.B.BOOT", Font.PLAIN, 20));
		lscr1.setAlignmentX(CENTER_ALIGNMENT);
		scr1value= new JLabel ("00000");
		scr1value.setFont(new Font("G.B.BOOT", Font.PLAIN, 24));
		scr1value.setAlignmentX(CENTER_ALIGNMENT);
		lscr2= new JLabel("SCORE");
		lscr2.setFont(new Font("G.B.BOOT", Font.PLAIN, 20));
		lscr2.setAlignmentX(CENTER_ALIGNMENT);
		scr2value= new JLabel ("00000");
		scr2value.setFont(new Font("G.B.BOOT", Font.PLAIN, 24));
		scr2value.setAlignmentX(CENTER_ALIGNMENT);
		lliv1= new JLabel("LIVES");
		lliv1.setFont(new Font("G.B.BOOT", Font.PLAIN, 20));
		lliv1.setAlignmentX(CENTER_ALIGNMENT);
		llivvalue1=new JLabel("3");
		llivvalue1.setFont(new Font("G.B.BOOT", Font.PLAIN, 24));
		llivvalue1.setAlignmentX(CENTER_ALIGNMENT);
		lliv2= new JLabel("LIVES");
		lliv2.setFont(new Font("G.B.BOOT", Font.PLAIN, 20));
		lliv2.setAlignmentX(CENTER_ALIGNMENT);
		llivvalue2=new JLabel("3");
		llivvalue2.setFont(new Font("G.B.BOOT", Font.PLAIN, 24));
		llivvalue2.setAlignmentX(CENTER_ALIGNMENT);
		
		c.add(pplayground,BorderLayout.WEST);
		c.add(pdata,BorderLayout.EAST);
		
		pp1.add(lp1);
		pp1.add(lscr1);
		pp1.add(scr1value);
		pp1.add(lliv1);
		pp1.add(llivvalue1);
		
		GridBagConstraints c1 = new GridBagConstraints();
		c1.insets = new Insets(10, 10, 10, 10);
	    c1.gridwidth = 1;
	    c1.weightx = 0.5;
	    c1.fill=GridBagConstraints.BOTH;
	    c1.weighty = 1;
	    c1.gridx = 0;
	    c1.gridy = 0; 
		pdata.add(pp1,c1);
		
		c1.gridx = 1;
	    c1.gridy = 0;
		pp2.add(lp2);
		pp2.add(lscr2);
		pp2.add(scr2value);
		pp2.add(lliv2);
		pp2.add(llivvalue2);
		pdata.add(pp2,c1);
		
		c1.gridwidth = 2;
		c1.gridx = 0;
	    c1.gridy = 1;
	    
		bplay= new JButton("Play"); // Botón de play
		bplay.setEnabled(false);
		bplay.setToolTipText("Select number of players to play.");
		
		// accion del boton play
		bplay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(gamestate==false){
					initPicLabel.setIcon(null);
					initTimer();
					gamestate=true;
					BeginT.start();
					cPlayers.setEnabled(false);
					lsplayers.setEnabled(false);
					if(flagSP==1) {
						pp2.setEnabled(false);
						lp2.setEnabled(false);
						lscr2.setEnabled(false);
						scr2value.setEnabled(false);
						lliv2.setEnabled(false);
						llivvalue2.setEnabled(false);
					}
				}else {
					if(dw1.getFlagl()==1) {
						dw1.getLg().initialize();
						dw1.set_totalbricks(3);
						dw1.setFlagl(0);
						dw1.restartMusic();
					}
					if(dw1.getFlag()== 1) {
						dw1.setFlag(0);
					}
					if(dw1.getFlagGo()==1) {
						dw1.setFlagGo(0);
						dw1.setCountGO(1);
					}
					dw1.requestFocusInWindow();
					dw1.setFlagPause(0);
					dw1.setFadds(0);
				}
				bpause.setEnabled(true);
				bplay.setEnabled(false);
				gamestate=true;
			}
		});
		
		bplay.setAlignmentX(CENTER_ALIGNMENT);
		bpause= new JButton("Pause");
		bpause.setAlignmentX(CENTER_ALIGNMENT);
		bpause.setEnabled(false);
		// accion del boton pausa
		bpause.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				
				bplay.setEnabled(true);
				bpause.setEnabled(false);
				dw1.setFlagPause(1);	
			}
		});

		lsplayers = new JLabel("Select players");
		cPlayers = new JComboBox<String>();
		cPlayers.addItem("-------------------------------------------");
		//cPlayers.addItem("1 Player");
		cPlayers.addItem("2 Players");
		cPlayers.setPreferredSize(new Dimension(200, 10));
		// Accion a realizar cuando el JComboBox cambia de item seleccionado.
		cPlayers.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(cPlayers.getSelectedItem().toString().equals("1 Player")) {
					flagSP=1;
					bplay.setEnabled(true);
					pp1.setVisible(false);
					bnetconfig.setEnabled(false);
					ToolTipManager.sharedInstance().setEnabled(false);
				}else if(cPlayers.getSelectedItem().toString().equals("2 Players")){
					flagSP=2;
					bplay.setEnabled(false);
					bnetconfig.setEnabled(true);
					pp1.setVisible(true);
					ToolTipManager.sharedInstance().setEnabled(false);
				}else {
					flagSP=0;
					bplay.setEnabled(false);
					bnetconfig.setEnabled(false);
					pp1.setVisible(true);
					ToolTipManager.sharedInstance().setEnabled(true);
				}
			}
		});
		
		llvl= new JLabel("LEVEL");
		llvl.setFont(new Font("G.B.BOOT", Font.PLAIN, 20));
		llvl.setAlignmentX(CENTER_ALIGNMENT);
		llvlvalue= new JLabel("1");
		llvlvalue.setFont(new Font("G.B.BOOT", Font.PLAIN, 24));
		llvlvalue.setAlignmentX(CENTER_ALIGNMENT);
		lspd= new JLabel("SPEED");
		lspd.setFont(new Font("G.B.BOOT", Font.PLAIN, 20));
		lspd.setAlignmentX(CENTER_ALIGNMENT);
		lspdvalue= new JLabel("1");
		lspdvalue.setFont(new Font("G.B.BOOT", Font.PLAIN, 24));
		lspdvalue.setAlignmentX(CENTER_ALIGNMENT);

		pconfig.add(llvl);
		pconfig.add(llvlvalue);
		pconfig.add(lspd);
		pconfig.add(lspdvalue);
		pconfig.add(bplay);
		pconfig.add(bpause);
		
		pp12.add(lsplayers, BorderLayout.SOUTH);
		pp12.add(cPlayers, BorderLayout.CENTER);
		pconfig.add(pp12);
		pdata.add(pconfig,c1);
		bnetconfig= new JButton("Net configuration");
		bnetconfig.setEnabled(false);
		bnetconfig.setAlignmentX(CENTER_ALIGNMENT);
		pconfig.add(bnetconfig);
		
		// accion del boton net config
		bnetconfig.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ventana = new cWindow();
			}
		});
	}
	
	// toma el tiempo inicial del juego
	public void initTimer() {
		
		cTime_start = System.currentTimeMillis();
	}
	
	// toma el tiempo final del juego
	public void endTimer() {
		
		cTime_end = System.currentTimeMillis();
	}
	
	// calcula el tiempo total de juego
	public void calcTimer() {
		
		difTime = cTime_end - cTime_start;					
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
		Date date = new Date(difTime);
		sTotalTime = simpleDateFormat.format(date);
	}
}
