//import models.DummyClient;
//import org.junit.jupiter.api.Test;
//
//import java.io.IOException;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class ServerTest {
//    @Test
//    public void givenClient1_whenServerResponds_thenCorrect() throws IOException {
//        DummyClient client1 = new DummyClient();
//        client1.startConnection("127.0.0.1", 5555);
//        String msg1 = client1.sendMessage("hello");
//        String msg2 = client1.sendMessage("world");
//        String terminate = client1.sendMessage(".");
//
//        assertEquals(msg1, "hello");
//        assertEquals(msg2, "world");
//        assertEquals(terminate, "bye");
//    }
//
//    @Test
//    public void givenClient2_whenServerResponds_thenCorrect() throws IOException {
//        DummyClient client2 = new DummyClient();
//        client2.startConnection("127.0.0.1", 5555);
//        String msg1 = client2.sendMessage("hello");
//        String msg2 = client2.sendMessage("world");
//        String terminate = client2.sendMessage(".");
//
//        assertEquals(msg1, "hello");
//        assertEquals(msg2, "world");
//        assertEquals(terminate, "bye");
//    }
//}
