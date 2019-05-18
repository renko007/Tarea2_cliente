// A simple Client Server Protocol .. Client for Echo Server

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class NetworkClient {

public static void main(String args[]) throws IOException{


    InetAddress address=InetAddress.getByName("192.168.0.12");
    Socket s1=null;
    DataInputStream  is = null;
    DataOutputStream os=null;

    try {
        s1=new Socket(address, 4445); // You can use static final constant PORT_NUM
        is = new DataInputStream(s1.getInputStream()); 
        os = new DataOutputStream(s1.getOutputStream()); 
    }
    catch (IOException e){
        e.printStackTrace();
        System.err.print("IO Exception");
    }

    System.out.println("Client Address : "+address);
    System.out.println("Enter Data to echo Server ( Enter QUIT to end):");

    try{
        Scanner scn = new Scanner(System.in); 
    	is = new DataInputStream(s1.getInputStream());
        os = new DataOutputStream(s1.getOutputStream());
        System.out.println(is.readUTF()); 
        String tosend = scn.nextLine(); 
        os.writeUTF(tosend); 
        while(tosend.compareTo("Exit")!=0){
	//comando ls
            if(tosend.equals("ls")) {
            	int expected=is.readInt();
            	int cont=0;
            	while (cont<expected) {
            		String received = is.readUTF(); 
                    System.out.println(received);
            		cont++;
            	}
            }
	//COMANDO get
            else if(tosend.contains("get")){
            	InputStream in = null;
                OutputStream out = null;
                long fileSize = is.readLong();
            	try {
                    in = s1.getInputStream();
                } catch (IOException ex) {
                    System.out.println("Can't get socket input stream. ");
                }

                try {
                    out = new FileOutputStream(tosend.split(" ")[1].split(".")[0]);
                } catch (FileNotFoundException ex) {
                    System.out.println("File not found. ");
                    continue;
                }

                byte[] bytes = new byte[64*1024];
                //System.out.println(fileSize);
                int count;
                //Thread.sleep(1000);
                while (fileSize > 0 && (count = in.read(bytes, 0, (int)Math.min(bytes.length, fileSize))) != -1) {
                    out.write(bytes, 0, count);
                    fileSize-=count;
                }
                out.close();
                System.out.println("Descarga realizada");
            }
	//COMANDO put
            else if(tosend.contains("put")) {
            	String[] parts = tosend.split(" ");
            	String name=parts[1];
            	System.out.println(name);
            	FileChannel fileChannel;
            	String current = new java.io.File( "." ).getCanonicalPath();
            	String fileName = current+"\\"+name;
                Path filePath = Paths.get(fileName);
            	fileChannel = FileChannel.open(filePath); //
                os.writeLong(fileChannel.size());
                byte [] bytes =new byte [16*1024];
                InputStream in = new FileInputStream(name);
                int count;
                while ((count = in.read(bytes)) > 0) {
                    os.write(bytes, 0, count);
                }
                System.out.println("Subida realizada");
                in.close();
            }
	//comando delete
            else if(tosend.contains("delete")) {
            	System.out.println(is.readUTF());
            }
            System.out.println(is.readUTF()); 
            tosend = scn.nextLine(); 
            os.writeUTF(tosend);
        }
        scn.close();
    }
    catch(IOException e){
        e.printStackTrace();
    System.out.println("Socket read Error");
    }
    finally{

        is.close();os.close();s1.close();
                System.out.println("Connection Closed");

    }

}
}
