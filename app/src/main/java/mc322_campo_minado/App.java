package mc322_campo_minado;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
        Minesweeper minesweeper = new Minesweeper();
    }
}
