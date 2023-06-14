package Arkanoid;

import java.awt.Color;

import java.awt.Graphics;

public class LevelGenerator {
	public int arena [][];	// Matriz que representa el mapa del nivel
	public int scorem[][];  // Matriz que representa los puntajes del nivel
	
	//Constructor que inicializa la matriz de acuerdo al número de filas y columnas que recibe como argumento.
	public LevelGenerator(int fila, int col) {
		arena= new int [fila][col];
		scorem= new int [fila][col];
		initialize();
	}
	
	// inicializa ambas matrices
	public void initialize() {
		for(int i=0;i<arena.length;i++) {
			for (int j=0;j<arena[0].length;j++) {
				arena[i][j]=1;
				scorem[i][j]=-1;
			}
		}	
	}
	
	// dibuja el mapa
	public void dibujarArena(Graphics g) {
	//ladrillo
		g.setColor(Color.MAGENTA);
		int brickW;// ancho de mi ladrillo
		int brickH;// altura de mi ladrillo
		brickH=25;
		brickW=70;
		int brickX;
		brickX=10;
		int brickY;
		brickY=15;
		
		for(int j=0; j<arena.length; j++) {
			for(int i=0;i<arena[0].length;i++) {
				if(arena[j][i]==1) {
					setearColor(g,j,i);
					g.fill3DRect(brickX,brickY,brickW,brickH,true);
				}
				brickX+=brickW;// Sumo para poder moverme en mi matriz de ladrillos
			}	
			brickX=10;
			brickY+=brickH;
		}
	}
	
	// pinta segun los datos recibidos
	public void setearColor(Graphics g, int rc, int i) {
		
		switch(rc) {
			case 0:
				g.setColor(Color.yellow);
				scorem[rc][i]=170;// Representa mi color y mi score
				break;
			case 1:
				g.setColor(Color.red);
				scorem[rc][i]=145;
				break;
			case 2:
				g.setColor(Color.LIGHT_GRAY);
				scorem[rc][i]=120;
				break;
			case 3:
				g.setColor(Color.BLUE);
				scorem[rc][i]=105;
				break;
			case 4:
				g.setColor(Color.pink);
				scorem[rc][i]=50;
				break;
			case 5:
				g.setColor(Color.green);
				scorem[rc][i]=25;
				break;
			
			default:
				g.setColor(Color.ORANGE);
				scorem[rc][i]=225;
				break;
		}
	}
	
	// set para los bloques
	public void set_Ladrillo(int valor, int fila, int columna) {
		arena[fila][columna]= valor;
	}

}
