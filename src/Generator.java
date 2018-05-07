import java.io.File;

public class Generator {
public static void main(String[] args){
	File f = new File("res/kronkorken");
	listFilesForFolder(f);
}
public static void listFilesForFolder(final File folder) {
    for (final File fileEntry : folder.listFiles()) {
        if (fileEntry.isDirectory()) {
            listFilesForFolder(fileEntry);
        } else {
            System.out.println(fileEntry.getName()+" 423000");
        }
    }
}
}
