import java.io.IOException;

public class Play{
    public static void main(String[] args) throws IOException {
        PlayThread thread = new PlayThread();
        thread.start();
    }
}
