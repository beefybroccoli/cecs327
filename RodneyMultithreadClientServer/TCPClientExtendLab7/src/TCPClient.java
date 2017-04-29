import java.io.IOException;

public class TCPClient {
    public static void main(String[] args) throws IOException {
        
        TCPClientThread tcp1 = new TCPClientThread("man");
        TCPClientThread tcp2 = new TCPClientThread("boy");
        
        tcp1.start();
        tcp2.start();
    
    }
    
}
