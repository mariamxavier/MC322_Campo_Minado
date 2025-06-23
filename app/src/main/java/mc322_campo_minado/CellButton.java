package mc322_campo_minado;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

/**
 * Um JButton customizado para representar cada célula do tabuleiro
 * com tema dark e troca de ícones (gema com tom verde-neon, mina, explosão).
 * Observa o modelo Cell para reagir automaticamente a revelações.
 */
public class CellButton extends JButton implements Observer {
    private static final Color BG_DEFAULT = Color.decode("#1E2230");
    private static final Color BG_MINE    = Color.decode("#B71C1C");

    private static ImageIcon gemIcon;
    private static ImageIcon mineIcon;
    private static ImageIcon explosionAnim;

    /** Ícone base atual (antes do resize), usado para redimensionamento dinâmico */
    private ImageIcon currentBaseIcon = null;

    private Cell cell;

    static {
        try {
            ClassLoader cl = CellButton.class.getClassLoader();
            // Carrega gema original
            BufferedImage origGem = ImageIO.read(cl.getResourceAsStream("assets/gem_medium.png"));
            // Cria versão tintada em verde-neon
            int w = origGem.getWidth(), h = origGem.getHeight();
            BufferedImage tintGem = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = tintGem.createGraphics();
            g2.drawImage(origGem, 0, 0, null);
            g2.setComposite(AlphaComposite.SrcAtop);
            g2.setColor(new Color(57, 255, 20, 200)); // verde-neon semi-transparente
            g2.fillRect(0, 0, w, h);
            g2.dispose();

            gemIcon = new ImageIcon(tintGem);
            // Ícone de mina e explosão mantêm originais
            mineIcon = new ImageIcon(ImageIO.read(cl.getResourceAsStream("assets/mine.png")));
            explosionAnim = new ImageIcon(ImageIO.read(cl.getResourceAsStream("assets/explosion.png")));
        } catch (Exception e) {
            System.err.println("Erro ao carregar ícones de CellButton:");
            e.printStackTrace();
            gemIcon = mineIcon = explosionAnim = null;
        }
    }

    /**
     * Construtor padrão: configura tema, borda e listener de redimensionamento.
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
     * Construtor com Cell: registra observador do modelo.
     */
    public CellButton(Cell cell) {
        this();
        this.cell = cell;
        cell.addObserver(this);
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

    /** Exibe o ícone de gema com tom verde-neon. */
    public void showGem() {
        if (gemIcon != null) {
            currentBaseIcon = gemIcon;
            int size = Math.min(getWidth(), getHeight());
            setIcon(resizeIcon(gemIcon, size));
        }
    }

    /** Exibe o ícone de mina e pinta o fundo de alerta. */
    public void showMine() {
        if (mineIcon != null) {
            currentBaseIcon = mineIcon;
            int size = Math.min(getWidth(), getHeight());
            setIcon(resizeIcon(mineIcon, size));
        }
        setBackground(BG_MINE);
    }

    /** Exibe animação de explosão sobre a célula e pinta o fundo de alerta. */
    public void showExplosion() {
        if (explosionAnim != null) {
            currentBaseIcon = explosionAnim;
            int size = Math.min(getWidth(), getHeight());
            setIcon(resizeIcon(explosionAnim, size));
        }
        setBackground(BG_MINE);
    }

    /**
     * Restaura o estado visual original (antes de qualquer reveal).
     */
    public void reset() {
        setIcon(null);
        setEnabled(true);
        setBackground(BG_DEFAULT);
        currentBaseIcon = null;
    }

    /**
     * Método do Observer: invocado quando a Cell muda.
     */
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
