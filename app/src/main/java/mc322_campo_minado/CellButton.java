package mc322_campo_minado;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;

/**
 * Um JButton customizado para representar cada célula do tabuleiro
 * com tema dark, hover e troca de ícones (gema, mina, bandeira, explosão).
 * Usa apenas um tipo de gema (gemMedium) e mantém o background neutro,
 * pois a cor da gema vem diretamente da imagem do ícone.
 */
public class CellButton extends JButton {
    private static final Color BG_DEFAULT = Color.decode("#1E2230");
    private static final Color BG_HOVER   = Color.decode("#2A2E40");
    private static final Color BG_MINE    = Color.decode("#B71C1C"); // mina/explosão

    private static Icon gemIcon;
    private static Icon mineIcon;
    private static Icon flagIcon;
    private static Icon explosionAnim;

    static {
        try {
            // Carrega ícones via ClassLoader
            gemIcon      = new ImageIcon(ImageIO.read(
                CellButton.class.getClassLoader().getResourceAsStream("assets/gem_medium.png")
            ));
            mineIcon     = new ImageIcon(ImageIO.read(
                CellButton.class.getClassLoader().getResourceAsStream("assets/mine.png")
            ));
            flagIcon     = new ImageIcon(ImageIO.read(
                CellButton.class.getClassLoader().getResourceAsStream("assets/flag.png")
            ));
            explosionAnim= new ImageIcon(ImageIO.read(
                CellButton.class.getClassLoader().getResourceAsStream("assets/explosion.png")
            ));
        } catch (Exception e) {
            System.err.println("Erro ao carregar ícones de CellButton:");
            e.printStackTrace();
            gemIcon = mineIcon = flagIcon = explosionAnim = null;
        }
    }

    public CellButton() {
        super();
        setBackground(BG_DEFAULT);
        setBorder(BorderFactory.createEmptyBorder());
        setFocusPainted(false);
        setOpaque(true);
        setIcon(null);

        // Efeito hover
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setBackground(BG_HOVER);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                setBackground(BG_DEFAULT);
            }
        });
    }

    /**
     * Exibe o ícone de gema (único tipo) para casas seguras.
     */
    public void showGem() {
        setIcon(gemIcon);
    }

    /** Exibe o ícone de mina e background de alerta. */
    public void showMine() {
        setIcon(mineIcon);
        setBackground(BG_MINE);
    }

    /** Exibe o ícone de bandeira. */
    public void showFlag() {
        setIcon(flagIcon);
    }

    /** Exibe animação de explosão sobre a célula. */
    public void showExplosion() {
        setIcon(explosionAnim);
        setBackground(BG_MINE);
    }

    /** Restaura o estado visual original (antes de qualquer reveal). */
    public void reset() {
        setIcon(null);
        setEnabled(true);
        setBackground(BG_DEFAULT);
    }
}
