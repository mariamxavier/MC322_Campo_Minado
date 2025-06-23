package mc322_campo_minado;

/**
 * Interface do padrão Observer para atualização de componentes
 * quando o estado de um objeto muda.
 * 
 */
public interface Observer {
    /**
     * Método chamado para notificar o observador sobre mudanças no objeto observado.
     */
    void update();
}