import java.util.LinkedList;

public class Directory extends FileSystemElement {
    
    // A Linked List containing child elements
    private LinkedList<FileSystemElement> children;
    
    // Constructor
    /**
     * Directory Constructore
     * @param RootName Name of root that directory will be named after
     * @param parent parent of directory
     */
    public Directory(String RootName, FileSystemElement parent) {

        super(RootName, parent);
        children = new LinkedList<FileSystemElement>();
    }

    /**
     * Method to return linked list 
     * @return returns nodes of directory
     */
    public LinkedList<FileSystemElement> GetChilds(){
        return children;
    }

    /**
     * Method to search for directory
     * @param NodeName Name of directory to be searched
     * @return
     */
    public FileSystemElement FindDirectory(String NodeName){

        for(FileSystemElement node : children)
        {
            if(node.GetName().equals(NodeName))
                return node;
        }

        return null;
    }

    /**
     * Method to find directory and delete
     * @param NodeName name of file
     * @param CurrDir parent of file 
     * @return
     */
    public boolean FindFileDelete(String NodeName, Directory CurrDir){

        for(FileSystemElement node : CurrDir.children)
        {
            if(node.GetName().equals(NodeName))
            {
                if(node instanceof File)
                {
                    CurrDir.remove(node);
                    return true;
                }
                
            }
        }
        return false;
    }

    /**
     * Method to find and delete a file of directory
     * @param NodeName Name of node to be deleted
     * @param CurrDir Current directory that will be searched from 
     * @return
     */
    public boolean FindDirDelete(String NodeName, Directory CurrDir){

        for(FileSystemElement node : CurrDir.children)
        {
            if(node.GetName().equals(NodeName))
            {
                if(node instanceof Directory)
                {
                    CurrDir.remove(node);
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Method to add FileSystemElement
     * @param element Directory or File
     */
    public void add(FileSystemElement element){
        children.add(element);
    }

    /**
     * Method to remove FileSystemElement
     * @param element Directory or File
     */
    public void remove(FileSystemElement element){
        children.remove(element);
    }
    
    public void manage(){
        
    }

    /**
     * Overridden print method to print name of directory with "/" character
     */
    @Override
    public void print(String prefix){
        System.out.print(prefix + GetName());
    }
}
