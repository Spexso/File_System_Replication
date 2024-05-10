import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;


public class FileSystem {

    // Root of File System
    private Directory root;

    public FileSystem(){

        this.root = new Directory("root", null);
    }

    /**
     * Method to select which message to be printed
     * @param key choice for message
     */
    public void MessageSelector(int key){

        switch (key) {
            case -1:
                PrintLine();
                System.out.println("Invalid code!");
                break;
            
            case 13:
                System.out.println("Terminating.. Bye!");
                break;
             
            default:
                break;
        }
    }

    public static void PrintLine(){
        System.out.println("");
        System.out.println("");
    }

    public void PrintMenu(){

        System.out.println("===== File System Management Menu =====");
        System.out.println("1.  Change Directory");
        System.out.println("2.  List Directory Contents");
        System.out.println("3.  Create file");
        System.out.println("4.  Create directory");
        System.out.println("5.  Delete file");
        System.out.println("6.  Delete directory");
        System.out.println("7.  Move file");
        System.out.println("8.  Move directory");
        System.out.println("9.  Search file");
        System.out.println("10. Search directory");
        System.out.println("11. Print directory tree");
        System.out.println("12. Sort contents by date created");
        System.out.println("13. Exit");
        System.out.print("Please select an option: ");
    }

    /**
     * Method to create single file
     * @param name Name of file
     * @param CurrentDir Parent of file
     * @return returns true/false based on success
     */
    public boolean CreateFile(String name, Directory CurrentDir){
        CurrentDir.add(new File(name, CurrentDir)); 
        return true;
    }

    /**
     * Method to create single directory
     * @param name Name of directory
     * @param CurrentDir Parent of directory
     * @return returns true/false based on success
     */
    public boolean CreateDirectory(String name, Directory CurrentDir){
        CurrentDir.add(new Directory(name, CurrentDir)); 
        return true;
    }

    /**
     * Method to delete directory
     * @param name name of directory
     * @param CurrentDir Root of system
     * @return returns true/false based on success
     */
    public boolean DeleteFile(String name, Directory CurrentDir){
        // Find Node then remove it 
        boolean isFound;
        isFound = CurrentDir.FindFileDelete(name, CurrentDir);
        
        return isFound;
    }

    /**
     * Method to delete directory but uses loop
     * @param name Name of directory to delete
     * @param CurrentDir Root of system
     * @return returns true/false based on success
     */
    public boolean DeleteDirectoryNonRecursive(String name, Directory CurrentDir){
        // Find Node then remove it 
        boolean isFound;
        isFound = CurrentDir.FindDirDelete(name, CurrentDir);
        
        return isFound;
    }

    /**
     * Recursive method to delete directory
     * @param DirToDelete Directory to be deleted
     */
    public void DeleteDirectory(FileSystemElement DirToDelete){
        
        // Base case: directory is null or not a directory
        if (DirToDelete == null || !(DirToDelete instanceof Directory)) {
            return;
        }

        Directory CurrDir = (Directory)DirToDelete;

        // Delete all child directories and files recursively
        for (FileSystemElement child : CurrDir.GetChilds()) {
            DeleteDirectory(child);
        }

        // Remove the directory from its parent's children list
        Directory ParentDir = (Directory)DirToDelete.GetParent();
        ParentDir.GetChilds().remove(DirToDelete);
    }

    /**
     * Method to list all of FileSystemElements in directory
     * @param CurrDir Current Directory to be printed
     */
    public void ListContents(Directory CurrDir){
        
        GetCurrentPath(CurrDir);

        for(FileSystemElement elem : CurrDir.GetChilds())
        {
            if(elem.getClass() == Directory.class)
            {
                System.out.println("* " + elem.GetName() + "/");
            }
            else
                elem.print("");
        }

        System.out.println("");
        System.out.println("");
    }

    /**
     * Method to print current directory's path
     * @param dir Current Directory
     */
    public void GetCurrentPath(Directory dir){

        Directory navDir = dir;
        Stack<Directory> list = new Stack<Directory>();

        while(navDir.parent != null)
        {
            list.add(navDir);
            navDir = (Directory) navDir.parent;
        }

        // Add root to Stack too
        list.add(navDir);
        
        while(!list.empty()){
            System.out.print("/" + list.pop().GetName());
        }

        System.out.println("");
    }

    /**
     * Method to print current directory's path for Sorting method
     * @param dir Current Directory
     */
    public void GetCurrentPathDateCall(Directory dir){
        
        // Prints Current Directory if root
        if(dir.parent == null)
        {
            System.out.println("/" + dir.GetName() + " by date created");
            return;
        }

        Directory navDir = dir;
        Stack<Directory> list = new Stack<Directory>();

        while(navDir.parent != null)
        {
            list.add(navDir);
            navDir = (Directory) navDir.parent;
        }

        // Add root to Stack too
        list.add(navDir);
        
        while(!list.empty()){
            System.out.print("/" + list.pop().GetName());
        }

        System.out.println("");
    }

    /**
     * Method to get root
     * @return root
     */
    public Directory GetRoot(){
        return root;
    }

    /**
     * 
     * @param CurrDir
     */
    public void SortContentsByDate(Directory CurrDir){

        List<Timestamp> TSList = new ArrayList<>();
        ArrayList<FileSystemElement> EList = new ArrayList<>();

        for ( FileSystemElement elem : CurrDir.GetChilds())
        {
            TSList.add(elem.GetDateCreated());
        }

        Collections.sort(TSList);

        // Sync Lists
        for ( Timestamp TSElem : TSList)
        {
            for( FileSystemElement SysElem : CurrDir.GetChilds())
            {
                if (SysElem.GetDateCreated().equals(TSElem)){
                    EList.add(SysElem);
                }
            }
        }

        // Print Sorted List
        for ( FileSystemElement T : EList)
        {
            if(T instanceof Directory)
                System.out.println("* " + T.GetName() + "/ " + "(" + FormatTimeStamp(T.GetDateCreated()) + ")");
            else
                System.out.println(T.GetName() + "(" + FormatTimeStamp(T.GetDateCreated()) + ")");
        }

        System.out.println("");
    }

    /**
     * 
     * @param ts
     * @return
     */
    private String FormatTimeStamp(Timestamp ts){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(ts);
    }

    /**
     * 
     * @param path
     * @return
     */
    public Directory ChangeDirectory(String path){

        Directory CurrentDir = GetRoot();
        FileSystemElement NewCurrDir = new Directory("|Garbage|", null);

        String[] nodes = path.split("/");

        if(nodes.length < 2 || !CurrentDir.GetName().equals(nodes[1])){
            //DebugPrint("rootfailed1");
            return null;
        }
        else
        {
            if(nodes[1] != null)
            {
                if(CurrentDir.GetName().equals(nodes[1]) && nodes.length == 2){
                    return CurrentDir;
                }
            }
        }

        /* 
        DebugPrint("===============");
        DebugPrint(CurrentDir.GetName());
        DebugPrint(nodes[1]);
        DebugPrint("===============");
        */
        
        
        for(int i = 2; i < nodes.length; i++)
        {
            NewCurrDir = CurrentDir.FindDirectory(nodes[i]);

            if( NewCurrDir == null || NewCurrDir instanceof File)
            {
                return null;
            }

            CurrentDir = (Directory) NewCurrDir;
        }

        return (Directory) NewCurrDir;
    }

    /**
     * 
     * @param DName
     * @param CurrParent
     * @return
     */
    public Directory SearchDirectory(String DName, Directory CurrParent){

        //DebugPrint(FName);

        for(FileSystemElement child : CurrParent.GetChilds())
        {
            if (child instanceof Directory) {
                //System.out.println("Child is directory=>" + child.GetName());
                if (DName.equals(child.GetName())) {
                    return (Directory)child;
                }
                Directory FoundD = SearchDirectory(DName, (Directory)child);
                if(FoundD != null)
                    return FoundD;
            }
        }

        return null;
    }

    /**
     * 
     * @param FName
     * @param CurrParent
     * @return
     */
    public File SearchFile(String FName, Directory CurrParent){
        
        //DebugPrint(FName);
        
        for(FileSystemElement child : CurrParent.GetChilds())
        {
            if (child instanceof Directory) {
                //System.out.println("Child is directory=>" + child.GetName());
                File FoundF = SearchFile(FName, (Directory)child);
                
                if(FoundF != null)
                    return FoundF;
            }
            else{
                //System.out.println("Child is file=>" + child.GetName());
                if(FName.equals(child.GetName()))
                {
                    //System.out.println("File is found=>" + child.GetName());
                    return (File)child;
                }
            }
        }

        return null;
    }

    /**
     * 
     * @param n
     */
    public void PutNSpace(int n){

        for(int i = 0; i < n; i++)
            System.out.print(" ");
    }

    /**
     * Method to print path tree
     * @param dir Current directory
     */
    public void PrintTree(Directory dir){
        int n = 0;
        Directory parent;
        boolean isRoot = false;

        // Prints Current Directory if root
        if(dir.parent == null)
        {
            System.out.println("/" + dir.GetName() + " (Current Directory)");
            isRoot = true;
        }

        if(isRoot != true)
        {
            Directory navDir = dir;
            Stack<Directory> list = new Stack<Directory>();
        
            while(navDir.parent != null)
            {
                list.add(navDir);
                navDir = (Directory) navDir.parent;
            }

            // Add root to stack too
            list.add(navDir);
                
            while(!list.empty()){

                if(list.size() == 1)
                {
                    PutNSpace(n++);
                    System.out.println("/" + list.pop().GetName() + " (Current Directory)");
                }
                else
                {
                    PutNSpace(n++);
                    System.out.println("/" + list.pop().GetName());
                }   
            }
        }
        
        // Print other childs of directory
        parent = dir;
        
        for(FileSystemElement elem : parent.GetChilds())
        {
            if(!elem.equals(parent))
            {
                if(elem instanceof Directory)
                {
                    PutNSpace(2);
                    System.out.println("* " + elem.GetName() + "/");
                }
                else
                {
                    PutNSpace(2);
                    elem.print("");
                }
                    
            }
        }

        System.out.println("");
    }

    /**
     * Method to move file 
     * @param fn name of file
     * @param NewPath new path of file
     */
    public void MoveFile(String fn, String NewPath)
    {
        Directory MovingFileParent;
        File FileToMove;
        File FileCopy;
        Directory DirToBeMoved;

        FileToMove = SearchFile(fn, root);
        DirToBeMoved = SearchDirectory(NewPath, root);

        if (DirToBeMoved == null || FileToMove == null ) {
            System.out.println("Move Operation Failed, no such directory or file");
            return;
        }

        FileCopy = FileToMove;
        MovingFileParent = (Directory) FileToMove.GetParent();
        MovingFileParent.GetChilds().remove(FileToMove);
        DirToBeMoved.GetChilds().add(FileCopy);

        System.out.println("File Moved: " + FileCopy.GetName() + " to ");
        GetCurrentPath(DirToBeMoved);
    }

    public void MoveDirectory(String DirectoryName, String NewPath)
    {
        Directory MovingFileParent;
        Directory DirToMove;
        Directory DirCopy;
        Directory DirToBeMoved;

        DirToMove = SearchDirectory(DirectoryName, root);
        DirToBeMoved = SearchDirectory(NewPath, root);

        if (DirToBeMoved == null || DirToMove == null ) {
            System.out.println("Move Operation Failed, no such directory or file");
            return;
        }

        DirCopy = DirToMove;
        MovingFileParent = (Directory) DirToMove.GetParent();
        MovingFileParent.GetChilds().remove(DirToMove);
        DirToBeMoved.GetChilds().add(DirCopy);

        System.out.println("Directory Moved: " + DirCopy.GetName() + " to ");
        GetCurrentPath(DirToBeMoved);
    }

    /**
     * helper
     * @param x string
     */
    public void DebugPrint(String x){System.out.println("-" + x + "-");}
}   
