package mc322_campo_minado;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Minesweeper {

    int tileSize = 70; // tamanho do quadrado
    int numRows = 8; // Numero de Linhas
    int numCols = numRows; // Numero de Colunas
    int boardHeight = numRows * tileSize; // Tamanho borda horizontal
    int boardWidth = numCols * tileSize; // Tamanho borda vertical

    JFrame frame = new JFrame("Minesweeper");

    Minesweeper() {
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
    }
}
