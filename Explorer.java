import java.util.Scanner;

public class Explorer {

    /* Root Directory variable */
    private static Directory CurrentDirectory;

    /* File System instance */
    private static FileSystem fs = new FileSystem();

    /* Scanner object for I/O */
    private static Scanner reader = new Scanner(System.in); 

    /**
     * Main Method to run program 
     * @param args default string array
     */
    public static void main(String[] args){

        // Link root
        CurrentDirectory = fs.GetRoot();

        // Variable to maintain loop continuity 
        boolean lock = true;

        // Create objects for I/O
        int selection;

        /*
         * I/O and system loop
         */
        while(true){

            fs.PrintMenu();
            
            if(reader.hasNextInt())
                selection = reader.nextInt();   
            else
                selection = -1;
            
            // Consume rest of line
            reader.nextLine();
            
            // Debug
            System.out.println(selection + " is selected!");
            FileSystem.PrintLine();

            switch (selection) {
                case 1:
                    ChangeDirectoryCall();
                    break;

                case 2:
                    ListContentsCall();
                    break;

                case 3:
                    CreateFileCall();
                    break;

                case 4:
                    CreateDirectoryCall();
                    break;

                case 5:
                    DeleteFileCall();
                    break;

                case 6:
                    DeleteDirectoryCall();
                    break;

                case 7:
                    MoveFileCall();
                    break;

                case 8:
                    MoveDirectoryCall();
                    break;

                case 9:
                    SearchFileCall();
                    break;
                
                case 10:
                    SearchDirectoryCall();
                    break;

                case 11:
                    PrintDirectoryTreeCall();
                    break;

                case 12:
                    SortContentsByDateCall();
                    break;

                case 13:
                    lock = false;
                    fs.MessageSelector(selection);
                    break;

                default:
                    fs.MessageSelector(-1);
                    break;
            }

            if(lock != true)
                break;
        }

        reader.close();

    }
    
    /**
     * Call FileSystem for Creating file
     */
    public static void CreateFileCall(){
        String fn;
        System.out.println("Enter the file name to create: ");
        fn = reader.nextLine();
        
        if(fs.CreateFile(fn, CurrentDirectory))
            System.out.println("-> Create call succeed");
        else
            System.out.println("-> Create call failed!");

    }

    /**
     * Call FileSystem for Deleting file
     */
    public static void DeleteFileCall(){

        String fn;
        System.out.println("Enter the file name to delete: ");
        fn = reader.nextLine();

        if(fs.DeleteFile(fn, CurrentDirectory))
            System.out.println("-> Delete call succeed");
        else
            System.out.println("-> Delete call failed, no such file!");
    }

    /**
     * Call FileSystem for Listing contents of directory
     */
    public static void ListContentsCall(){

        System.out.print("Listing contents of File System ");
        fs.ListContents(CurrentDirectory);
    }

    /**
     * Call FileSystem for Changing directory
     */
    public static void ChangeDirectoryCall(){

        System.out.print("Current directory: ");
        fs.GetCurrentPath(CurrentDirectory);
        Directory NewCurrDir;

        String fn;
        System.out.print("Enter new directory path: ");
        fn = reader.nextLine();
        NewCurrDir = fs.ChangeDirectory(fn);

        if( NewCurrDir != null)
        {
            CurrentDirectory = NewCurrDir;
            System.out.println("-> Change Directory call succeed");
            System.out.print("Directory changed to: ");
            fs.GetCurrentPath(CurrentDirectory);
        }
        else
            System.out.println("-> Change Directory call failed, path is invalid!");

    }

    /**
     * Call FileSystem for Creating Directory
     */
    public static void CreateDirectoryCall(){

        String fn;
        System.out.println("Enter the directory name to create: ");
        fn = reader.nextLine();
        
        if(fs.CreateDirectory(fn, CurrentDirectory))
            System.out.println("-> Create call succeed");
        else
            System.out.println("-> Create call failed!");

    }

    /**
     * Call FileSystem for Deleting Directory
     */
    public static void DeleteDirectoryCall(){

        String fn;
        System.out.println("Enter the directory name to delete: ");
        fn = reader.nextLine();

        Directory DirToRemove = fs.SearchDirectory(fn, fs.GetRoot());
        fs.DeleteDirectory(DirToRemove);

        if(DirToRemove != null)
            System.out.println("-> Delete call succeed");
        else
            System.out.println("-> Delete call failed, no such Directory!");
    }

    /**
     * Call FileSystem for Moving a file to another directory
     */
    public static void MoveFileCall(){

        String FileName;
        String DirectoryPath;
        String DirectoryName;
        String PathNodes[];

        System.out.print("Current directory: ");
        fs.GetCurrentPath(CurrentDirectory);
        System.out.print("Enter name of file: "); 
        FileName = reader.nextLine();

        System.out.print("Enter new directory path: "); 
        DirectoryPath = reader.nextLine();
        
        PathNodes = DirectoryPath.split("/");

        DirectoryName = PathNodes[PathNodes.length-1];

        fs.MoveFile(FileName, DirectoryName);

        CurrentDirectory = fs.GetRoot();
        System.out.println("Current directory chaged to Root!");
    }

    /**
     * Call FileSystem for Moving a directory to another directory
     */
    public static void MoveDirectoryCall(){

        String DirectoryName;
        String DirectoryPath;
        String NewDirectoryName;
        String PathNodes[];

        System.out.print("Current directory: ");
        fs.GetCurrentPath(CurrentDirectory);
        System.out.print("Enter name of directory: "); 
        DirectoryName = reader.nextLine();

        System.out.print("Enter new directory path: "); 
        DirectoryPath = reader.nextLine();
        
        PathNodes = DirectoryPath.split("/");

        NewDirectoryName = PathNodes[PathNodes.length-1];

        fs.MoveDirectory(DirectoryName, NewDirectoryName);

        CurrentDirectory = fs.GetRoot();
        System.out.println("Current directory chaged to Root!");
    }

    /**
     * Call FileSystem to Search for entered file
     */
    public static void SearchFileCall(){

        File FoundF = null;
        String fn;
        System.out.print("Search query: ");
        fn = reader.nextLine();

        System.out.println("Searching from root...");
        
        FoundF = fs.SearchFile(fn, fs.GetRoot());

        if( FoundF != null){
            System.out.print("Found: ");
            fs.GetCurrentPath((Directory)FoundF.GetParent());
            System.out.println("->" + FoundF.GetName());
        }
        else
            System.out.println("-> File is not present in the System");
        
    }

    /**
     * Call FileSystem for to Search for entered directory
     */
    public static void SearchDirectoryCall(){

        Directory FoundD = null;
        String fn;
        System.out.print("Search query: ");
        fn = reader.nextLine();

        System.out.println("Searching from root...");
        
        FoundD = fs.SearchDirectory(fn, fs.GetRoot());

        if( FoundD != null && FoundD != fs.GetRoot()){
            System.out.print("Found: ");
            fs.GetCurrentPath((Directory)FoundD.GetParent());
            System.out.println("->" + FoundD.GetName());
        }
        else if(FoundD == fs.GetRoot()){
            System.out.print("Found: ");
            System.out.println("/" + fs.GetRoot().GetName());
        }
        else
            System.out.println("-> Directory is not present in the System");
        
    }

    /**
     * Call FileSystem to Print Current directory's Tree
     */
    public static void PrintDirectoryTreeCall(){
        fs.PrintTree(CurrentDirectory);
    }

    /**
     * Call FileSystem to Sort contents of a directory by date
     */
    public static void SortContentsByDateCall(){
        System.out.print("Sorted contents of ");
        fs.GetCurrentPathDateCall(CurrentDirectory);
        fs.SortContentsByDate(CurrentDirectory);
    }
    
}