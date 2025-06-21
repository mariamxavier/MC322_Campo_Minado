package mc322_campo_minado;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;

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

    private static ImageIcon gemIcon;
    private static ImageIcon mineIcon;
    private static ImageIcon flagIcon;
    private static ImageIcon explosionAnim;

    private ImageIcon currentBaseIcon = null;  // ícone atual, antes do resize

    static {
        try {
            // Carrega ícones via ClassLoader
            gemIcon = new ImageIcon(ImageIO.read(
                CellButton.class.getClassLoader().getResourceAsStream("assets/gem_medium.png")
            ));
            mineIcon = new ImageIcon(ImageIO.read(
                CellButton.class.getClassLoader().getResourceAsStream("assets/mine.png")
            ));
            flagIcon = new ImageIcon(ImageIO.read(
                CellButton.class.getClassLoader().getResourceAsStream("assets/flag.png")
            ));
            explosionAnim = new ImageIcon(ImageIO.read(
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
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);

        // Redimensiona o ícone conforme o tamanho da célula
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (currentBaseIcon != null) {
                    int size = Math.min(getWidth(), getHeight());
                    setIcon(resizeIcon(currentBaseIcon, size));
                }
            }
        });

        // Efeito hover
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(BG_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(BG_DEFAULT);
            }
        });
    }

    /** Redimensiona o ícone para manter formato quadrado centralizado */
    private Icon resizeIcon(ImageIcon icon, int size) {
        Image img = icon.getImage();
        Image resized = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(resized);
    }

    /** Exibe o ícone de gema (unico tipo) para casas seguras. */
    public void showGem() {
        if (gemIcon != null) {
            currentBaseIcon = gemIcon;
            int size = Math.min(getWidth(), getHeight());
            setIcon(resizeIcon(gemIcon, size));
        }
    }

    /** Exibe o ícone de mina e background de alerta.*/
    public void showMine() {
        if (mineIcon != null) {
            currentBaseIcon = mineIcon;
            int size = Math.min(getWidth(), getHeight());
            setIcon(resizeIcon(mineIcon, size));
        }
        setBackground(BG_MINE);
    }

    /** Exibe o ícone de bandeira*/
    public void showFlag() {
        if (flagIcon != null) {
            currentBaseIcon = flagIcon;
            int size = Math.min(getWidth(), getHeight());
            setIcon(resizeIcon(flagIcon, size));
        }
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

    /** Restaura o estado visual original (antes de qualquer reveal)*/
    public void reset() {
        setIcon(null);
        currentBaseIcon = null;
        setEnabled(true);
        setBackground(BG_DEFAULT);
    }
}
