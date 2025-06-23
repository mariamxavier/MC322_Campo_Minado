package mc322_campo_minado;

import javax.swing.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Um JButton customizado para representar cada célula do tabuleiro
 * com tema dark, hover e troca de ícones (diamante, mina, bandeira, explosão).
 * Usa apenas um tipo de gema (gemMedium) e mantém o background neutro,
 * pois a cor da gema vem diretamente da imagem do ícone.
 */
public class CellButton extends JButton implements Observer {
    private static final Color BG_DEFAULT = Color.decode("#1E2230");
    private static final Color BG_MINE    = Color.decode("#B71C1C"); // mina/explosão

    private static ImageIcon gemIcon;
    private static ImageIcon mineIcon;
    private static ImageIcon explosionAnim;

    /** Ícone base atual (antes do resize), usado para redimensionamento dinâmico */
    private ImageIcon currentBaseIcon = null;


    private Cell cell;

    static {
        try {
            // Carrega ícones via ClassLoader
            gemIcon      = new ImageIcon(ImageIO.read(
                CellButton.class.getClassLoader().getResourceAsStream("assets/gem_medium.png")
            ));
            mineIcon     = new ImageIcon(ImageIO.read(
                CellButton.class.getClassLoader().getResourceAsStream("assets/mine.png")
            ));
            explosionAnim= new ImageIcon(ImageIO.read(
                CellButton.class.getClassLoader().getResourceAsStream("assets/explosion.png")
            ));
        } catch (Exception e) {
            System.err.println("Erro ao carregar ícones de CellButton:");
            e.printStackTrace();
            gemIcon = mineIcon = explosionAnim = null;
        }
    }

    public CellButton() {
        super();
        setBackground(BG_DEFAULT);
        setBorder(BorderFactory.createEmptyBorder());
        setFocusPainted(false);
        setOpaque(true);
        setIcon(null);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);

        /* Efeito hover
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setBackground(BG_HOVER);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                setBackground(BG_DEFAULT);
            }
        }); */

        // Listener para redimensionar o ícone conforme o tamanho do botão
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (currentBaseIcon != null) {
                    int size = Math.min(getWidth(), getHeight());
                    setIcon(resizeIcon(currentBaseIcon, size));
                }
            }
        });
    }

    /**
     * Redimensiona o ícone para manter o formato quadrado centralizado no botão.
     * 
     * @param icon Ícone original
     * @param size Tamanho desejado (largura/altura)
     * @return Ícone redimensionado
     */
    private Icon resizeIcon(ImageIcon icon, int size) {
        Image img = icon.getImage();
        Image resized = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(resized);
    }

    // Novo construtor com Cell
    public CellButton(Cell cell) {
        this();
        this.cell = cell;     
        cell.addObserver(this); // registra como observador
    }

    /**
     * Exibe o ícone de gema (único tipo) para casas seguras.
     */
    public void showGem() {
        if (gemIcon != null) {
            currentBaseIcon = gemIcon;
            int size = Math.min(getWidth(), getHeight());
            setIcon(resizeIcon(gemIcon, size));
        }
    }

    /** Exibe o ícone de mina e background de alerta. */
    public void showMine() {
        if (mineIcon != null) {
            currentBaseIcon = mineIcon;
            int size = Math.min(getWidth(), getHeight());
            setIcon(resizeIcon(mineIcon, size));
        }
        setBackground(BG_MINE);
    }

    /** Exibe animação de explosão sobre a célula. */
    public void showExplosion() {
        if (explosionAnim != null) {
            currentBaseIcon = explosionAnim;
            int size = Math.min(getWidth(), getHeight());
            setIcon(resizeIcon(explosionAnim, size));
        }
        setBackground(BG_MINE);
    }

    /** Restaura o estado visual original (antes de qualquer reveal). */
    public void reset() {
        setIcon(null);
        setEnabled(true);
        setBackground(BG_DEFAULT);
    }

    @Override
    public void update() {
        if (cell.isRevealed()) {
            setEnabled(false);
            if (cell.hasMine()) {
                showMine();
            } else {
                showGem();
            }
        }
    }
}
