package mc322_campo_minado;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;

/**
 * JButton customizado que representa uma célula do tabuleiro do Campo Minado.
 * 
 * Este botão possui tema escuro, efeito de hover e pode exibir diferentes ícones
 * (gema, mina, bandeira, explosão) de acordo com o estado da célula.
 * O background é neutro, pois a cor da gema é definida pela imagem do ícone.
 * 
 * Os ícones são carregados uma única vez por meio do ClassLoader.
 */
public class CellButton extends JButton {
    /** Cor padrão do fundo da célula */
    private static final Color BG_DEFAULT = Color.decode("#1E2230");
    /** Cor de fundo ao passar o mouse (hover) */
    private static final Color BG_HOVER   = Color.decode("#2A2E40");
    /** Cor de fundo para mina/explosão */
    private static final Color BG_MINE    = Color.decode("#B71C1C");

    // Ícones estáticos compartilhados entre todas as instâncias
    private static ImageIcon gemIcon;
    private static ImageIcon mineIcon;
    private static ImageIcon flagIcon;
    private static ImageIcon explosionAnim;

    /** Ícone base atual (antes do resize), usado para redimensionamento dinâmico */
    private ImageIcon currentBaseIcon = null;

    // Bloco estático para carregar os ícones apenas uma vez
    static {
        try {
            // Carrega ícones dos recursos do projeto
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

    /**
     * Construtor padrão. Inicializa o botão com tema escuro e listeners de hover e resize.
     */
    public CellButton() {
        super();
        setBackground(BG_DEFAULT);
        setBorder(BorderFactory.createEmptyBorder());
        setFocusPainted(false);
        setOpaque(true);
        setIcon(null);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);

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

        // Listener para efeito de hover (mudança de cor ao passar o mouse)
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

    /**
     * Exibe o ícone de gema (para casas seguras).
     */
    public void showGem() {
        if (gemIcon != null) {
            currentBaseIcon = gemIcon;
            int size = Math.min(getWidth(), getHeight());
            setIcon(resizeIcon(gemIcon, size));
        }
    }

    /**
     * Exibe o ícone de mina e altera o fundo para cor de alerta.
     */
    public void showMine() {
        if (mineIcon != null) {
            currentBaseIcon = mineIcon;
            int size = Math.min(getWidth(), getHeight());
            setIcon(resizeIcon(mineIcon, size));
        }
        setBackground(BG_MINE);
    }

    /**
     * Exibe o ícone de bandeira (usado para marcar possíveis minas).
     */
    public void showFlag() {
        if (flagIcon != null) {
            currentBaseIcon = flagIcon;
            int size = Math.min(getWidth(), getHeight());
            setIcon(resizeIcon(flagIcon, size));
        }
    }

    /**
     * Exibe a animação de explosão sobre a célula e altera o fundo.
     */
    public void showExplosion() {
        if (explosionAnim != null) {
            currentBaseIcon = explosionAnim;
            int size = Math.min(getWidth(), getHeight());
            setIcon(resizeIcon(explosionAnim, size));
        }
        setBackground(BG_MINE);
    }

    /**
     * Restaura o estado visual original do botão (sem ícone, fundo padrão e habilitado).
     */
    public void reset() {
        setIcon(null);
        currentBaseIcon = null;
        setEnabled(true);
        setBackground(BG_DEFAULT);
    }
}